package voxpetrae.musicmetadata.services.interfaces;

import java.util.List;
import javafx.collections.ObservableList;
import voxpetrae.musicmetadata.models.AlbumTrack;

public interface NameOrderService {
    void changeNameOrder(ObservableList<AlbumTrack> albumTracks, List<String> nameTagFieldsToChange, String nameOrder);
}