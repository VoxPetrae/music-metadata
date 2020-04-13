package voxpetrae.musicmetadata.guice;

import com.google.inject.AbstractModule;
import voxpetrae.musicmetadata.album.interfaces.AlbumView;
import voxpetrae.musicmetadata.album.interfaces.TableBuilderInterface;
import voxpetrae.musicmetadata.album.AlbumTableView;
import voxpetrae.musicmetadata.album.TableBuilder;
import voxpetrae.musicmetadata.views.interfaces.TagView;
import voxpetrae.musicmetadata.views.FlacTagView;
import voxpetrae.musicmetadata.common.interfaces.IOHelperInterface;
import voxpetrae.musicmetadata.common.IOHelper;
import voxpetrae.musicmetadata.services.interfaces.AlbumService; 
import voxpetrae.musicmetadata.services.FlacAlbumService; 
import voxpetrae.musicmetadata.services.interfaces.TagService; 
import voxpetrae.musicmetadata.services.FlacTagService;
import voxpetrae.musicmetadata.services.interfaces.NameOrderService; 

public class MusicMetadataModule extends AbstractModule {
    protected void configure() {
        bind(AlbumView.class).to(AlbumTableView.class);
        bind(TagView.class).to(FlacTagView.class);
        bind(IOHelperInterface.class).to(IOHelper.class);
        bind(AlbumService.class).to(FlacAlbumService.class);
        bind(TagService.class).to(FlacTagService.class);
        bind(TableBuilderInterface.class).to(TableBuilder.class);
        bind(NameOrderService.class).to(voxpetrae.musicmetadata.services.NameOrderService.class);
    }
}