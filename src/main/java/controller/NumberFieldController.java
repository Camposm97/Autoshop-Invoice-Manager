package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NumberFieldController {
    @FXML
    TextField tfNum;

    @FXML
    public void initialize() {
        tfNum.textProperty().addListener((o, x, y) -> {
            if (y == null || y.isEmpty()) return;
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
