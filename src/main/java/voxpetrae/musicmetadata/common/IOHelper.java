package voxpetrae.musicmetadata.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class IOHelper extends Stage implements voxpetrae.musicmetadata.common.interfaces.IOHelper {

    private String folderPath;
    
    public void setFolderPath(String title){
        var useTestPathString = (String) Props.prop("usetestpath");
        var useDefaultPathString = (String) Props.prop("usedefaultpath");
        boolean useTestPath = Boolean.valueOf(useTestPathString);
        boolean useDefaultPath = Boolean.valueOf(useDefaultPathString);
        var defaultPath = (String) Props.prop("defaultpath");
        if (useTestPath){
            folderPath = Props.prop("testpath");
        }
        else{
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(title);
            if (useDefaultPath){
                directoryChooser.setInitialDirectory(new File(defaultPath));
            }
            
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
    // The fileSuffix thing could be smoother, ponder alternative solution.
    public Boolean isAudioFile(Path filePath, String fileSuffix){
        return Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith(fileSuffix);
   }
}