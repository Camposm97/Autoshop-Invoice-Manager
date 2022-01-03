package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FX {
    public static Parent view(String src) {
        try {
            URL url = FX.class.getClassLoader().getResource("view/" + src);
            return FXMLLoader.load(url);
        } catch (IOException e) {
            return new Pane();
        }
    }

    public static Parent view(String src, Object controller) {
        try {
            URL url = FX.class.getClassLoader().getResource("view/" + src);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(controller);
            fxmlLoader.setLocation(url);
            return fxmlLoader.load();
        } catch (IOException e) {
            return new Pane();
        }
    }
}
