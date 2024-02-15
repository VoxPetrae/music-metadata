/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package voxpetrae.musicmetadata;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.google.inject.Guice;
//import com.google.inject.Injector;
import voxpetrae.musicmetadata.views.interfaces.AlbumView;
import voxpetrae.musicmetadata.guice.MusicMetadataModule;
import org.apache.logging.log4j.Logger;

public class MainView extends Application {
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(MainView.class);
    private AlbumView _albumView;

    @Override
    public void start(Stage primaryStage) {
        //Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
        //Application.setUserAgentStylesheet(STYLESHEET_MODENA);
        MusicMetadataModule module = new MusicMetadataModule();
        var injector = Guice.createInjector(module);
        this._albumView = injector.getInstance(AlbumView.class);
        
        final String MENU_BAR_ID = "#menuBar";
        final VBox vBox = new VBox();
        Scene scene = new Scene(vBox, 809, 809);
        MenuBar menuBar = buildMenu();
        menuBar.setId(MENU_BAR_ID);
        vBox.getChildren().addAll(menuBar);
        primaryStage.setTitle("Music Metadata - A minimalistic metadata handler");
        primaryStage.setScene(scene);
        //scene.setFill(Color.BLUEVIOLET); // just playing around
        //primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("#TEST");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Shutdown hook test")));
        launch(args);
    }

    /**
     * Creates the menu
     *
     * @return a MenuBar with eventhandlers set for the menu choices
     */
    private MenuBar buildMenu() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu toolsMenu = new Menu("Tools");
        menuBar.getMenus().addAll(fileMenu, toolsMenu);
        MenuItem openAlbum = new MenuItem("Open album");
        MenuItem quit = new MenuItem("Quit");
        openAlbum.setOnAction(openAlbumView);
        quit.setOnAction(exitHandler);
        fileMenu.getItems().addAll(openAlbum, quit);
        return menuBar;
    }

    /**
     * Sets exit action
     */
    EventHandler<ActionEvent> exitHandler = event -> {
        System.out.println(System.getProperty("java.version"));
        System.out.println("Closing Music Metadata...");
        System.exit(0);
    };
    /**
     * Initiates the album listing GUI
     */
    EventHandler<ActionEvent> openAlbumView = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Open Album...");
            
                _albumView.initiate();
            
        }
    };
    @Override
    public void stop(){
        System.out.println("Window closing...");
    }
}
