package voxpetrae.musicmetadata.views;

import java.io.File;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.control.Alert; 
import javafx.scene.control.Alert.AlertType; 
import javax.inject.Inject;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.views.interfaces.AlbumView;
import voxpetrae.musicmetadata.views.interfaces.TableBuilder;
import voxpetrae.musicmetadata.views.interfaces.NameOrderView;
import voxpetrae.musicmetadata.common.interfaces.IOHelper;
import voxpetrae.musicmetadata.common.Props;
import voxpetrae.musicmetadata.services.interfaces.AlbumService;

//@SuppressWarnings("unchecked")
public class AlbumTableView extends Stage implements AlbumView {
    private ObservableList<AlbumTrack> tracks;
    //private Button quitButton;
    private Button saveNameOrderChangesButton;
    private Alert alert;
    private String folderPath;
    @Inject private IOHelper _ioHelper;
    @Inject private AlbumService _genericAlbumService;
    @Inject private TableBuilder<AlbumTrack> _tableBuilder;
    @Inject private NameOrderView _nameOrderView;

    public void initiate() {
        _ioHelper.setFolderPath("Choose folder");
        folderPath = _ioHelper.getFolderPath();
        if (folderPath != null){
            tracks = _genericAlbumService.getAlbumTracks(folderPath);    
        }
        else{
            return;
        }
        drawGui(tracks);
    }
    private void drawGui(final ObservableList<AlbumTrack> tracks) {
        final VBox vBox = new VBox();
        final HBox hBox = new HBox();
        final Scene scene = new Scene(vBox, 1000, 500);
        final var cssPath = getClass().getResource("../css/musicmetadata.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        final MenuBar menuBar = buildMenu();
        final Label imageLabel = buildImageLabel();
        final Label messageLabel = buildMessageTable();
        final var table = _tableBuilder.buildTable(tracks);
        saveNameOrderChangesButton = buildSaveButton();
        // quitButton = buildQuitButton();
        alert = new Alert(AlertType.NONE);
        // vBox.setSpacing(15);
        // vBox.setPadding(new Insets(0, 10, 10, 0));
        hBox.getChildren().addAll(saveNameOrderChangesButton, messageLabel);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(menuBar, imageLabel, table, hBox);

        this.setTitle("Music Album");
        this.setScene(scene);
        this.show();
        this.setOnCloseRequest(a -> {
            this.close();
        });
    }

    private MenuBar buildMenu() {
        final MenuBar menuBar = new MenuBar();
        final Menu fileMenu = new Menu("File");
        final Menu toolsMenu = new Menu("Tools");
        menuBar.getMenus().addAll(fileMenu, toolsMenu);
        final MenuItem quit = new MenuItem("Close album view");
        final MenuItem changeNameOrder = new MenuItem("Change name order");
        quit.setOnAction(exitHandler);
        changeNameOrder.setOnAction(changeNameOrderHandler);
        fileMenu.getItems().add(quit);
        toolsMenu.getItems().add(changeNameOrder);
        fileMenu.getProperties().put(MenuBar.class.getCanonicalName(), menuBar); // Hack to access Node from
                                                                                 // EventHandler
        return menuBar;
    }

    private Label buildImageLabel() {
        final Label label = new Label(_genericAlbumService.getAlbumArtist() + ": " + _genericAlbumService.getAlbumName());
        label.setFont(new Font("Arial", 20));
        label.setPadding(new Insets(10));
        label.getStyleClass().add("tableLable");
        
        final String imagePath = folderPath + File.separator + Props.prop("albumcoverfilename");// "\\Folder.jpg"
        final Image image = new Image(new File(imagePath).toURI().toString());
        final ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        label.setGraphic(imageView);
        return label;
    }

    private Label buildMessageTable() {
        return new Label("Do stuff!");
    }

    private Button buildSaveButton() {
        final Button button = new Button("Save changes");
        // button.getStyleClass().add("marginalized-button");
        button.setOnAction(saveChangesHandler);
        // button.setDisable(true);
        button.setVisible(false);
        return button;
    }

    // private Button buildQuitButton(){
    // Button button = new Button("Quit" + _ioHelper.getFolderPath());
    // //button.getStyleClass().add("marginalized-button");
    // button.setOnAction(exitHandler);
    // //button.setDisable(true);
    // return button;
    // }

    EventHandler<ActionEvent> saveChangesHandler = event -> {
        System.out.println("Saving changes...");
        folderPath = _ioHelper.getFolderPath();
        _genericAlbumService.saveAlbumTracksToFile(tracks, folderPath);
        toggleButtonStatus(true);
    };

    private void toggleButtonStatus(final boolean disabled) {
        System.out.println("Toggling...");
        saveNameOrderChangesButton.setVisible(disabled);
    }

    EventHandler<ActionEvent> exitHandler = event -> {
        System.out.println("Closing Album View...");
        final Stage stage = getStageFromEvent(event);
        try {
            stage.close();
        } catch (final Exception e) {
            System.out.println("Oops! Closing Album View failed because of " + e);
        }
    };

    EventHandler<ActionEvent> changeNameOrderHandler = event -> {
        _nameOrderView.selectNameOrder(tracks);
        // Todo: To check only the first track is insufficient, change to loop.
        if (tracks.get(0).isUnsaved()) {
            toggleButtonStatus(true);
            alert = new Alert(AlertType.NONE);
            alert.setAlertType(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle(null);
            alert.setContentText("View your changes before saving.");
            alert.show();
        }

    };

    /*
     * Helper to access Stage from ActionEvent. The thing is that MenuItem isn't a
     * Node, but MenuBar is, and Button is. So you have to check if the event target
     * is a MenuItem before you get the window to close. Alternatively, you could
     * use different event handlers for exit actions on Button and MenuItem.
     */
    private Stage getStageFromEvent(final ActionEvent event) {
        Node node;
        final var target = event.getTarget();
        if (target instanceof MenuItem) {
            System.out.println("Target is Menu");
            final MenuItem item = (MenuItem) target;
            final Menu menu = item.getParentMenu();
            final MenuBar bar = (MenuBar) menu.getProperties().get(MenuBar.class.getCanonicalName());
            node = (Node) bar;
        } else {
            node = (Node) event.getSource();
        }
        final Stage stage = (Stage) node.getScene().getWindow();
        return stage;
    }
}