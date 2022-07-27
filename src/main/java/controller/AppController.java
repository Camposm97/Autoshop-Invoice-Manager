package controller;

import app.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import model.ui.AlertFactory;
import model.ui.FX;

public class AppController {
    @FXML
    BorderPane root;
    @FXML
    MenuBar menuBar;

    @FXML
    public void initialize() {
        Parent center = FX.view("My_Company.fxml");
        root.setCenter(center);
    }

    /**
     * Adds a new customer ot the database
     */
    public void addCustomer() {
        AlertFactory.showAddCustomer();
    }

    /**
     * Adds a new vehicle for an existing customer
     */
    public void addVehicle() {
        AlertFactory.showAddVehicle();
    }

    public void addWorkOrder() {
        WorkOrderWorkspaceController controller = new WorkOrderWorkspaceController();
        App.setDisplay(FX.view("Work_Order_Workspace.fxml", controller));
    }

    public void viewMyCompany() {
        App.setDisplay(FX.view("My_Company.fxml"));
    }

    public void viewCustomers() {
        App.setDisplay(FX.view("Customer_Table.fxml"));
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
