package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.ui.ChangeListenerFactory;

public class PhoneFieldController {
    @FXML
    TextField tfPhone;

    @FXML
    public void initialize() {
        final var REGEX = "[A-Za-z{}\"$&+,:;=?@#|\'<>.\\]\\[^*()%!/_\\\\]";
        tfPhone.textProperty().addListener((o, x, y) -> {
            if (y == null) return;
            if (y.length() == 3) tfPhone.appendText("-");
            if (y.length() == 7) tfPhone.appendText("-");
            if (y.length() > 12) tfPhone.setText(x);
            if (y.matches(REGEX)) tfPhone.setText(x);
            for (int i = 0; i < y.length(); i++) {
                char c = y.charAt(i);
                if (i != 3 && i != 7) {
                    if (c == '-') tfPhone.setText(x);
                }
            }
        });
        tfPhone.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.BACK_SPACE)) tfPhone.clear();
        });
    }
}
