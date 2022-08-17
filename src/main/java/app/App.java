package app;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.ui.GUIScale;
import model.Preferences;
import model.database.DB;
import model.ui.FX;
import model.ui.Theme;
import model.work_order.RecentWorkOrders;

public class App extends Application {
    public static final String TITLE = "Autoshop Invoice Manager";
    private static BorderPane root;
    private static RecentWorkOrders recentWorkOrders;

    public static void setDisableMenu(boolean flag) {
        root.getTop().setDisable(flag);
    }

    public static void setDisplay(Node node) {
        root.setCenter(node);
    }

    public static Scene getScene() {
        return root.getScene();
    }

    public static void displayMyCompany() {
        App.setDisplay(FX.view("My_Company.fxml"));
    }

    public static void displayCustomers() {
        App.setDisplay(FX.view("Customer_Table.fxml"));
    }

    public static void displayWorkOrders() {
        App.setDisplay(FX.view("Work_Order_Table.fxml"));
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
        App.setDisplay(FX.view("My_Company.fxml"));
        Scene scene = new Scene(root, 1600, 900);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        Image img = new Image("red_car.png");
        stage.getIcons().add(img);
        stage.show();
        stage.setOnCloseRequest(e -> recentWorkOrders.save());
        System.out.println(Preferences.get());
    }

    public static RecentWorkOrders getRecentWorkOrders() {
        if (recentWorkOrders == null) recentWorkOrders = new RecentWorkOrders();
        return recentWorkOrders;
    }

//    public static void loadRecentWorkOrders() {
//        try {
//            File file = new File("recents.dat");
//            if (file.exists()) {
//                FileInputStream fis = new FileInputStream(file);
//                ObjectInputStream ois = new ObjectInputStream(fis);
//                recentWorkOrders = (LinkedList<Integer>)  ois.readObject();
//                ois.close();
//            } else {
//                recentWorkOrders = new LinkedList<>();
//            }
//        } catch (IOException | ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
//    }

//    public static void saveRecentWorkOrders() {
//        try {
//            File file = new File("recents.dat");
//            FileOutputStream fos = new FileOutputStream(file);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(recentWorkOrders);
//            oos.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        launch();
    }
}
