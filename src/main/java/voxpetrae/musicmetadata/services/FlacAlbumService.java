package voxpetrae.musicmetadata.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;
import voxpetrae.musicmetadata.services.interfaces.AlbumService;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.services.interfaces.TagService;
import voxpetrae.musicmetadata.common.interfaces.IOHelper;

//@SuppressWarnings("unchecked")
public class FlacAlbumService implements AlbumService {
    @Inject private IOHelper _ioHelper;
    @Inject private TagService _flacTagService;
    private String albumName;
    private String albumArtist;

    /**
     * {@inheritDoc}
     */
    public ObservableList<AlbumTrack> getAlbumTracks(String folderPath) {
        
        List<AlbumTrack> aTracks = new ArrayList<AlbumTrack>();
        //System.out.println("Trying folder " + folderPath + "...");
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.forEach((Path filePath) -> {
                if (_ioHelper.isAudioFile(filePath, "flac")) {
                    try {
                        var flacTag = (FlacTag) _flacTagService.getTag(_ioHelper.getFileFromFilePath(filePath));
                        // AudioFile audioFile = null;

                        // audioFile = AudioFileIO.read(new File(filePath.toString()));
                        // var generalTag = audioFile.getTag();
                        // var tagFields = generalTag.getFields();
                        
                        /* for (TagField field : ftmp2) {
                            System.out.println("field: " + field.toString());
                        } */

                        if (flacTag != null && !flacTag.isEmpty()) {
                            var track = Integer.parseInt(flacTag.getFirst(FieldKey.TRACK));
                            var title = flacTag.getFirst(FieldKey.TITLE);
                            var artists = convertTagListToSemicolonSeparatedString(flacTag, FieldKey.ARTIST);
                            var albumArtists = convertTagListToSemicolonSeparatedString(flacTag, FieldKey.ALBUM_ARTIST);
                            var composers = convertTagListToSemicolonSeparatedString(flacTag, FieldKey.COMPOSER);
                            var genre = flacTag.getFirst(FieldKey.GENRE);
                            var year = flacTag.getFirst(FieldKey.YEAR);
                            /* System.out.println("Track no: " + track + ", title: " + title + ", artists: " + artists
                                    + ", albumArtists: " + albumArtists + ", composers: " + composers + ", genre: "
                                    + genre + ", year: " + year + ", album name: " + albumName + ", album artist: "
                                    + albumArtist); */
                            aTracks.add(new AlbumTrack(track, title, artists, albumArtists, composers, genre, year,
                                    filePath.toString(), false));
                            if (albumName == null)
                                albumName = flacTag.getFirst(FieldKey.ALBUM);
                            if (albumArtist == null)
                                albumArtist = flacTag.getFirst(FieldKey.ALBUM_ARTIST);
                        } else {
                            System.out.println("Not a real ID tag: " + flacTag.toString());
                        }
                    } catch (Exception eee) {
                        System.out.println("Exception in FlacAlbumService: " + eee.toString());
                    // } catch (CannotReadException cre) {
                    //     System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + cre);
                    // } catch (IOException ex) {
                    //     System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + ex);
                    // } catch (TagException tex) {
                    //     System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + tex);
                    // } catch (ReadOnlyFileException rex) {
                    //     System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + rex);
                    // } catch (InvalidAudioFrameException iex) {
                    //     System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + iex);
                    }
                } else {
                    //System.out.println(filePath + " is not a flac file!");
                }
            });
        } catch (IOException ex) {
            System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + ex);
        }
        ObservableList<AlbumTrack> albumTracks = FXCollections.observableArrayList(aTracks);
        //ObservableList<AlbumTrack> albumTracks = FXCollections.observableArrayList(AlbumTrack.extractor());
        //albumTracks.addAll(aTracks);
        return albumTracks;
    }

    public boolean saveAlbumTracksToFile(ObservableList<AlbumTrack> albumTracks, String folderPath){
        try(Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.forEach((Path filePath) -> {
                if (_ioHelper.isAudioFile(filePath, "flac")) {
                    var file = filePath.toFile();
                    FlacTag tag = (FlacTag) _flacTagService.getTag(file);
                    if (tag != null && !tag.isEmpty()){
                        int trackNumber = Integer.parseInt(tag.getFirst(FieldKey.TRACK));
                        AlbumTrack track = getAlbumTrack(albumTracks, trackNumber);
                        var artists = convertTagListToSemicolonSeparatedString(tag, FieldKey.ARTIST);
                        var albumArtists = convertTagListToSemicolonSeparatedString(tag, FieldKey.ALBUM_ARTIST);
                        var composers = convertTagListToSemicolonSeparatedString(tag, FieldKey.COMPOSER);
                        System.out.println(track.getTrackNumber() + " = " + track.getTitle() + ", ARTIST: " + artists + " (" + track.getArtist() + "), ALBUM ARTIST: " + albumArtists +
                        " (" + track.getAlbumArtist() + "), COMPOSER: " + composers + " (" + track.getComposer() + "), " +
                        artists.equals(track.getArtist()) + ", " + albumArtists.equals(track.getAlbumArtist()) + ", " +
                        composers.equals(track.getComposer()) + "!");
                        // 1: Hur mappa alla värden i track mot taggarna i flactag?
                        // 2: Om man låter AlbumTrack ha dynamiska properties som får sina värden vid inläsning av flac-filen.
                        _flacTagService.updateTag(tag, file);
                        track.setUnsaved(false);
                        //albumTracks.add(new AlbumTrack(track, title, artists, albumArtists, composers, filePath.toString(), false));
                    }
                    else {
                        System.out.println("Not a real ID tag: " + tag.toString());
                    }
                }
                else
                {
                    System.out.println(filePath + " is not a flac file!");
                }
            });
        } catch (IOException ex) {
            System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + ex);
            return false;
        }
        /* albumTracks.forEach((AlbumTrack track) -> {
            track.setUpdated(false);
        }); */
        return true;
    }
    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    private String convertTagListToSemicolonSeparatedString(FlacTag flacTag, FieldKey fieldKey) {
        List<TagField> tagFields = flacTag.getFields(fieldKey);
        StringBuilder sb = new StringBuilder();
        tagFields.forEach((TagField tagField) -> {
            VorbisCommentTagField tf = (VorbisCommentTagField) tagField;
            sb.append(tf.getContent());
            if (tagFields.indexOf(tagField) < tagFields.size() - 1)
                sb.append("; ");
        });
        return sb.toString();
    }
    
    private AlbumTrack getAlbumTrack(ObservableList<AlbumTrack> albumTracks, int trackNumber){
        int size = albumTracks.size();
        for(int index = 0; index < size; index++){
            AlbumTrack currentTrack = albumTracks.get(index);
            if (currentTrack.getTrackNumber() == trackNumber){
                return currentTrack;
            }
        }
        return null;
    }

    /**
     * Converts iterator to list
     * 
     * @param iter The Iterator to convert
     * @param <T>  The type
     * @return A List with the elements from the Iterator
     
    private static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }*/
}