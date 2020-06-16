package voxpetrae.musicmetadata.services;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import voxpetrae.musicmetadata.common.NameOrder;
import voxpetrae.musicmetadata.models.AlbumTrack;

// The eternal dilemma: how do you name an interface that's only for DI and has only one implementation? INameOrderService? NameOrderServiceInterface?
// Or should the one implementing class be named NameOrderServiceImpl? Or maybe the solution below is the preferred one? Discuss in small groups!
public class NameOrderService implements voxpetrae.musicmetadata.services.interfaces.NameOrderService {

    public void changeNameOrder(ObservableList<AlbumTrack> albumTracks, List<String> nameTagFieldsToChange, String nameOrder) {
        nameTagFieldsToChange.forEach((String fname) -> {
            // System.out.println("--- " + fname + ", " + albumTracks.size());
        });

        albumTracks.forEach((AlbumTrack track) -> {
            if (nameTagFieldsToChange.contains("ARTIST"))
                track.setArtist(changeNameOrder(track.getArtist(), nameOrder));

            if (nameTagFieldsToChange.contains("ALBUMARTIST"))
                track.setAlbumArtist(changeNameOrder(track.getAlbumArtist(), nameOrder));

            if (nameTagFieldsToChange.contains("COMPOSER"))
                track.setComposer(changeNameOrder(track.getComposer(), nameOrder));

            track.setUpdated(true);
        });

    }

    private String changeNameOrder(String nameString, String desiredNameOrder) {
        String[] names = nameString.split(";");
        List<String> alteredNames = new ArrayList<>();
        for (String name : names) {
            String trimmedName = name.trim(); // Belt and suspenders
            String alteredName = "";
            if (detectNameOrder(trimmedName) == NameOrder.GIVENNAMESPACESURNAME
                    && desiredNameOrder.equals(NameOrder.SURNAMECOMMASPACEGIVENNAME.name())) {
                alteredName = changeNameOrderToSurnameCommaSpaceGivenName(trimmedName);
            } else if (detectNameOrder(trimmedName) == NameOrder.SURNAMECOMMASPACEGIVENNAME
                    && desiredNameOrder.equals(NameOrder.GIVENNAMESPACESURNAME.name())) {
                alteredName = changeNameOrderToGivenNameSpaceSurname(trimmedName);
            } else {
                alteredName = trimmedName;
            }
            alteredNames.add(alteredName);
        }
        String stringifiedList = convertListToSemicolonSeparatedString((alteredNames));
        return stringifiedList;
    }

    private static NameOrder detectNameOrder(String name) {
        if (name.contains(" ") && !name.contains(",")) {
            return NameOrder.GIVENNAMESPACESURNAME;
        } else if (name.contains(",")) {
            return NameOrder.SURNAMECOMMASPACEGIVENNAME;
        } else {
            return NameOrder.AMBIGUOUS;
        }
    }

    private static String changeNameOrderToGivenNameSpaceSurname(String name) {
        if (detectNameOrder(name) == NameOrder.SURNAMECOMMASPACEGIVENNAME) {
            String[] names = name.split(",");
            if (names.length == 2) {
                String surname = names[0].trim();
                String givenName = names[1].trim();
                return givenName + " " + surname;
            } else {
                System.out.println(name + " has wrong name format, should be '[surname], [given name]' ");
            }
        }
        return name;
    }

    private static String changeNameOrderToSurnameCommaSpaceGivenName(String name) {
        if (detectNameOrder(name) == NameOrder.GIVENNAMESPACESURNAME) {
            String givenName = name.split(" ")[0];
            String restOfName = name.substring(name.indexOf(' ') + 1);
            return restOfName.trim() + ", " + givenName.trim();
        } else {
            System.out.println(name + " has wrong name format, should be '[given name] [surname]' ");
        }
        return name;
    }

    // Todo: Move to utility class to allow DI & unit testing
    private static String convertListToSemicolonSeparatedString(List<String> listToConvert) {
        StringBuilder sb = new StringBuilder();
        listToConvert.forEach((String word) -> {
            sb.append(word);
            if (listToConvert.indexOf(word) < listToConvert.size() - 1)
                sb.append("; ");
        });
        return sb.toString();
    }
}