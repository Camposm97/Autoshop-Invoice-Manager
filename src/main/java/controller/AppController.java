package controller;

import app.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import model.ui.AlertFactory;
import model.ui.FX;

public class AppController {
    @FXML
    BorderPane root;
    @FXML
    MenuBar menuBar;

    public void addCustomer() {
        AlertFactory.showAddCustomer();
    }

    public void addVehicle() {
        AlertFactory.showAddVehicle();
    }

    public void addWorkOrder() {
        WorkOrderWorkspaceController controller = new WorkOrderWorkspaceController();
        App.setDisplay(FX.view("Work_Order_Workspace.fxml", controller));
    }

    public void viewCustomers() {
        App.setDisplay(FX.view("Customer_Table.fxml"));
    }

    public void viewVehicles() {

    }

    public void viewWorkOrders() {
        App.setDisplay(FX.view("Work_Order_Table.fxml"));
    }

    public void preferences() {
        AlertFactory.showPreferences();
    }

    public void about() {
        AlertFactory.showAbout();
    }

    public void exit() {
        Platform.exit();
    }
}
