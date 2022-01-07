package app;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.DB;
import model.FX;

public class App extends Application {
    private static final String TITLE = "Autoshop Invoice Manager";
    private static BorderPane root;

    public static void setDisplay(Node node) {
        root.setCenter(node);
    }

    public static void clearDisplay() {
        root.setCenter(null);
    }

    @Override
    public void init() {
        DB.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        App.root = (BorderPane) FX.view("App.fxml");
        Scene scene = new Scene(root, 1400, 900);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        Image img = new Image("red_car.png");
        stage.getIcons().add(img);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
