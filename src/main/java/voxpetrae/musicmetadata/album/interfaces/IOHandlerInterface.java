package voxpetrae.musicmetadata.album.interfaces;
import java.io.File;
import java.nio.file.Path;

public interface IOHandlerInterface {
    String getFolderPath();
    void setFolderPath(Boolean mockPath, String title);
    File getFileFromFilePath(Path filePath);
}