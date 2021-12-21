package main.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AppController {
    public void addCustomer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ButtonType bt = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(bt);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {

        });
    }

    public void addVehicle() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    public void addWorkOrder() {

    }

    public void exit() {
        Platform.exit();
    }
}
