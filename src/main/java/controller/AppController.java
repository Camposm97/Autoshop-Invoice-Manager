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
        App.setDisplay(FX.view("Work_Order_Workspace.fxml"));
    }

    public void viewMyCompany() {
        App.displayMyCompany();
    }

    public void viewCustomers() {
        App.displayCustomers();
    }

    public void viewWorkOrders() {
        App.displayWorkOrders();
    }

    public void preferences() {
        AlertFactory.showPreferences();
    }

    public void about() {
        AlertFactory.showAbout();
    }

    public void exit() {
        App.getRecentWorkOrders().save();
        Platform.exit();
    }
}
