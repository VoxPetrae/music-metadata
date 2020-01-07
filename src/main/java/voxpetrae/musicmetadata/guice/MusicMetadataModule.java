package voxpetrae.musicmetadata.guice;

import com.google.inject.AbstractModule;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.AlbumTableView;
import voxpetrae.musicmetadata.album.interfaces.IOHandler;
import voxpetrae.musicmetadata.album.FolderHandler;
import voxpetrae.musicmetadata.services.interfaces.AlbumService; 
import voxpetrae.musicmetadata.services.FlacAlbumService; 

public class MusicMetadataModule extends AbstractModule {
    protected void configure() {
        bind(AlbumView.class).to(AlbumTableView.class);
        bind(IOHandler.class).to(FolderHandler.class);
        bind(AlbumService.class).to(FlacAlbumService.class);
    }
}