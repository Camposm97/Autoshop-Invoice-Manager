package app;

import controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.database.DB;
import model.ui.FX;

import java.io.IOException;

/**
 * TODO
 * Fool-proof fields when adding a customer and vehicle (maybe)
 * Update offsets for each column in tables: customer, vehicle, work order, parts, and labor
 * Searching vehicles and adding vehicles should have suggestions
 * Display notification confirming the following
 *  - adding a customer
 *  - adding a vehicle
 *  - printing a work order
 */
public class App extends Application {
    private static AppController controller;

    public static AppController get() {
        return controller;
    }
    @Override
    public void init() {
        DB.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxml = FX.load("App.fxml");
        stage = fxml.load();
        controller = fxml.getController();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
