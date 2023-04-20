package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
import javafx.util.Duration;
import model.Model;
import model.customer.Customer;
import model.database.DB;
import model.ui.DialogFactory;
import model.ui.FX;
import model.ui.GUIScale;
import model.ui.Theme;
import model.work_order.Vehicle;
import model.work_order.WorkOrder;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.function.Function;

public class AppController implements IShortcuts {
    /* Prefix id for work order tabs */
    private static final String TAB_PREFIX_ID = "tab-wo-";
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
    TreeMap<String, WorkOrderWorkspaceController> map;
    @FXML
    public void initialize() {
        map = new TreeMap<>();
        stage.setTitle(Model.TITLE);
        stage.getIcons().add(new Image("icon.png"));
        accels().put(ACCEL_CLOSE, this::closeCurrentTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((o,prev,curr) -> {
            if (curr == null) return;
            try {
                removeShortcuts();
                accels().put(ACCEL_CLOSE, this::closeCurrentTab);
                if (curr == tabComp) {
                    viewMyCompany();
                } else if (curr == tabCus) {
                    viewCustomers();
                } else if (curr == tabWO) {
                    viewWorkOrders();
                }
                WorkOrderWorkspaceController x = map.get(curr.getId());
                if (x != null) x.loadShortcuts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tabPane.getTabs().addListener((ListChangeListener<? super Tab>) change -> {
            System.out.println("tab-ids: " + map.keySet()); /* debug */
        });
        final int D = 3000;
        FadeTransition ft = new FadeTransition(Duration.millis(D), root);
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.play();
    }

    public void removeShortcuts() {
        accels().remove(ACCEL_SAVE);
        accels().remove(ACCEL_PRINT);
        accels().remove(ACCEL_REDO);
        accels().remove(ACCEL_UNDO);
    }

    /**
     * Defines where the new work order menu item to be disabled or not
     * @param flag Decide whether menu item 'New Work Order' is disabled
     */
    public void setDisableMIWO(boolean flag) {
        miWorkOrder.setDisable(flag);
    }

    /**
     * Appends work order workspace as a tab
     * @param workOrder To be shown on work order tab
     */
    public void showWorkOrder(@NotNull WorkOrder workOrder) {
        try {
            FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
            Parent content = loader.load();
            WorkOrderWorkspaceController controller = loader.getController();
            controller.loadWorkOrder(workOrder);
            Tab tab = new Tab("#" + controller.workOrder.getId());
            tab.setId(TAB_PREFIX_ID + controller.workOrder.getId());
            tab.setContent(content);
            tab.setOnClosed(e -> {
                System.out.println("Closing tab");
                map.remove(tab.getId());
                controller.closeTab();
            });
            /* Do we already have this work order open? */
            for (Tab x : tabPane.getTabs()) {
                if (x.getId().equals(tab.getId())) {
                    tabPane.getSelectionModel().select(x);
                    return;
                }
            }
            map.put(tab.getId(), controller);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            /* Add work order id to {currOWOs} */
            Model.get().currOWOs().add(workOrder.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCurrentWorkOrders() {
        DB.get().workOrders().getCurrOWOs().forEach(this::showWorkOrder);
    }

    /**
     * @brief Closes the tab that is currently selected
     */
    public void closeCurrentTab() {
        Tab x = tabPane.getSelectionModel().getSelectedItem();
        if (x == null) return;
        map.remove(x.getId());
        tabPane.getTabs().remove(x);
    }

    /**
     * Changes the scale size of the program
     * @param styleClass Refer to GUIScale.java
     */
    public void setScale(String styleClass) {
        root.getStyleClass().removeAll(GUIScale.styleClasses());
        root.getStyleClass().add(styleClass);
    }

    public void setTheme(Theme theme) {
        switch (theme) {
            case Light -> root.getStylesheets().remove(FX.loadCSS("dark-mode.css"));
            case Dark -> root.getStylesheets().add(FX.loadCSS("dark-mode.css"));
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

    public WorkOrderWorkspaceController addWorkOrder() {
        try {
            FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
            Parent content = loader.load();
            WorkOrderWorkspaceController controller = loader.getController();
            Integer id = DB.get().workOrders().getNextId();
            Tab tab = new Tab("#" + id);
            tab.setId(TAB_PREFIX_ID + id.toString());
            tab.setContent(content);
            tab.setOnClosed(e -> {
                map.remove(tab.getId());
                setDisableMIWO(false);
                controller.closeTab();
            });
            map.put(tab.getId(), controller);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            /* disable ability to create a new work order */
            setDisableMIWO(true);
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public void addWorkOrder(@NotNull Customer c) {
        WorkOrderWorkspaceController controller = addWorkOrder();
        controller.loadCustomer(c);
    }

    @Deprecated
    public void addWorkOrder(@NotNull Customer c, Vehicle v) {
        WorkOrderWorkspaceController controller = addWorkOrder();
        controller.loadCustomer(c);
        controller.loadVehicle(v);
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

    public void exit() {
        Model.get().save();
        DB.get().close();
        Platform.exit();
    }

    public Window window() {
        return root.getScene().getWindow();
    }
}
