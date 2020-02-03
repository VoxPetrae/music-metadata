package voxpetrae.musicmetadata.guice;

import com.google.inject.AbstractModule;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.interfaces.TableBuilderInterface;
import voxpetrae.musicmetadata.album.AlbumTableView;
import voxpetrae.musicmetadata.album.TableBuilder;
import voxpetrae.musicmetadata.helpers.interfaces.IOHelperInterface;
import voxpetrae.musicmetadata.helpers.IOHelper;
import voxpetrae.musicmetadata.services.interfaces.AlbumService; 
import voxpetrae.musicmetadata.services.FlacAlbumService; 
import voxpetrae.musicmetadata.services.interfaces.TagService; 
import voxpetrae.musicmetadata.services.FlacTagService;

public class MusicMetadataModule extends AbstractModule {
    protected void configure() {
        bind(AlbumView.class).to(AlbumTableView.class);
        bind(IOHelperInterface.class).to(IOHelper.class);
        bind(AlbumService.class).to(FlacAlbumService.class);
        bind(TagService.class).to(FlacTagService.class);
        bind(TableBuilderInterface.class).to(TableBuilder.class);
    }
}