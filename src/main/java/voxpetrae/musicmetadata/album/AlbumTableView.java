package voxpetrae.musicmetadata.album;

import java.io.File;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javax.inject.Inject;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.interfaces.TableBuilderInterface;
import voxpetrae.musicmetadata.helpers.interfaces.IOHelperInterface;
import voxpetrae.musicmetadata.services.interfaces.AlbumService;

public class AlbumTableView extends Stage implements AlbumView {
    private Button quitButton;
    @Inject private IOHelperInterface _ioHelper;
    @Inject private AlbumService _flacAlbumService;
    @Inject private TableBuilderInterface _tableBuilder;

    public void initiate(){
        _ioHelper.setFolderPath(false, "Choose folder");
        String folderPath = _ioHelper.getFolderPath();
        ObservableList<AlbumTrack> tracks = _flacAlbumService.getAlbumTracks(folderPath);
        // tracks.forEach((track) -> {
        //     System.out.println("testing: " + track.getTitle());
        // });
        drawGui(tracks);
    }
    private void drawGui(ObservableList<AlbumTrack> tracks){
        final VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 1000, 500);
        MenuBar menuBar = buildMenu();
        Label label = buildImageLabel();
        TableView table = _tableBuilder.buildTable(tracks);
        quitButton = buildQuitButton();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(0, 10, 10, 0));
        //((VBox) scene.getRoot()).getChildren().addAll(menuBar, quitButton);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, label, table);
        this.setTitle("Music Album");
        this.setScene(scene);
        this.show();
        this.setOnCloseRequest(a -> {
            this.close();
        });
    }
    private MenuBar buildMenu(){
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(exitHandler);
        fileMenu.getItems().add(quit);
        fileMenu.getProperties().put(MenuBar.class.getCanonicalName(), menuBar); // Hack to access Node from EventHandler
        return menuBar;
    }
    private Label buildImageLabel(){
        Label label = new Label(_flacAlbumService.getAlbumArtist() + ": " + _flacAlbumService.getAlbumName());
        label.setFont(new Font("Arial", 20));
        label.setPadding(new Insets(10));
        label.getStyleClass().add("tableLable");
        String folderPath = _ioHelper.getFolderPath();
        String imagePath = folderPath + "\\Folder.jpg";
        Image image = new Image(new File(imagePath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        label.setGraphic(imageView);
        return label;
    }

    private Button buildQuitButton(){
        Button button = new Button("Quit" + _ioHelper.getFolderPath());
        //button.getStyleClass().add("marginalized-button");
        button.setOnAction(exitHandler);
        //button.setDisable(true);
        return button;
    }
   
    EventHandler<ActionEvent> exitHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Closing Album View...");
            Stage stage = getStageFromEvent(event);
            try{
                stage.close();
            }
            catch(Exception e){
                System.out.println("Oops! Closing Album View failed because of " + e);
            }
        }
    };
    /*
    Helper to access Stage from ActionEvent.
    The thing is that MenuItem isn't a Node, but MenuBar is, and Button is.
    So you have to check if the event target is a MenuItem before you get the window to close.
    Alternatively, you could use different event handlers for exit actions on Button and MenuItem.
    Or, you could find an even better way to deal with this. I'm certain there is one :).
    */
    private Stage getStageFromEvent(ActionEvent event){
        Node node;
        var target = event.getTarget();
        if (target instanceof MenuItem){
            System.out.println("Target ");
            MenuItem item = (MenuItem) event.getTarget();
            Menu menu = item.getParentMenu();
            MenuBar bar = (MenuBar) menu.getProperties().get(MenuBar.class.getCanonicalName());
            node = (Node) bar;
        }
        else{
            node = (Node) event.getSource(); 
        }
        Stage stage = (Stage) node.getScene().getWindow();
        return stage;
    }
}