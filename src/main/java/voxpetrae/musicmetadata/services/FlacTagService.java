package voxpetrae.musicmetadata.services;

import voxpetrae.musicmetadata.services.interfaces.TagService;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.audio.flac.FlacTagReader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FlacTagService implements TagService {
    public Tag getTag(File file){
        try {
            FlacTagReader reader = new FlacTagReader();
            FlacTag tag = reader.read(new RandomAccessFile(file, "rw"));
            return tag;
        } catch (CannotReadException ex) {
            System.out.println("CannotReadException in FlacTagService: " + ex);
        } catch (IOException ex) {
            System.out.println("IOException in FlacTagService: " + ex);
        }
        return null;
    }
}