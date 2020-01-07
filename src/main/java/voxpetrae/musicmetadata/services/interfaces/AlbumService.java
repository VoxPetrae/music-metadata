package voxpetrae.musicmetadata.services.interfaces;

import voxpetrae.musicmetadata.models.AlbumTrack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public interface AlbumService {
    ObservableList<AlbumTrack> getAlbumTracks();
}