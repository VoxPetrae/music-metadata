package voxpetrae.musicmetadata.views;

import java.util.function.Consumer;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
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

import javax.inject.Inject;
import voxpetrae.musicmetadata.services.interfaces.NameOrderService;
import voxpetrae.musicmetadata.models.AlbumTrack;
import voxpetrae.musicmetadata.services.nameorder.NameOrder;
import voxpetrae.musicmetadata.services.nameorder.NameTagsToChange;

public class ChangeNameOrderView implements voxpetrae.musicmetadata.views.interfaces.NameOrderView {
    private static final String STRAIGHT_NAMEORDER = "STRAIGHT_NAMEORDER";
    private static final String COMPOSER = "COMPOSER";
    private static final String ALBUMARTIST = "ALBUMARTIST";
    private static final String ARTIST = "ARTIST";
    private NameOrderService nameOrderService;

    @Inject
    public ChangeNameOrderView(NameOrderService nameOrderService){
        this.nameOrderService = nameOrderService;
    }

    @SuppressWarnings("unchecked")
    public void selectNameOrder(ObservableList<AlbumTrack> tracks){
        List<String> nameFieldsToChange = new ArrayList<>();
                        HashMap<String, Boolean> prefs = new HashMap<>();
            // Open dialog window with name alternatives
            Dialog dialog = createNameTagChooser(
                    param -> prefs.put(ARTIST, param),
                    param -> prefs.put(ALBUMARTIST, param),
                    param -> prefs.put(COMPOSER, param),
                    param -> prefs.put(STRAIGHT_NAMEORDER, param));
            // Get response
            //Optional<ButtonType> result = (Optional<ButtonType>) dialog.showAndWait();
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK){
                    final String nameOrder;
                    if (prefs.get(ARTIST) != null)
                        nameFieldsToChange.add(NameTagsToChange.ARTIST.name());
                    if (prefs.get(ALBUMARTIST) != null)
                        nameFieldsToChange.add(NameTagsToChange.ALBUMARTIST.name());
                    if (prefs.get(COMPOSER) != null)
                        nameFieldsToChange.add(NameTagsToChange.COMPOSER.name());
                    // Notice: STRAIGHT_NAMEORDER = null means it's not registered by the listener,
                    // but since it's pre-selected it still counts as true. Tricky yes?
                    if(prefs.get(STRAIGHT_NAMEORDER) == null){
                        System.out.println("STRAIGHT IS SELECTED");
                        nameOrder = NameOrder.GIVENNAMESPACESURNAME.name();
                    }
                    else if(Boolean.TRUE.equals(prefs.get(STRAIGHT_NAMEORDER))){
                        System.out.println("STRAIGHT IS SELECTED");
                        nameOrder = NameOrder.GIVENNAMESPACESURNAME.name();
                    }
                    else{
                        System.out.println("REVERSE IS SELECTED");
                        nameOrder = NameOrder.SURNAMECOMMASPACEGIVENNAME.name();
                    }
                    System.out.println("Artists: " + prefs.get(ARTIST) + ", albumArtists: " + prefs.get(ALBUMARTIST) +
                            ", composers: " + prefs.get(COMPOSER) + ", chosen name order: " + nameOrder);
                    if (!nameFieldsToChange.isEmpty()){
                        nameOrderService.changeNameOrder(tracks, nameFieldsToChange, nameOrder);
                        // if (tracks.get(0).isUnsaved()){
                        //     toggleButtonStatus(true);
                        //     alert.setAlertType(AlertType.INFORMATION); 
                        //     alert.setHeaderText(null);
                        //     alert.setTitle(null);
                        //     alert.setContentText("View your changes before saving.");
                        //     alert.show();
                        // }
                    }
                    else {
                        System.out.println("No name tags choosen!");
                    }
                }
                else{
                    System.out.println("Name order change aborted");
                }

            });
    }
    public Dialog<String> createNameTagChooser(Consumer<Boolean> artistsAction, Consumer<Boolean> albumArtistsAction,
            Consumer<Boolean> composersAction, Consumer<Boolean> straightNameOrderAction) {
        // Create dialog
        Dialog<String> dialog = new Dialog<>();
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
        // and has to be treated as null at evaluation time. So null and true mean
        // "Straight",
        // while false means "Reverse".
        radioGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observableValue, old_toggle, new_toggle) -> {
            if (radioGroup.getSelectedToggle() != null
            && radioGroup.getSelectedToggle().getUserData() == "REVERSE") {
        straightNameOrderAction.accept(false);
            } else {
        straightNameOrderAction.accept(true);
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

}