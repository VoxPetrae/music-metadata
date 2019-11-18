package voxpetrae.musicmetadata.guice;

import com.google.inject.AbstractModule;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.AlbumTableView;
import voxpetrae.musicmetadata.album.interfaces.IOHandler;
import voxpetrae.musicmetadata.album.FolderHandler;

public class MusicMetadataModule extends AbstractModule {
    protected void configure() {
        bind(AlbumView.class).to(AlbumTableView.class);
        bind(IOHandler.class).to(FolderHandler.class);
    }
}