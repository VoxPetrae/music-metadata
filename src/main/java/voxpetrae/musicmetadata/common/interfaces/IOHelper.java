package voxpetrae.musicmetadata.common.interfaces;
import java.io.File;
import java.nio.file.Path;

public interface IOHelper {
    String getFolderPath();
    void setFolderPath(String title);
    File getFileFromFilePath(Path filePath);
    Boolean isAudioFile(Path filePath);
}