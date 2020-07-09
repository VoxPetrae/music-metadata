package voxpetrae.musicmetadata.views.interfaces;
import javafx.scene.control.Dialog;
import javafx.collections.ObservableList;
import java.util.function.Consumer;
import voxpetrae.musicmetadata.models.AlbumTrack;

public interface NameOrderView {
    void selectNameOrder(ObservableList<AlbumTrack> tracks);
    Dialog<String> createNameTagChooser(Consumer<Boolean> artistsAction, Consumer<Boolean> albumArtistsAction,
        Consumer<Boolean> composersAction, Consumer<Boolean> straightNameOrderAction);
}