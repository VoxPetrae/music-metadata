package voxpetrae.musicmetadata.helpers.interfaces;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileHelper {
    Boolean isAudioFile(Path filePath);
}