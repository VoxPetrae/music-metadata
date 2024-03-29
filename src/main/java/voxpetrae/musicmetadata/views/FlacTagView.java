package voxpetrae.musicmetadata.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import voxpetrae.musicmetadata.common.interfaces.IOHelper;
import voxpetrae.musicmetadata.views.interfaces.TagView;
import voxpetrae.musicmetadata.views.interfaces.TableBuilder;
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
    private IOHelper ioHelper;
    private TagService<FlacTag> flacTagService;
    private TableBuilder<VorbisCommentTagField> tableBuilder;
    
    @Inject
    public FlacTagView(IOHelper ioHelper, TagService<FlacTag> flacTagService, TableBuilder<VorbisCommentTagField> tableBuilder){
        this.ioHelper = ioHelper;
        this.flacTagService = flacTagService;
        this.tableBuilder = tableBuilder;
    }
    public void initiate(Path filePath){
        if (Boolean.TRUE.equals(ioHelper.isAudioFile(filePath, "flac"))){
            File file = filePath.toFile();
            FlacTag tag = (FlacTag) flacTagService.getTag(file);
            if (tag == null){
                System.out.println("No tag for file!");
            }
            else{
                // Vorbis comment is the tag format used with Flac
                List<VorbisCommentTagField> ftmp2 = copyIterator(tag.getFields());
                for (VorbisCommentTagField vorbisCommentTagField : ftmp2) {
                    System.out.println("ogg vorbis field: " + vorbisCommentTagField.getId() + " = " + vorbisCommentTagField.toString());
                }
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
        TableView<VorbisCommentTagField> table = tableBuilder.buildTable(fields);
        //TableView table = buildTable(fields);
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
    
    EventHandler<ActionEvent> exitHandler = event -> {
        System.out.println("Closing Tag View...");
        Stage stage = getStageFromEvent(event);
        try{
            stage.close();
        }
        catch(Exception e){
            System.out.println("Oops! Closing Tag View failed because of " + e);
        }
    };

    /*
    Hack to access Stage from ActionEvent using Node properties
    */
    private Stage getStageFromEvent(ActionEvent event){
        MenuItem item = (MenuItem) event.getTarget();
        Menu menu = item.getParentMenu();
        MenuBar bar = (MenuBar) menu.getProperties().get(MenuBar.class.getCanonicalName());
        Node node = bar;
        return (Stage) node.getScene().getWindow();
    }

    /**
     * Converts iterator to list
     * @param iter The Iterator to convert
     * @param <T> The type
     * @return A List with the elements from the Iterator
     */
    private static <T> List<VorbisCommentTagField> copyIterator(Iterator<T> iter) {
        List<VorbisCommentTagField> copy = (List<VorbisCommentTagField>) new ArrayList<T>();
        while (iter.hasNext())
            copy.add((VorbisCommentTagField) iter.next());
        return copy;
    }
}
