package voxpetrae.musicmetadata.services;

import voxpetrae.musicmetadata.services.interfaces.TagService;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.audio.flac.FlacTagReader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Embryonic Flac implementation. This should be completed with embedded image handling.
 */
public class FlacTagService<T> implements TagService<FlacTag> {
    public Tag getTag(File file){
        try {
            FlacTagReader reader = new FlacTagReader();
            FlacTag tag = reader.read(new RandomAccessFile(file, "rw"));
            System.out.println("Read flac tag with field count " + tag.getFieldCount());
            return tag;
        } catch (CannotReadException ex) {
            System.out.println("CannotReadException in FlacTagService.getTag: " + ex);
        } catch (IOException ex) {
            System.out.println("IOException in FlacTagService.getTag: " + ex);
        }
        return null;
    }
    public void updateTag(Tag tag, File file){
        System.out.println("updating flac tag...");
        /* try {
            FlacTagWriter writer = new FlacTagWriter();
            writer.write(tag, new RandomAccessFile(file, "rw"), null);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException in FlacTagService.updateTag: " + ex);
        } catch (CannotWriteException ex) {
            System.out.println("CannotWriteException in FlacTagService.updateTag: " + ex);
        } catch (IOException ex) {
            System.out.println("IOException in FlacTagService.updateTag: " + ex);
        } */
    }
}