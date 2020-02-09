package voxpetrae.musicmetadata.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import voxpetrae.musicmetadata.common.interfaces.IOHelperInterface;
import voxpetrae.musicmetadata.common.IOHelper;


public class IOHelper extends Stage implements IOHelperInterface {

    private String folderPath;
    
    public void setFolderPath(Boolean mockPath, String title){
        if (mockPath){
            folderPath = "C:\\Users\\MrAberg\\Music\\Bill Evans\\Explorations";
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
    public Boolean isAudioFile(Path filePath){
        return Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith("flac");
   }
}