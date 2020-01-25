package voxpetrae.musicmetadata.album;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Path;
import voxpetrae.musicmetadata.guice.MusicMetadataModule;
import voxpetrae.musicmetadata.album.interfaces.IOHandlerInterface;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public class IOHandler extends Stage implements IOHandlerInterface {

    private String folderPath;
    
    public void setFolderPath(Boolean mockPath, String title){
        if (mockPath){
            folderPath = "C:\\Users\\PelleAberg\\Music\\Bill Evans\\Explorations";
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
    public File getFileFromFilePath(Path filePath){
        return filePath.toFile();
    }
}