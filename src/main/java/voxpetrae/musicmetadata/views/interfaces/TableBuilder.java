package voxpetrae.musicmetadata.views.interfaces;

import javafx.scene.control.*;
import javafx.collections.ObservableList;

public interface TableBuilder<T> {
    TableView<T> buildTable(ObservableList<T> tracks);
}