package model.ui;

import javafx.scene.control.TextField;
import model.Timer;
import org.jetbrains.annotations.NotNull;

public class ChangeListenerFactory {
    public void setPositiveNums(@NotNull TextField tf) {
        tf.textProperty().addListener((o, x, y) -> {
            if (y == null || y.isEmpty()) return;
            try {
                if (Integer.parseInt(y) < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                tf.setText(x);
            }
        });
    }

    public void setAlphaNums(@NotNull TextField tf) {
        final var REGEX = "[A-Za-z]+";
        tf.textProperty().addListener((o,x,y) -> {
            if (y.isEmpty()) return;
            if (!y.matches(REGEX))
                tf.setText(x);
        });
    }

    public void setTimer(@NotNull TextField tf, Runnable callback) {
        final long DELAY = 300;
        Timer timer = new Timer();
        timer.setCallback(callback);
        tf.textProperty().addListener((o,x,y) -> {
            if (timer.isRunning()) {
                timer.restart();
            } else {
                timer.start(DELAY);
            }
        });
    }

    public void setUpperCase(@NotNull TextField tf) {
        tf.textProperty().addListener((o,x,y) -> tf.setText(y.toUpperCase()));
    }

    public void setVINFormat(@NotNull TextField tf) {
        tf.textProperty().addListener((o,x,y) -> tf.setText(y.length() > 17 ? x : y));
        tf.textProperty().addListener((o,x,y) -> tf.setText(y.replaceAll("\\s+", "")));
        tf.textProperty().addListener((o,x,y) -> tf.setText(y.replaceAll("[qioQIO]", "")));
        setUpperCase(tf);
    }

    public void setCurrencyFormat(TextField tf) {
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
