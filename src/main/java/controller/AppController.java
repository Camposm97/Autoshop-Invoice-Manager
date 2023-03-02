package controller;

import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.AppModel;
import model.database.DB;
import model.ui.DialogFactory;
import model.ui.FX;
import model.ui.GUIScale;
import model.ui.Theme;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;

public class AppController {
    @FXML
    Stage stage;
    @FXML
    BorderPane root;
    @FXML
    MenuBar menuBar;
    @FXML
    TabPane tabPane;
    @FXML
    Tab tabComp, tabCus, tabWO;
    Parent myCompany, customers, workOrders;
    MyCompanyController compView;
    CustomerTableController cusView;
    WorkOrderTableController woView;

    @FXML
    public void initialize() {
        stage.setTitle(AppModel.TITLE);
        stage.getIcons().add(new Image("icon.png"));
        tabPane.getSelectionModel().selectedItemProperty().addListener((o,previous,current) -> {
            try {
                if (current == tabComp) {
                    viewMyCompany();
                } else if (current == tabCus) {
                    viewCustomers();
                } else if (current == tabWO) {
                    viewWorkOrders();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setDisableMenu(boolean flag) {
        menuBar.setDisable(flag);
    }

    public void display(Parent x) {
        root.setCenter(x);
    }

    public void display() {
        root.setCenter(tabPane);
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

    public void addCustomer() {
        DialogFactory.initAddCustomer();
    }

    public void addVehicle() {
        DialogFactory.initAddVehicle(cusView);
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
            if (AppModel.get().preferences().getTheme() == Theme.Dark)
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
            if (AppModel.get().preferences().getTheme() == Theme.Dark)
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
            if (AppModel.get().preferences().getTheme() == Theme.Dark)
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
            if (AppModel.get().preferences().getTheme() == Theme.Dark)
                n = n.darkStyle();
            n.showInformation();
        }
    }

    public void viewMyCompany() throws IOException {
        if (!tabPane.getTabs().contains(tabComp)) {
            tabPane.getTabs().add(tabComp);
            myCompany = null;
        }
        if (myCompany == null) {
            FXMLLoader fxml = FX.load("MyCompany.fxml");
            myCompany = fxml.load();
            compView = fxml.getController();
            tabComp.setContent(myCompany);
        }
        tabPane.getSelectionModel().select(tabComp);
    }

    public void viewCustomers() throws IOException {
        if (!tabPane.getTabs().contains(tabCus)) {
            tabPane.getTabs().add(tabCus);
            customers = null;
        }
        if (customers == null) {
            FXMLLoader fxml = FX.load("CustomerTable.fxml");
            customers = fxml.load();
            cusView = fxml.getController();
            tabCus.setContent(customers);
        }
        tabPane.getSelectionModel().select(tabCus);
    }

    public void viewWorkOrders() throws IOException {
        if (!tabPane.getTabs().contains(tabWO)) {
            tabPane.getTabs().add(tabWO);
            workOrders = null;
        }
        if (workOrders == null) {
            FXMLLoader fxml = FX.load("WorkOrderTable.fxml");
            workOrders = fxml.load();
            woView = fxml.getController();
            tabWO.setContent(workOrders);
        }
        tabPane.getSelectionModel().select(tabWO);
    }

    public void preferences() {
        DialogFactory.initPreferences();
    }

    public void about() {
        DialogFactory.initAbout();
    }

    public AppModel model() {
        return AppModel.get();
    }

    public void exit() {
        System.out.println("Exiting program...");
        AppModel.get().recentWorkOrders().save();
        Platform.exit();
    }

    public Window getWindow() {
        return root.getScene().getWindow();
    }
}
