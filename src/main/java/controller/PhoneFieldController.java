package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class PhoneFieldController {
    @FXML
    TextField tfPhone;

    @FXML
    public void initialize() {
        tfPhone.textProperty().addListener((o, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue.length() == 3) {
                tfPhone.appendText("-");
            }
            if (newValue.length() == 7) {
                tfPhone.appendText("-");
            }
            if (newValue.length() > 12) {
                tfPhone.setText(oldValue);
            }
        });
        tfPhone.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.BACK_SPACE)) {
                tfPhone.clear();
            }
        });
    }
}
