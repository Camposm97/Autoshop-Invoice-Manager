import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.DB;
import model.FX;

public class App extends Application {
    private static final String TITLE = "Autoshop Invoice Manager";

    @Override
    public void init() {
        DB.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FX.view("App.fxml");
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
