package voxpetrae.musicmetadata.views.interfaces;
import javafx.scene.control.Dialog;
import java.util.function.Consumer;

public interface NameOrderView {
    Dialog<String> createNameTagChooser(Consumer<Boolean> artistsAction, Consumer<Boolean> albumArtistsAction,
        Consumer<Boolean> composersAction, Consumer<Boolean> straightNameOrderAction);
}