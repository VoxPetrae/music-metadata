package voxpetrae.musicmetadata.guice;

import com.google.inject.AbstractModule;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.AlbumTableView;
import voxpetrae.musicmetadata.album.interfaces.IOHandlerInterface;
import voxpetrae.musicmetadata.album.IOHandler;
import voxpetrae.musicmetadata.services.interfaces.AlbumService; 
import voxpetrae.musicmetadata.services.FlacAlbumService; 
import voxpetrae.musicmetadata.services.interfaces.TagService; 
import voxpetrae.musicmetadata.services.FlacTagService;
import voxpetrae.musicmetadata.helpers.interfaces.FileHelper;
import voxpetrae.musicmetadata.helpers.FlacFileHelper;

public class MusicMetadataModule extends AbstractModule {
    protected void configure() {
        bind(AlbumView.class).to(AlbumTableView.class);
        bind(IOHandlerInterface.class).to(IOHandler.class);
        bind(AlbumService.class).to(FlacAlbumService.class);
        bind(TagService.class).to(FlacTagService.class);
        bind(FileHelper.class).to(FlacFileHelper.class);
    }
}