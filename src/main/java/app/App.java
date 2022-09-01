package app;

import controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Preferences;
import model.database.DB;
import model.ui.FX;

import java.io.IOException;

/**
 * TODO
 * Add customer ids to work orders
 * Update layout view for MyCompany,Customers,and Work Orders to be in a Tab Pane
 * Add listeners for Add Customer and Vehicle dialog for updating Customer Table
 * Move to MySQL? Supports Multi-threading and Scalability
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
        Preferences.init();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public class AppWrapper {
        public static void main(String[] args) {
            App.main(args);
        }
    }
}
