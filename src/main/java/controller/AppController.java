package controller;

import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.AppModel;
import model.Preferences;
import model.database.DB;
import model.ui.DialogFactory;
import model.ui.FX;
import model.ui.GUIScale;
import model.ui.Theme;
import model.work_order.RecentWorkOrders;
import org.controlsfx.control.Notifications;

import java.io.File;

public class AppController {
    AppModel appModel;
    @FXML
    Stage stage;
    @FXML
    BorderPane root;
    @FXML
    MenuBar menuBar;
    Parent myCompany, customers, workOrders;

    @FXML
    public void initialize() {
        appModel = new AppModel();
        stage.setTitle(title());
        stage.getIcons().add(new Image("icon.png"));
    }

    public void setDisableMenu(boolean flag) {
        menuBar.setDisable(flag);
    }

    public void display(Parent x) {
        root.setCenter(x);
    }

    public void setScale(String styleClass) {
        root.getStyleClass().removeAll(GUIScale.styleClasses());
        root.getStyleClass().add(styleClass);
    }

    public void setTheme(Theme theme) {
        switch (theme) {
            case Light:
                root.getStylesheets().remove(FX.loadCSS("dark-mode.css"));
                break;
            case Dark:
                root.getStylesheets().add(FX.loadCSS("dark-mode.css"));
                break;
        }
    }

    public ObservableMap<KeyCombination, Runnable> getAccels() {
        return root.getScene().getAccelerators();
    }

    public RecentWorkOrders getRecentWorkOrders() {
        return appModel.getRecentWorkOrders();
    }

    public void addCustomer() {
        DialogFactory.showAddCustomer();
    }

    public void addVehicle() {
        DialogFactory.initAddVehicle();
    }

    public void addWorkOrder() {
        root.setCenter(FX.view("WorkOrderWorkspace.fxml"));
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

    public void exportVehicles() throws Exception {
        DialogFactory f = new DialogFactory();
        File file = f.initExport("Export Vehicles", "vehicles");
        if (file != null) {
            DB.get().vehicles().export(file.getPath());
            Notifications n = Notifications.create().title("Export Vehicles")
                    .text("Successfully exported vehicles to " + file.getPath());
            if (Preferences.get().getTheme() == Theme.Dark)
                n = n.darkStyle();
            n.showInformation();
        }
    }

    public void exportAutoPartSuggestions() throws Exception {
        DialogFactory f = new DialogFactory();
        File file = f.initExport("Export Auto Part Suggestions", "auto-part-suggestions.xlsx");
        if (file != null) {
            DB.get().autoParts().export(file.getPath());
            Notifications n = Notifications.create().title("Export Auto Parts")
                    .text("Successfully exported auto parts to " + file.getPath());
            if (Preferences.get().getTheme() == Theme.Dark)
                n = n.darkStyle();
            n.showInformation();
        }
    }

    public void exportWorkOrders() throws Exception {
        DialogFactory f = new DialogFactory();
        File file = f.initExport("Export Work Orders", "work-orders.xlsx");
        if (file != null) {
            DB.get().workOrders().export(file.getPath());
            Notifications n = Notifications.create().title("Export Work Orders")
                    .text("Successfully exported work orders to " + file.getPath());
            if (Preferences.get().getTheme() == Theme.Dark)
                n = n.darkStyle();
            n.showInformation();
        }
    }

    public void viewMyCompany() {
        if (myCompany == null) myCompany = FX.view("MyCompany.fxml");
        root.setCenter(myCompany);
    }

    public void viewCustomers() {
        if (customers == null) customers = FX.view("CustomerTable.fxml");
        root.setCenter(customers);
    }

    public void viewWorkOrders() {
        if (workOrders == null) workOrders = FX.view("WorkOrderTable.fxml");
        root.setCenter(workOrders);
    }

    public void preferences() {
        DialogFactory.initPreferences();
    }

    public void about() {
        DialogFactory.initAbout();
    }

    public void exit() {
        appModel.getRecentWorkOrders().save();
        Platform.exit();
    }

    public Window getWindow() {
        return root.getScene().getWindow();
    }

    public String title() {
        return AppModel.TITLE;
    }
}
