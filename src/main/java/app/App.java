package app;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.database.DB;
import model.ui.FX;

import java.io.*;
import java.util.LinkedList;

public class App extends Application {
    private static final String TITLE = "Autoshop Invoice Manager";
    private static BorderPane root;
    private static LinkedList<Integer> recentWorkOrders;
    public static String getTitle() {
        return TITLE;
    }

    public static void setDisplay(Node node) {
        root.setCenter(node);
    }

    public static void clearDisplay() {
        root.setCenter(null);
    }

    public static LinkedList<Integer> getRecentWorkOrders() {
        return recentWorkOrders;
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

    @Override
    public void init() {
        DB.init();
        try {
            File file = new File("recents.dat");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                recentWorkOrders = (LinkedList<Integer>)  ois.readObject();
                ois.close();
            } else {
                recentWorkOrders = new LinkedList<>();
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        App.root = (BorderPane) FX.view("App.fxml");
        App.setDisplay(FX.view("My_Company.fxml"));
        Scene scene = new Scene(root, 1160, 768);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        Image img = new Image("red_car.png");
        stage.getIcons().add(img);
        stage.show();
        stage.setOnCloseRequest(e -> {
            try {
                File file = new File("recents.dat");
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(recentWorkOrders);
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
