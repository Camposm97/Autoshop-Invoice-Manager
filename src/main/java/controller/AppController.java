package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import model.AlertFactory;
import model.FX;

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
        WorkOrderWorkspaceController controller = new WorkOrderWorkspaceController();
        root.setCenter(FX.view("Work_Order_Workspace.fxml", controller));
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
