package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import model.AlertFactory;
import model.FX;
import java.util.Optional;

public class AppController {
    @FXML
    BorderPane root;

    public void addCustomer() {
        AlertFactory.showAddCustomer();
    }

    public void addVehicle() {
        AlertFactory.showAddVehicle();
    }

    public void addWorkOrder() {
        WorkOrderController controller = new WorkOrderController();
        root.setCenter(FX.view("Work_Order.fxml", controller));
    }

    public void viewCustomers() {
        root.setCenter(FX.view("Customer_Table.fxml"));
    }

    public void viewVehicles() {

    }

    public void viewWorkOrders() {
        root.setCenter(FX.view("Work_Order_Table.fxml"));
    }

    public void exit() {
        Platform.exit();
    }
}
