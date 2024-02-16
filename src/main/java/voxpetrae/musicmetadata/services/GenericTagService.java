package voxpetrae.musicmetadata.services;

import voxpetrae.musicmetadata.services.interfaces.TagService;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

// Observe: This class has nothing to do with org.jaudiotagger.audio.generic.GenericTag.
// Maybe I should rename it to CommonTagService or the like...
public class GenericTagService<T> implements TagService<Tag> {
    public Tag getTag(File file){
        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            var audioHeader = f.getAudioHeader();
            //System.out.println("Generic FORMAT: " + audioHeader.getFormat() + ", " + tag.getFieldCount());
            return tag;
        } catch (TagException ex) {
            System.out.println("TagException in GenericTagService.getTag: " + ex);    
        } catch (InvalidAudioFrameException ex) {
            System.out.println("InvalidAudioFrameException in GenericTagService.getTag: " + ex);    
        } catch (ReadOnlyFileException ex) {
            System.out.println("ReadOnlyFileException in GenericTagService.getTag: " + ex);    
        } catch (CannotReadException ex) {
            System.out.println("CannotReadException in GenericTagService.getTag: " + ex);
        } catch (IOException ex) {
            System.out.println("IOException in GenericTagService.getTag: " + ex);
        }
        return null;
    }
    public void updateTag(Tag tag, File file){
        System.out.println("updating...");
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