package voxpetrae.musicmetadata.services.interfaces;

import java.util.function.Consumer;
import java.util.List;
import javafx.scene.control.Dialog;
import javafx.collections.ObservableList;
import voxpetrae.musicmetadata.models.AlbumTrack;

public interface NameOrderService{
    Dialog<String> createNameTagChooser(Consumer<Boolean> artistsAction, Consumer<Boolean> albumArtistsAction,
        Consumer<Boolean> composersAction, Consumer<Boolean> straightNameOrderAction);

    void changeNameOrder(ObservableList<AlbumTrack> albumTracks, List<String> nameTagFieldsToChange, String nameOrder);
}