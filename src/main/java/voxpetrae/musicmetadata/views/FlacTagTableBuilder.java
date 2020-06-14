package voxpetrae.musicmetadata.views;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;

import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;
import voxpetrae.musicmetadata.views.interfaces.TableBuilder;
import voxpetrae.musicmetadata.textfieldworkaround.StringTableCell;

public class FlacTagTableBuilder<T> implements TableBuilder<VorbisCommentTagField> {
    public TableView buildTable(ObservableList<VorbisCommentTagField> fields){
        TableView table = new TableView();
        table.getStyleClass().add("tableStyle");
        table.setEditable(true);
        TableColumn idColumn = new TableColumn("Tag ID");
        TableColumn contentColumn = new TableColumn("Content");
        idColumn.setMinWidth(100);
        // The column's cell value factory is used to populate the column cells
        idColumn.setCellValueFactory(new PropertyValueFactory<VorbisCommentTagField, String>("id"));
        idColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
        contentColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
        contentColumn.setCellValueFactory(new PropertyValueFactory<VorbisCommentTagField, String>("content"));
        contentColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        contentColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<VorbisCommentTagField, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setContent(cellEditEvent.getNewValue()));

        table.getColumns().addAll(idColumn, contentColumn);
        table.setItems(fields);
        table.setRowFactory(tv -> {
            TableRow<VorbisCommentTagField> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) { //             && event.getClickCount() == 2
                    VorbisCommentTagField tagField = row.getItem();
                    //System.out.println("Clicked: " + tagField.getId() + " = " + tagField.getContent());
                }
            });
            return row ;
        });
        return table;
    }
}