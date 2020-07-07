package voxpetrae.musicmetadata.services.nameorder;

import java.util.List;
import javax.inject.Inject;
import javafx.collections.ObservableList;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.services.interfaces.NameOrderModifier;

public class NameOrderService implements voxpetrae.musicmetadata.services.interfaces.NameOrderService {
    @Inject private NameOrderModifier _nameOrderModifier;
    public void changeNameOrder(ObservableList<AlbumTrack> albumTracks, List<String> nameTagFieldsToChange, String nameOrder) {
        nameTagFieldsToChange.forEach((String fname) -> {
            // System.out.println("--- " + fname + ", " + albumTracks.size());
        });

        albumTracks.forEach((AlbumTrack track) -> {
            if (nameTagFieldsToChange.contains("ARTIST"))
                track.setArtist(_nameOrderModifier.setNameOrder(track.getArtist(), nameOrder));

            if (nameTagFieldsToChange.contains("ALBUMARTIST"))
                track.setAlbumArtist(_nameOrderModifier.setNameOrder(track.getAlbumArtist(), nameOrder));

            if (nameTagFieldsToChange.contains("COMPOSER"))
                track.setComposer(_nameOrderModifier.setNameOrder(track.getComposer(), nameOrder));

            track.setUnsaved(true);
        });
    }
}