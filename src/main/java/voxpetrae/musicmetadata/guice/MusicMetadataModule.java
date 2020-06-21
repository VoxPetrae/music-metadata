package voxpetrae.musicmetadata.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import voxpetrae.musicmetadata.views.interfaces.AlbumView;
import voxpetrae.musicmetadata.views.interfaces.TableBuilder;
import voxpetrae.musicmetadata.views.AlbumTrackTableBuilder;
import voxpetrae.musicmetadata.views.FlacTagTableBuilder;
import voxpetrae.musicmetadata.views.AlbumTableView;
import voxpetrae.musicmetadata.views.interfaces.TagView;
import voxpetrae.musicmetadata.views.FlacTagView;
import voxpetrae.musicmetadata.views.interfaces.NameOrderView;
import voxpetrae.musicmetadata.common.interfaces.IOHelper;
import voxpetrae.musicmetadata.services.interfaces.AlbumService; 
import voxpetrae.musicmetadata.services.FlacAlbumService; 
import voxpetrae.musicmetadata.services.interfaces.TagService; 
import voxpetrae.musicmetadata.services.FlacTagService;
import voxpetrae.musicmetadata.services.interfaces.NameOrderService; 
import voxpetrae.musicmetadata.services.interfaces.NameOrderModifier;
import voxpetrae.musicmetadata.services.interfaces.NameOrderOperations;
import voxpetrae.musicmetadata.models.AlbumTrack;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;

public class MusicMetadataModule extends AbstractModule {
    protected void configure() {
        bind(AlbumView.class).to(AlbumTableView.class);
        bind(TagView.class).to(FlacTagView.class);
        bind(IOHelper.class).to(voxpetrae.musicmetadata.common.IOHelper.class);
        bind(AlbumService.class).to(FlacAlbumService.class);
        bind(TagService.class).to(FlacTagService.class);
        bind(NameOrderService.class).to(voxpetrae.musicmetadata.services.nameorder.NameOrderService.class);
        bind(NameOrderModifier.class).to(voxpetrae.musicmetadata.services.nameorder.NameOrderModifier.class);
        bind(NameOrderOperations.class).to(voxpetrae.musicmetadata.services.nameorder.NameOrderOperations.class);
        bind(NameOrderView.class).to(voxpetrae.musicmetadata.views.NameOrderView.class);
        bind(new TypeLiteral<TableBuilder<AlbumTrack>>(){}).to(new TypeLiteral<AlbumTrackTableBuilder<AlbumTrack>>(){});
        bind(new TypeLiteral<TableBuilder<VorbisCommentTagField>>(){}).to(new TypeLiteral<FlacTagTableBuilder<VorbisCommentTagField>>(){});
    }
}