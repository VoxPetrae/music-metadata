package voxpetrae.musicmetadata.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class IOHelper extends Stage implements voxpetrae.musicmetadata.common.interfaces.IOHelper {

    private String folderPath;
    
    public void setFolderPath(String title){
        var useMockPathString = (String) Props.prop("usemockpath");
        boolean useMockPath = Boolean.valueOf(useMockPathString);
        if (useMockPath){
            folderPath = Props.prop("mockpath");
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