package voxpetrae.musicmetadata.album.interfaces;

import javafx.scene.control.*;
import javafx.collections.ObservableList;
import voxpetrae.musicmetadata.models.AlbumTrack;

public interface TableBuilderInterface {
    TableView buildTable(ObservableList<AlbumTrack> tracks);
}