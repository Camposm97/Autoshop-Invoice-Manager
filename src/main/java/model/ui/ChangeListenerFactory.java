package model.ui;

import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

public class ChangeListenerFactory {
    public void initIntFormat(@NotNull TextField tf) {
        tf.textProperty().addListener((o, x, y) -> {
            if (y == null || y.isEmpty()) {
                return;
            }
            try {
                var val = Integer.parseInt(y);
                if (val < 0.0) throw new NumberFormatException();
                if (!y.matches("\\d")) {
                    tf.setText(x);
                }
            } catch (NumberFormatException e) {
                tf.setText(x);
            }
        });
    }

    public void initCurrencyFormat(TextField tf) {
        tf.textProperty().addListener((o,x,y) -> {
            if (y == null || y.isEmpty()) {
                tf.getStyleClass().add("number_field_invalid");
                return;
            }
            tf.getStyleClass().remove("number_field_invalid");
            try {
                var val = Double.parseDouble(y);
                if (val < 0.0) throw new NumberFormatException();
                if (!y.matches("[0-9]*\\.?[0-9]*")) {
                    tf.setText(x);
                }
            } catch (NumberFormatException e) {
                tf.setText(x);
            }
        });

    }
}
