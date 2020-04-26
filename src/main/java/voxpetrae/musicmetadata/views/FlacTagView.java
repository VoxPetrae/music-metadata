package voxpetrae.musicmetadata.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import voxpetrae.musicmetadata.common.interfaces.IOHelperInterface;
import voxpetrae.musicmetadata.textfieldworkaround.StringTableCell;
import voxpetrae.musicmetadata.views.interfaces.TagView;
import voxpetrae.musicmetadata.services.interfaces.TagService;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

@SuppressWarnings("unchecked")
public class FlacTagView extends Stage implements TagView{
    @Inject private IOHelperInterface _ioHelper;
    @Inject private TagService _tagService;
    
    public void initiate(Path filePath){
        if (_ioHelper.isAudioFile(filePath)){
            File file = filePath.toFile();
            FlacTag tag = (FlacTag) _tagService.getTag(file);
            if (tag == null){
                System.out.println("No tag for file!");
            }
            else{
                Iterator ftmp1 = tag.getFields();
                // Vorbis comment is the tag format used with Flac
                List<VorbisCommentTagField> ftmp2 = copyIterator(ftmp1);
                // ObservableList is an array list that allows a listener to track changes
                // The methods of FXCollections mirrors java.util.Collection methods
                ObservableList<VorbisCommentTagField> fields = FXCollections.observableArrayList(ftmp2);
                drawGui(fields);
            }
        }
    }
    public void drawGui(ObservableList<VorbisCommentTagField> fields){
        final VBox vbox = new VBox();
        Scene scene = new Scene(vbox, 809, 500);
        // File f = new File("src/main/resources/voxpetrae/musicmetadata/css/musicmetadata.css");
        // String fileURI = f.toURI().toString();
        // scene.getStylesheets().add(fileURI);
        var cssPath = getClass().getResource("../css/musicmetadata.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        MenuBar menuBar = buildMenu();
        TableView table = buildTable(fields);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(0, 10, 10, 0));
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, table);
        this.setTitle("Tag fields");
        this.setScene(scene);
        this.show();
        this.setOnCloseRequest(a -> {
            this.close();
        });
    }
    private MenuBar buildMenu(){
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        //Menu toolsMenu = new Menu("Tools");
        menuBar.getMenus().addAll(fileMenu);
        //MenuItem changeNameOrder = new MenuItem("Change name order");
        MenuItem quit = new MenuItem("Quit");
        //changeNameOrder.setOnAction(changeNameOrderHandler);
        quit.setOnAction(exitHandler);
        fileMenu.getItems().addAll(quit);
        fileMenu.getProperties().put(MenuBar.class.getCanonicalName(), menuBar); // Hack to access Node from EventHandler
        return menuBar;
    }
    // Todo: Maybe this should be refactored to a separate class, like we did in AlbumTableView. I'll think about it.
    private TableView buildTable(ObservableList<VorbisCommentTagField> fields){
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
    EventHandler<ActionEvent> exitHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Closing Tag View...");
            Stage stage = getStageFromEvent(event);
            try{
                stage.close();
            }
            catch(Exception e){
                System.out.println("Oops! Closing Tag View failed because of " + e);
            }
        }
    };
    /*
    Hack to access Stage from ActionEvent using Node properties
    */
    private Stage getStageFromEvent(ActionEvent event){
        MenuItem item = (MenuItem) event.getTarget();
        Menu menu = item.getParentMenu();
        MenuBar bar = (MenuBar) menu.getProperties().get(MenuBar.class.getCanonicalName());
        Node node = (Node) bar;
        Stage stage = (Stage) node.getScene().getWindow();
        return stage;
    }

    /**
     * Converts iterator to list
     * @param iter The Iterator to convert
     * @param <T> The type
     * @return A List with the elements from the Iterator
     */
    private static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }
}
