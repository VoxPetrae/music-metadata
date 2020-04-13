package voxpetrae.musicmetadata.album;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javax.inject.Inject;
import org.jaudiotagger.audio.exceptions.*;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.interfaces.TableBuilderInterface;
import voxpetrae.musicmetadata.common.interfaces.IOHelperInterface;
import voxpetrae.musicmetadata.common.NameOrder;
import voxpetrae.musicmetadata.common.NameTagsToChange;
import voxpetrae.musicmetadata.services.interfaces.AlbumService;
import voxpetrae.musicmetadata.services.interfaces.NameOrderService;

public class AlbumTableView extends Stage implements AlbumView {
    private ObservableList<AlbumTrack> tracks;
    private Button quitButton;
    @Inject private IOHelperInterface _ioHelper;
    @Inject private AlbumService _flacAlbumService;
    @Inject private TableBuilderInterface _tableBuilder;
    @Inject private NameOrderService _nameOrderService;

    public void initiate() {
        _ioHelper.setFolderPath("Choose folder");
        String folderPath = _ioHelper.getFolderPath();
        if (folderPath != null){
            tracks = _flacAlbumService.getAlbumTracks(folderPath);    
        }
        else{
            return;
        }
        drawGui(tracks);
    }
    private void drawGui(ObservableList<AlbumTrack> tracks){
        final VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 1000, 500);
        var cssPath = getClass().getResource("../css/musicmetadata.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        MenuBar menuBar = buildMenu();
        Label label = buildImageLabel();
        TableView table = _tableBuilder.buildTable(tracks);
        quitButton = buildQuitButton();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(0, 10, 10, 0));
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
        Menu toolsMenu = new Menu("Tools");
        menuBar.getMenus().addAll(fileMenu, toolsMenu);
        MenuItem quit = new MenuItem("Quit");
        MenuItem changeNameOrder = new MenuItem("Change name order");
        quit.setOnAction(exitHandler);
        changeNameOrder.setOnAction(changeNameOrderHandler);
        fileMenu.getItems().add(quit);
        toolsMenu.getItems().add(changeNameOrder);
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
    EventHandler<ActionEvent> changeNameOrderHandler = new EventHandler<ActionEvent>() {
        /**
         * Opens the view for name order change (e.g., from "Lennon, John" to "John Lennon" and v.v.)
         *
         * @param event - the click on the menu item
         */
        @Override
        @SuppressWarnings("unchecked")
        public void handle(ActionEvent event) {
            List<String> nameFieldsToChange = new ArrayList<>();
                        HashMap<String, Boolean> prefs = new HashMap<>();
            // Open dialog window with name alternatives
            Dialog dialog = _nameOrderService.createNameTagChooser(
                    param -> prefs.put("ARTIST", param ? true : false),
                    param -> prefs.put("ALBUMARTIST", param  ? true : false),
                    param -> prefs.put("COMPOSER", param  ? true : false),
                    param -> prefs.put("STRAIGHT_NAMEORDER", param  ? true : false));
            // Get response
            //Optional<ButtonType> result = (Optional<ButtonType>) dialog.showAndWait();
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK){
                    final String nameOrder;
                    if (prefs.get("ARTIST") != null)
                        nameFieldsToChange.add(NameTagsToChange.ARTIST.name());
                    if (prefs.get("ALBUMARTIST") != null)
                        nameFieldsToChange.add(NameTagsToChange.ALBUMARTIST.name());
                    if (prefs.get("COMPOSER") != null)
                        nameFieldsToChange.add(NameTagsToChange.COMPOSER.name());
                    // Notice: STRAIGHT_NAMEORDER = null means it's not registered by the listener,
                    // but since it's pre-selected it still counts as true. Tricky yes?
                    if(prefs.get("STRAIGHT_NAMEORDER") == null){
                        System.out.println("STRAIGHT IS SELECTED");
                        nameOrder = NameOrder.GIVENNAMESPACESURNAME.name();
                    }
                    else if(prefs.get("STRAIGHT_NAMEORDER")){
                        System.out.println("STRAIGHT IS SELECTED");
                        nameOrder = NameOrder.GIVENNAMESPACESURNAME.name();
                    }
                    else{
                        System.out.println("REVERSE IS SELECTED");
                        nameOrder = NameOrder.SURNAMECOMMASPACEGIVENNAME.name();
                    }
                    System.out.println("Artists: " + prefs.get("ARTIST") + ", albumArtists: " + prefs.get("ALBUMARTIST") +
                            ", composers: " + prefs.get("COMPOSER") + ", chosen name order: " + nameOrder);
                    if (nameFieldsToChange.size() > 0){
                        _nameOrderService.changeNameOrder(tracks, nameFieldsToChange, nameOrder);
                        if (tracks.get(0).isUpdated())
                            toggleButtonStatus(false);
                    }
                    else {
                        System.out.println("No name tags choosen!");
                    }
                }
                else{
                    System.out.println("Name order change aborted");
                }

            });
            
        }
    };
    private void toggleButtonStatus(boolean disabled){
        System.out.println("Toggling...");
        //saveChangesButton.setDisable(disabled);
    }
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
            System.out.println("Target is Menu");
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