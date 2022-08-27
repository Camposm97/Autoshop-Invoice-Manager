package app;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Preferences;
import model.database.DB;
import model.ui.FX;
import model.ui.GUIScale;
import model.ui.Theme;
import model.work_order.RecentWorkOrders;

public class App extends Application {
    public static final String TITLE = "Autoshop Invoice Manager";
    private static BorderPane root;
    private static RecentWorkOrders recentWorkOrders;

    public static RecentWorkOrders getRecentWorkOrders() {
        if (recentWorkOrders == null) recentWorkOrders = new RecentWorkOrders();
        return recentWorkOrders;
    }
    public static Scene getScene() {
        return root.getScene();
    }

    public static void setDisableMenu(boolean flag) {
        root.getTop().setDisable(flag);
    }

    public static void display(Node node) {
        root.setCenter(node);
    }

    public static void setScale(String styleClass) {
        root.getStyleClass().removeAll(GUIScale.styleClasses());
        root.getStyleClass().add(styleClass);
    }

    public static void setTheme(Theme theme) {
        switch (theme) {
            case Light:
                root.getStylesheets().remove(FX.loadCSS("dark-mode.css"));
                break;
            case Dark:
                root.getStylesheets().add(FX.loadCSS("dark-mode.css"));
                break;
        }
    }

    @Override
    public void init() {
        DB.init();
    }

    @Override
    public void start(Stage stage) {
        App.root = (BorderPane) FX.view("App.fxml");
        App.display(FX.view("MyCompany.fxml"));
        Preferences.init();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
        stage.setOnCloseRequest(e -> recentWorkOrders.save());
    }

    public static void main(String[] args) {
        launch();
    }
}
