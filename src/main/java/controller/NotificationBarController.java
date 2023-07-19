package controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationBarController {
    @FXML GridPane root;
    @FXML Label lbl;

    public void log(String text) {
        var date = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var formattedDateTime = date.format(formatter);
        lbl.setText('[' + formattedDateTime + "]: " + text);
        translate(); /* animate label */
    }

    public void translate() {
        lbl.setTranslateX(-lbl.getWidth());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), lbl);
        transition.setFromX(-lbl.getWidth());
        transition.setToX(0);
        transition.play();
    }
}
