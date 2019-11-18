package voxpetrae.musicmetadata.album;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import voxpetrae.musicmetadata.guice.MusicMetadataModule;
import voxpetrae.musicmetadata.album.interfaces.IOHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public class FolderHandler extends Stage implements IOHandler {

    private String folderPath;
    
    public void setFolderPath(Boolean mockPath, String title){
        if (mockPath){
            folderPath = "C:\\Users\\TheGuy\\Music\\Bill Evans\\Explorations";
        }
        else{
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(title);
            File directory = directoryChooser.showDialog(this);
            if (directory != null){
                folderPath = directory.getAbsolutePath();
            }
        }
    }
    public String getFolderPath(){
        return folderPath;
    }
}