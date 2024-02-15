package voxpetrae.musicmetadata.views;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;

import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;
import voxpetrae.musicmetadata.views.interfaces.TableBuilder;
import voxpetrae.musicmetadata.textfieldworkaround.StringTableCell;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unchecked")
public class FlacTagTableBuilder<T> implements TableBuilder<VorbisCommentTagField> {
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(FlacTagTableBuilder.class);
    public TableView<VorbisCommentTagField> buildTable(ObservableList<VorbisCommentTagField> fields){
        logger.info("Building table for flac tracks...");
        TableView<VorbisCommentTagField> table = new TableView<>();
        table.getStyleClass().add("tableStyle");
        table.setEditable(true);
        TableColumn<VorbisCommentTagField, String> idColumn = new TableColumn<>("Tag ID");
        TableColumn<VorbisCommentTagField, String> contentColumn = new TableColumn<>("Content");
        idColumn.setMinWidth(100);
        // The column's cell value factory is used to populate the column cells
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
        contentColumn.prefWidthProperty().bind(table.widthProperty().divide(2));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        contentColumn.setCellFactory(cellDataFeatures -> new StringTableCell<VorbisCommentTagField, String>());
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