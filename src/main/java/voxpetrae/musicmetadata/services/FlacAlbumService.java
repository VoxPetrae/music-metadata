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
//import voxpetrae.musicmetadata.services.FlacTagService;
import voxpetrae.musicmetadata.helpers.interfaces.IOHelperInterface;

public class FlacAlbumService implements AlbumService {
    @Inject private IOHelperInterface _ioHelper;
    @Inject private TagService _flacTagService;
    private String albumName;
    private String albumArtist;
       
    /**
     * {@inheritDoc}
     */
    public ObservableList<AlbumTrack> getAlbumTracks(String folderPath){
        List<AlbumTrack> aTracks = new ArrayList<AlbumTrack>();
        System.out.println("Trying folder " + folderPath + "...");
        var tkon = Paths.get(folderPath).toAbsolutePath();
        try(Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.forEach((Path filePath) -> {
                if (_ioHelper.isAudioFile(filePath)) {
                    var flacTag = (FlacTag) _flacTagService.getTag(_ioHelper.getFileFromFilePath(filePath));
                    if (flacTag != null && !flacTag.isEmpty()){
                        var track = Integer.parseInt(flacTag.getFirst(FieldKey.TRACK));
                        var title = flacTag.getFirst(FieldKey.TITLE);
                        var artists = convertTagListToSemicolonSeparatedString(flacTag, FieldKey.ARTIST);
                        var albumArtists = convertTagListToSemicolonSeparatedString(flacTag, FieldKey.ALBUM_ARTIST);
                        var composers = convertTagListToSemicolonSeparatedString(flacTag, FieldKey.COMPOSER);
                        var genre = flacTag.getFirst(FieldKey.GENRE);
                        var year = flacTag.getFirst(FieldKey.YEAR);
                        System.out.println("Track no: " + track + ", title: " + title + ", artists: " + artists + ", albumArtists: " + albumArtists + ", composers: " + composers +", genre: " + genre + ", year: " + year + ", album name: " + albumName + ", album artist: " + albumArtist);
                        aTracks.add(new AlbumTrack(track, title, artists, albumArtists, composers, genre, year, filePath.toString(), false));
                        if (albumName == null)
                            albumName = flacTag.getFirst(FieldKey.ALBUM);
                        if (albumArtist == null)
                            albumArtist = flacTag.getFirst(FieldKey.ALBUM_ARTIST);
                    }
                    else {
                        System.out.println("Not a real ID tag: " + flacTag.toString());
                    }
                }
                else
                {
                    System.out.println(filePath + " is not a flac file!");
                    //MegaMetaLogger.error(filePath + " is not a flac file!");
                }
            });
        } catch (IOException ex) {
            System.out.println("Exception in " + FlacAlbumService.class.getName() + ": " + ex);
        }
        ObservableList<AlbumTrack> albumTracks = FXCollections.observableArrayList(aTracks);
        return albumTracks;
    }
    public String getAlbumName(){
        return albumName;
    }
    public String getAlbumArtist()
    {
        return albumArtist;
    }

    private String convertTagListToSemicolonSeparatedString(FlacTag flacTag, FieldKey fieldKey){
        List<TagField> tagFields = flacTag.getFields(fieldKey);
        StringBuilder sb = new StringBuilder();
        tagFields.forEach((TagField tagField) -> {
            VorbisCommentTagField tf = (VorbisCommentTagField) tagField;
            sb.append(tf.getContent());
            if (tagFields.indexOf(tagField) < tagFields.size()-1)
                sb.append("; ");
        });
        return sb.toString();
    }
}