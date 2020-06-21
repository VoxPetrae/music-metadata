package voxpetrae.musicmetadata.services.nameorder;

import java.util.List;
import javax.inject.Inject;
import javafx.collections.ObservableList;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.services.interfaces.NameOrderModifyer;

// The eternal dilemma: how do you name an interface that's only for DI and has only one implementation? INameOrderService? NameOrderServiceInterface?
// Or should the one implementing class be named NameOrderServiceImpl? Or maybe the solution below is the preferred one? Discuss in small groups!
public class NameOrderService implements voxpetrae.musicmetadata.services.interfaces.NameOrderService {
    @Inject private NameOrderModifyer _nameOrderModifyer;
    public void changeNameOrder(ObservableList<AlbumTrack> albumTracks, List<String> nameTagFieldsToChange, String nameOrder) {
        nameTagFieldsToChange.forEach((String fname) -> {
            // System.out.println("--- " + fname + ", " + albumTracks.size());
        });

        albumTracks.forEach((AlbumTrack track) -> {
            if (nameTagFieldsToChange.contains("ARTIST"))
                track.setArtist(_nameOrderModifyer.setNameOrder(track.getArtist(), nameOrder));

            if (nameTagFieldsToChange.contains("ALBUMARTIST"))
                track.setAlbumArtist(_nameOrderModifyer.setNameOrder(track.getAlbumArtist(), nameOrder));

            if (nameTagFieldsToChange.contains("COMPOSER"))
                track.setComposer(_nameOrderModifyer.setNameOrder(track.getComposer(), nameOrder));

            track.setUpdated(true);
        });
    }
}