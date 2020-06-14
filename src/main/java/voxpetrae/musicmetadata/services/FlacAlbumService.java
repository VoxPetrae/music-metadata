package voxpetrae.musicmetadata.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;
import voxpetrae.musicmetadata.services.interfaces.AlbumService;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.services.interfaces.TagService;
import voxpetrae.musicmetadata.common.interfaces.IOHelper;

@SuppressWarnings("unchecked")
public class FlacAlbumService implements AlbumService {
    @Inject
    private IOHelper _ioHelper;
    @Inject
    private TagService _flacTagService;
    private String albumName;
    private String albumArtist;

    /**
     * {@inheritDoc}
     */
    public ObservableList<AlbumTrack> getAlbumTracks(String folderPath) {
        List<AlbumTrack> aTracks = new ArrayList<AlbumTrack>();
        //System.out.println("Trying folder " + folderPath + "...");
        var tkon = Paths.get(folderPath).toAbsolutePath();
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.forEach((Path filePath) -> {
                if (_ioHelper.isAudioFile(filePath)) {
                    try {
                        var flacTag = (FlacTag) _flacTagService.getTag(_ioHelper.getFileFromFilePath(filePath));
                        AudioFile audioFile = null;

                        audioFile = AudioFileIO.read(new File(filePath.toString()));
                        var generalTag = audioFile.getTag();
                        Iterator tagFields = generalTag.getFields();
                        List<TagField> ftmp2 = copyIterator(tagFields);

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
                    } catch (CannotReadException cre) {
                        System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + cre);
                    } catch (IOException ex) {
                        System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + ex);
                    } catch (TagException tex) {
                        System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + tex);
                    } catch (ReadOnlyFileException rex) {
                        System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + rex);
                    } catch (InvalidAudioFrameException iex) {
                        System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + iex);
                    }
                } else {
                    //System.out.println(filePath + " is not a flac file!");
                }
            });
        } catch (IOException ex) {
            System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + ex);
        }
        ObservableList<AlbumTrack> albumTracks = FXCollections.observableArrayList(aTracks);
        return albumTracks;
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

    /**
     * Converts iterator to list
     * 
     * @param iter The Iterator to convert
     * @param <T>  The type
     * @return A List with the elements from the Iterator
     */
    private static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }
}