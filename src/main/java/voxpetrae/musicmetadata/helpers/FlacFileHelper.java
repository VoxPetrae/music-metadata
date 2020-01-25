package voxpetrae.musicmetadata.helpers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import voxpetrae.musicmetadata.helpers.interfaces.FileHelper;

public class FlacFileHelper implements FileHelper {
    public Boolean isAudioFile(Path filePath){
         return Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith("flac");
    }
}