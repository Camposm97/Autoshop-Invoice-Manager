package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NumberFieldController {
    @FXML
    TextField tfNum;

    @FXML
    public void initialize() {
        tfNum.getStyleClass().add("number_field_invalid");
        tfNum.textProperty().addListener((o, x, y) -> {
            if (y == null || y.isEmpty()) {
                tfNum.getStyleClass().add("number_field_invalid");
                return;
            }
            tfNum.getStyleClass().remove("number_field_invalid");
            try {
                Double.parseDouble(y);
                if (!y.matches("[0-9]*\\.?[0-9]*")) {
                    tfNum.setText(x);
                }
            } catch (NumberFormatException e) {
                tfNum.setText(x);
            }

        });
    }
}
