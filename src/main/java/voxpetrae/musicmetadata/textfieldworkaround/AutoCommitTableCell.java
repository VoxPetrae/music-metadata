package voxpetrae.musicmetadata.textfieldworkaround;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Workaround for JavaFx bug https://bugs.openjdk.java.net/browse/JDK-8089514
 * Thanks to following post (give it an upvote!):
 * https://stackoverflow.com/a/42914845/4117587
 * 
 * @param <S> The generic type of the TableView (for example a
 *            VorbisCommentTagField)
 * @param <T> The type of the cell's item (for example a String)
 */
public abstract class AutoCommitTableCell<S, T> extends TableCell<S, T> {
    private Node field;
    private boolean startEditing;
    private T defaultValue;

    /** @return a newly created input field. */
    protected abstract Node newInputField();

    /** @return the current value of the input field. */
    protected abstract T getInputValue();

    /** Sets given value to the input field. */
    protected abstract void setInputValue(T value);

    /**
     * @return the default in case item is null, must be never null, else cell will
     *         not be editable.
     */
    protected abstract T getDefaultValue();

    /**
     * @return converts the given value to a string, being the cell-renderer
     *         representation.
     */
    protected abstract String inputValueToText(T value);

    @Override
    public void startEdit() {
        try {
            startEditing = true;

            super.startEdit(); // updateItem() will be called

            setInputValue(getItem());
        } finally {
            startEditing = false;
        }
    }

    /**
     * Redirects to commitEdit(). Leaving the cell should commit, just ESCAPE should
     * cancel.
     */
    @Override
    public void cancelEdit() {
        // avoid JavaFX NullPointerException when calling commitEdit()
        getTableView().edit(getIndex(), getTableColumn());
        System.out.println("Committing by leaving cell... " + Thread.currentThread().getStackTrace()[2].getClassName()
                + "/" + Thread.currentThread().getStackTrace()[2].getMethodName());
        commitEdit(getInputValue());
    }

    private void cancelOnEscape() {
        if (defaultValue != null) { // canceling default means writing null
            setItem(defaultValue = null);
            setText(null);
            setInputValue(null);
        }
        super.cancelEdit();
    }

    @Override
    protected void updateItem(T newValue, boolean empty) {
        // String val = newValue == null ? "NULL" : newValue.toString();
        // System.out.println("Updating item... " + val);
        if (startEditing && newValue == null)
            newValue = (defaultValue = getDefaultValue());

        super.updateItem(newValue, empty);

        if (empty || newValue == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(inputValueToText(newValue));
            setGraphic(startEditing || isEditing() ? getInputField() : null);
        }
    }

    protected final Node getInputField() {
        if (field == null) {
            field = newInputField();

            // a cell-editor won't be committed or canceled automatically by JFX
            field.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    System.out.println("Committing by pressing enter... "
                            + Thread.currentThread().getStackTrace()[2].getClassName() + "/"
                            + Thread.currentThread().getStackTrace()[2].getMethodName());
                    commitEdit(getInputValue());
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    cancelOnEscape();
                }

            });

            contentDisplayProperty().bind(Bindings.when(editingProperty()).then(ContentDisplay.GRAPHIC_ONLY)
                    .otherwise(ContentDisplay.TEXT_ONLY));
        }
        return field;
    }
}
