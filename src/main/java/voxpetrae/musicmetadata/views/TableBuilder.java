package voxpetrae.musicmetadata.views;

import java.nio.file.Paths;
import java.nio.file.Path;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.util.Callback;
//import javafx.scene.input.MouseButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import com.google.inject.Guice;
import com.google.inject.Injector;
import voxpetrae.musicmetadata.guice.MusicMetadataModule;


import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.views.interfaces.TagView;
import voxpetrae.musicmetadata.views.interfaces.TableBuilderInterface;
import voxpetrae.musicmetadata.textfieldworkaround.StringTableCell;

//@SuppressWarnings("unchecked")
public class TableBuilder implements TableBuilderInterface {
    @SuppressWarnings("unchecked")
    public TableView buildTable(ObservableList<AlbumTrack> tracks){
        // Create a TableView and make its AlbumTrack items observable and editable
        TableView<AlbumTrack> table = new TableView();
        table.setEditable(true);
        table.setId("tracks");
        TableColumn<AlbumTrack, Number> trackColumn = new TableColumn("Track no.");
        trackColumn.setCellValueFactory(new PropertyValueFactory<>("trackNumber"));
        // Title column
        TableColumn titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<AlbumTrack, String>("title"));
        titleColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        titleColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<AlbumTrack, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setTitle(cellEditEvent.getNewValue()));
        // Artist column
        TableColumn artistColumn = new TableColumn("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        artistColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<AlbumTrack, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setArtist(cellEditEvent.getNewValue()));
        // Album artist column
        TableColumn albumArtistColumn = new TableColumn("Album Artist");
        albumArtistColumn.setCellValueFactory(new PropertyValueFactory<>("albumArtist"));
        albumArtistColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        albumArtistColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<AlbumTrack, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setAlbumArtist(cellEditEvent.getNewValue()));
        // Composer column
        TableColumn composerColumn = new TableColumn("Composer");
        composerColumn.setCellValueFactory(new PropertyValueFactory<>("composer"));
        composerColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        composerColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<AlbumTrack, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setComposer(cellEditEvent.getNewValue()));
        // Genre column
        TableColumn genreColumn = new TableColumn("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        //genreColumn.setOnEditCancel(t -> toggleButtonStatus(false));
        genreColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<AlbumTrack, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setGenre(cellEditEvent.getNewValue()));
        // Year column
        TableColumn yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearColumn.setCellFactory(cellDataFeatures -> new StringTableCell());
        //yearColumn.setOnEditCancel(t -> toggleButtonStatus(false));
        yearColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<AlbumTrack, String>>) cellEditEvent -> cellEditEvent.getTableView().getItems().get(
                        cellEditEvent.getTablePosition().getRow()).setYear(cellEditEvent.getNewValue()));
        // Updated column
        TableColumn updatedColumn = new TableColumn<>("Updated");
        updatedColumn.setCellValueFactory(new PropertyValueFactory<>("updated"));
        TableColumn viewAllColumn = new TableColumn("View all tags");
        viewAllColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AlbumTrack, Boolean>, ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<AlbumTrack, Boolean> p){
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
        viewAllColumn.setCellFactory(new Callback<TableColumn<AlbumTrack, Boolean>, TableCell<AlbumTrack, Boolean>>() {
            @Override
            public TableCell<AlbumTrack, Boolean> call(TableColumn<AlbumTrack, Boolean> p) {
                return new ButtonCell();
            }
        });
        table.getColumns().addAll(trackColumn, titleColumn, artistColumn, albumArtistColumn, composerColumn, genreColumn, yearColumn, updatedColumn, viewAllColumn);
        table.setItems(tracks);
        return table;
    }
    //Define the button cell
    private class ButtonCell extends TableCell<AlbumTrack, Boolean> {
        final Button cellButton = new Button("View");

        ButtonCell(){

            //Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    AlbumTrack currentTrack = ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                    Path filePath = Paths.get(currentTrack.getFilePath());
                    Injector injector = Guice.createInjector(new MusicMetadataModule());
                    TagView tagView = injector.getInstance(TagView.class);
                    tagView.initiate(filePath);
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }
}