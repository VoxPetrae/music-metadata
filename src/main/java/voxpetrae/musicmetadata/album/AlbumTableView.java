package voxpetrae.musicmetadata.album;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.interfaces.IOHandler;
import voxpetrae.musicmetadata.album.FolderHandler;
import voxpetrae.musicmetadata.guice.MusicMetadataModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import javax.inject.Inject;


public class AlbumTableView extends Stage implements AlbumView {
    private Button quitButton;
    private IOHandler _ioHandler;

    @Inject
    public AlbumTableView(IOHandler ioHandler){
        _ioHandler = ioHandler;
    }
    
    public void initiate(){
        _ioHandler.setFolderPath(true, "Choose folder");
        drawGui();
    }
    private void drawGui(){
        final VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 1000, 500);
        MenuBar menuBar = buildMenu();
        quitButton = buildQuitButton();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(0, 10, 10, 0));
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, quitButton);
        this.setTitle("Testing");
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
    private Button buildQuitButton(){
        Button button = new Button("Quit" + _ioHandler.getFolderPath());
        //button.getStyleClass().add("marginalized-button");
        button.setOnAction(exitHandler);
        //button.setDisable(true);
        return button;
    }
    public String testMessage(String testString){
        return testString + ", and then some!";
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
    Or, you could find the proper way to deal with this. I'm certain there is one :).
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