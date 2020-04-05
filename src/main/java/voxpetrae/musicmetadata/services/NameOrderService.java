package voxpetrae.musicmetadata.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import voxpetrae.musicmetadata.common.NameOrder;
import voxpetrae.musicmetadata.models.AlbumTrack;

// The eternal dilemma: how do you name an interface that's only for DI? INameOrderService? NameOrderServiceInterface?
// Or should the one implementing class be named NameOrderServiceImpl? Or maybe the solution below is the preferred one? I just don't know...
public class NameOrderService implements voxpetrae.musicmetadata.services.interfaces.NameOrderService {

    // Todo: This method returns a GUI component and should not be in the service layer.
    public Dialog<String> createNameTagChooser(Consumer<Boolean> artistsAction, Consumer<Boolean> albumArtistsAction,
        Consumer<Boolean> composersAction, Consumer<Boolean> straightNameOrderAction){
            // Create dialog
        Dialog<String> dialog = new Dialog();
        dialog.setTitle("Name order choices");
        dialog.setHeaderText("Select name tag(s) to change and which name order to change to");
        dialog.setContentText("Name order choices");

        // Set button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create grid for checkboxes
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // Create checkboxes for name fields to change
        CheckBox artists = new CheckBox();
        artists.setText("Artists");
        artists.setOnAction(e -> artistsAction.accept(artists.isSelected()));

        CheckBox albumArtists = new CheckBox();
        albumArtists.setText("Album artists");
        albumArtists.setOnAction(e -> albumArtistsAction.accept(albumArtists.isSelected()));

        CheckBox composers = new CheckBox();
        composers.setText("Composers");
        composers.setOnAction(e -> composersAction.accept(composers.isSelected()));

        // Create radio buttons for name order
        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton straight = new RadioButton();
        straight.setText("Straight name order (as in Bob Dylan)");
        straight.setUserData("STRAIGHT");
        straight.setToggleGroup(radioGroup);
        straight.setSelected(true);

        RadioButton reverse = new RadioButton();
        reverse.setText("Reverse name order (as in Dylan, Bob)");
        reverse.setUserData("REVERSE");
        reverse.setToggleGroup(radioGroup);

        // Caveat: the default value "Straight" will not be registered by this listener,
        // and has to be treated as null at evaluation time. So null and true mean "Straight",
        // while false means "Reverse".
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle old_toggle, Toggle new_toggle) {
                if (radioGroup.getSelectedToggle() != null && radioGroup.getSelectedToggle().getUserData() == "REVERSE"){
                    straightNameOrderAction.accept(false);
                }
                else{
                    straightNameOrderAction.accept(true);
                }
            }
        });

        // Add components to grid pane
        gridPane.add(artists, 0, 0);
        gridPane.add(albumArtists, 0, 1);
        gridPane.add(composers, 0, 2);
        gridPane.add(straight, 1, 0);
        gridPane.add(reverse, 1, 1);
        dialog.getDialogPane().setContent(gridPane);

        return dialog;

    }
    public void changeNameOrder(ObservableList<AlbumTrack> albumTracks, List<String> nameTagFieldsToChange, String nameOrder){
        nameTagFieldsToChange.forEach((String fname) -> {
            System.out.println("--- " + fname + ", " + albumTracks.size());
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
    private String changeNameOrder(String nameString, String desiredNameOrder){
        String[] names = nameString.split(";");
        List<String> alteredNames = new ArrayList<>();
        for(String name : names){
            String trimmedName = name.trim(); // Belt and suspenders
            String alteredName = "";
            if (detectNameOrder(trimmedName) == NameOrder.GIVENNAMESPACESURNAME && desiredNameOrder.equals(NameOrder.SURNAMECOMMASPACEGIVENNAME.name())) {
                alteredName = changeNameOrderToSurnameCommaSpaceGivenName(trimmedName);
            }
            else if(detectNameOrder(trimmedName) == NameOrder.SURNAMECOMMASPACEGIVENNAME && desiredNameOrder.equals(NameOrder.GIVENNAMESPACESURNAME.name())){
                alteredName = changeNameOrderToGivenNameSpaceSurname(trimmedName);
            }
            else{
                alteredName = trimmedName;
            }
            alteredNames.add(alteredName);
        }
        String stringifiedList = convertListToSemicolonSeparatedString((alteredNames));
        return stringifiedList;
    }
    private static NameOrder detectNameOrder(String name){
        if (name.contains(" ") && !name.contains(",")){
            return NameOrder.GIVENNAMESPACESURNAME;
        }
        else if (name.contains(",")){
            return NameOrder.SURNAMECOMMASPACEGIVENNAME;
        }
        else{
            return NameOrder.AMBIGUOUS;
        }
    }
    private static String changeNameOrderToGivenNameSpaceSurname(String name) {
        if (detectNameOrder(name) == NameOrder.SURNAMECOMMASPACEGIVENNAME){
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
    private static String changeNameOrderToSurnameCommaSpaceGivenName(String name){
        if (detectNameOrder(name) == NameOrder.GIVENNAMESPACESURNAME){
            String givenName = name.split(" ")[0];
            String restOfName = name.substring(name.indexOf(' ') + 1);
            return restOfName.trim() + ", " + givenName.trim();
        }
        else{
            System.out.println(name + " has wrong name format, should be '[given name] [surname]' ");
        }
        return name;
    }
    // Todo: Move to utility class to allow DI & unit testing
    private static String convertListToSemicolonSeparatedString(List<String> listToConvert){
        StringBuilder sb = new StringBuilder();
        listToConvert.forEach((String word) -> {
            sb.append(word);
            if (listToConvert.indexOf(word) < listToConvert.size()-1)
                sb.append("; ");
        });
        return sb.toString();
    }
}