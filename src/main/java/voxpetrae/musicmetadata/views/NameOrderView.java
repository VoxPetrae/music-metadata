package voxpetrae.musicmetadata.views;

import java.util.function.Consumer;
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

public class NameOrderView implements voxpetrae.musicmetadata.views.interfaces.NameOrderView {
    public Dialog<String> createNameTagChooser(Consumer<Boolean> artistsAction, Consumer<Boolean> albumArtistsAction,
            Consumer<Boolean> composersAction, Consumer<Boolean> straightNameOrderAction) {
        // Create dialog
        Dialog<String> dialog = new Dialog<String>();
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
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle old_toggle,
                    Toggle new_toggle) {
                if (radioGroup.getSelectedToggle() != null
                        && radioGroup.getSelectedToggle().getUserData() == "REVERSE") {
                    straightNameOrderAction.accept(false);
                } else {
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

}