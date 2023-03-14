package controller;

import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Model;
import model.database.DB;
import model.ui.DialogFactory;
import model.ui.FX;
import model.ui.GUIScale;
import model.ui.Theme;
import model.work_order.WorkOrder;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class AppController {
    @FXML
    Stage stage;
    @FXML
    BorderPane root;
    @FXML
    MenuItem miWorkOrder;
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
        stage.setTitle(Model.TITLE);
        stage.getIcons().add(new Image("icon.png"));
        tabPane.getSelectionModel().selectedItemProperty().addListener((o,prev,curr) -> {
            try {
                if (curr == tabComp) {
                    viewMyCompany();
                } else if (curr == tabCus) {
                    viewCustomers();
                } else if (curr == tabWO) {
                    viewWorkOrders();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Defines where the new work order menu item to be disabled or not
     * @param flag
     */
    public void setDisableMIWO(boolean flag) {
        miWorkOrder.setDisable(flag);
    }

    /**
     * Replaces center pane of the BorderPane {root}
     */
    public void closeCurrentTab(Parent x) {
        root.setCenter(x);
    }

    /**
     * Appends work order workspace as a tab
     * @param workOrder
     */
    public void showWorkOrder(@NotNull WorkOrder workOrder) {
        try {
            FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
            Parent content = loader.load();
            WorkOrderWorkspaceController controller = loader.getController();
            controller.loadWorkOrder(workOrder);
            Tab tab = new Tab("#" + controller.workOrder.getId());
            tab.setOnClosed(e -> controller.closeTab());
            tab.setContent(content);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            /* Add work order id to {currOWOs} */
            Model.get().currOWOs().add(workOrder.getId());
//            App.get().show(content); /* old way of doing things */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Closes the tab that is currently selected
     */
    public void closeCurrentTab() {
        System.out.println("Closing current tab");
        tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
    }

    /**
     * Changes the scale size of the program
     * @param styleClass
     */
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

    public ObservableMap<KeyCombination, Runnable> accels() {
        return root.getScene().getAccelerators();
    }

    public void addCustomer() {
        DialogFactory.initAddCustomer();
    }

    public void addVehicle() {
        DialogFactory.initAddVehicle();
    }

    public void addWorkOrder() {
        try {
            FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
            Parent content = loader.load();
            WorkOrderWorkspaceController controller = loader.getController();
            Tab tab = new Tab("#" + DB.get().workOrders().getNextId());
            tab.setOnClosed(e -> {
                setDisableMIWO(false);
                controller.closeTab();
            });
            tab.setContent(content);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            /* disable ability to create a new work order */
            setDisableMIWO(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportCustomers() throws Exception {
        DialogFactory f = new DialogFactory();
        File file = f.initExport("Export Customers", "customers");
        if (file != null) {
            DB.get().customers().export(file.getPath());
            Notifications n = Notifications.create().title("Export Customers")
                    .text("Successfully exported customers to " + file.getPath());
            if (Model.get().preferences().getTheme() == Theme.Dark)
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
            if (Model.get().preferences().getTheme() == Theme.Dark)
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
            if (Model.get().preferences().getTheme() == Theme.Dark)
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
            if (Model.get().preferences().getTheme() == Theme.Dark)
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

    public Model model() {
        return Model.get();
    }

    public void exit() {
        Model.get().save();
        DB.get().close();
        Platform.exit();
    }

    public Window window() {
        return root.getScene().getWindow();
    }
}
