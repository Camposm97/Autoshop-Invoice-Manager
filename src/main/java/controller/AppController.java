package controller;

import app.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import model.Preferences;
import model.database.DB;
import model.ui.DialogFactory;
import model.ui.FX;
import model.ui.Theme;
import org.controlsfx.control.Notifications;

import java.io.File;

public class AppController {
    @FXML
    BorderPane root;
    @FXML
    MenuBar menuBar;

    public void addCustomer() {
        DialogFactory.showAddCustomer();
    }

    public void addVehicle() {
        DialogFactory.initAddVehicle();
    }

    public void addWorkOrder() {
        App.display(FX.view("WorkOrderWorkspace.fxml"));
    }

    public void exportCustomers() throws Exception {
        DialogFactory f = new DialogFactory();
        File file = f.initExport("Export Customers", "customers");
        if (file != null) {
            DB.get().customers().export(file.getPath());
            Notifications n = Notifications.create().title("Export Customers")
                    .text("Successfully exported customers to " + file.getPath());
            if (Preferences.get().getTheme() == Theme.Dark)
                n = n.darkStyle();
            n.showInformation();
        }
    }

    public void exportVehicles() {

    }

    public void exportAutoPartSuggestions() {

    }

    public void exportWorkOrders() {

    }

    public void viewMyCompany() {
        App.display(FX.view("MyCompany.fxml"));
    }

    public void viewCustomers() {
        App.display(FX.view("CustomerTable.fxml"));
    }

    public void viewWorkOrders() {
        App.display(FX.view("WorkOrderTable.fxml"));
    }

    public void preferences() {
        DialogFactory.initPreferences();
    }

    public void about() {
        DialogFactory.initAbout();
    }

    public void exit() {
        App.getRecentWorkOrders().save();
        Platform.exit();
    }
}
