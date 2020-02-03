package voxpetrae.musicmetadata.textfieldworkaround;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class StringTableCell extends AutoCommitTableCell<Object,String> {

    @Override
    protected String getInputValue() {
        return ((TextField) getInputField()).getText();
    }

    @Override
    protected void setInputValue(String value) {
        ((TextField) getInputField()).setText(value);
    }

    @Override
    protected String getDefaultValue() {
        return "";
    }

    @Override
    protected Node newInputField() {
        return new TextField();
    }

    @Override
    protected String inputValueToText(String newValue) {
        return newValue;
    }
}
