package voxpetrae.musicmetadata.services.interfaces;

import org.jaudiotagger.tag.Tag;
import java.io.File;


public interface TagService{
    Tag getTag(File file);
    void updateTag(Tag tag, File file);
}