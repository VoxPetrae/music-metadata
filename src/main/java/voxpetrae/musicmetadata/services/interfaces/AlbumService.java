package voxpetrae.musicmetadata.services.interfaces;

import voxpetrae.musicmetadata.models.AlbumTrack;
import javafx.collections.ObservableList;

/**
 * Provides the caller (typically a view/GUI) with a list of album tracks.
 */
public interface AlbumService {
    /**
     * For each file in the given folder the metadata tag is extracted and used to create an AlbumTrack.
     * @param folderPath - The path to the music album.
     * @return A list with AlbumTracks representing the music album's songs. 
     */
    ObservableList<AlbumTrack> getAlbumTracks(String folderPath);
    String getAlbumName();
    String getAlbumArtist();
}