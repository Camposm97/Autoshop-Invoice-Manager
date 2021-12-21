package main.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.model.AutoshopDB;

import java.net.URL;

public class AppWrapper extends Application {
    private static final String TITLE = "Autoshop Invoice Manager";


    @Override
    public void start(Stage stage) throws Exception {
        URL url = AppWrapper.class.getResource("./view/App.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.getIcons().add(new Image("./res/red_car.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
