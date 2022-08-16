package model.ui;

import app.App;
import controller.*;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import model.Preferences;
import model.work_order.AutoPart;
import model.work_order.Labor;
import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

import java.util.Optional;
import java.util.function.Function;

public class AlertFactory {
    public static void showAddCustomer() {
        CustomerWorkspaceController controller = new CustomerWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Customer",
                FX.view("Customer_Workspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.addCustomer();
        });
    }

    public static void showAddVehicle() {
        VehicleWorkspaceController controller = new VehicleWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Vehicle",
                FX.view("Vehicle_Workspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.addVehicle();
        });
    }

    public static void showAddPart(Function<AutoPart, Void> callback) {
        AutoPartWorkspaceController controller = new AutoPartWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Part",
                FX.view("Auto_Part_Workspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.savePart(callback);
            }
        });
    }

    public static void showEditPart(Function<AutoPart, Void> callback, AutoPart selectedItem) {
        AutoPartWorkspaceController controller = new AutoPartWorkspaceController();
        Parent node = FX.view("Auto_Part_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Part", node);
        controller.loadPart(selectedItem);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.updatePart(callback);
        });
    }

    public static void showAddLabor(Function<Labor, Void> callback) {
        LaborWorkspaceController controller = new LaborWorkspaceController();
        Parent node = FX.view("Labor_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Add Labor", node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.saveLabor(callback);
            }
        });
    }

    public static void showEditLabor(Function<Labor, Void> callback, Labor selectedLabor) {
        LaborWorkspaceController controller = new LaborWorkspaceController();
        Parent node = FX.view("Labor_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Labor", node);
        controller.loadLabor(selectedLabor);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.updateLabor(callback);
            }
        });
    }

    public static void showAddPayment(Function<WorkOrderPayment, Void> callback) {
        PaymentWorkspaceController controller = new PaymentWorkspaceController();
        Parent node = FX.view("Payment_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Add Payment", node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.savePayment(callback);
            }
        });
    }

    public static void showEditPayment(Function<WorkOrderPayment, Void> callback, WorkOrderPayment selectedPayment) {
        PaymentWorkspaceController controller = new PaymentWorkspaceController();
        Parent node = FX.view("Payment_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Payment", node);
        controller.loadPayment(selectedPayment);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.updatePayment(callback);
            }
        });
    }

    public static void showPrintWorkOrder(WorkOrder workOrder, WorkOrderWorkspaceController c) {
        final var SCALE = 1.5;
        WorkOrderFormController controller = new WorkOrderFormController(workOrder);
        Parent formPane = FX.view("Work_Order_Form.fxml", controller);
        formPane.getTransforms().add(new Scale(SCALE, SCALE));
        Group group = new Group(formPane);
        ScrollPane scrollPane = new ScrollPane(group);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefViewportHeight(640);
        Alert alert = new AlertBuilder()
                .setTitle("Print Work Order")
                .setHeaderText("Ready to print Work Order #" + workOrder.getId())
                .setContent(scrollPane)
                .setPrintWorkOrderBtns()
                .build();
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                PrinterJob printerJob = PrinterJob.createPrinterJob();
                if (printerJob != null) {
                    if (printerJob.showPrintDialog(App.getScene().getWindow())) {
                        WorkOrderFormController tempController = new WorkOrderFormController(workOrder);
                        Parent tempForm = FX.view("Work_Order_Form.fxml", tempController);
                        tempForm.getTransforms().add(new Scale(SCALE, SCALE));
                        tempController.lightMode();
                        Group tempGroup = new Group(tempForm);
                        ScrollPane tempScrollPane = new ScrollPane(tempGroup);
                        Alert tempAlert = new AlertBuilder().setContent(tempScrollPane).build();
                        tempAlert.show();
                        tempAlert.close();
                        WritableImage wi = tempForm.snapshot(null,null);
                        ImageView iv = new ImageView(wi);
                        PageLayout pageLayout = printerJob.getPrinter().createPageLayout(Paper.NA_LETTER,
                                PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
                        printerJob.getJobSettings().setPageLayout(pageLayout);
                        iv.setFitWidth(pageLayout.getPrintableWidth());
                        iv.setFitHeight(pageLayout.getPrintableHeight());
                        if (printerJob.printPage(iv)) {
                            System.out.println(Printer.MarginType.HARDWARE_MINIMUM);
                            System.out.println(printerJob.getJobSettings().getPageLayout().toString());
                            printerJob.endJob();
                            c.save();
                        }
                    }
                } else {
                    AlertBuilder a = new AlertBuilder();
                    a.setTitle("Error");
                    a.setHeaderText("No Printer Available");
                    a.build().showAndWait();
                }

            }
        });
    }

    public static void showPreferences() {
        AlertBuilder builder = new AlertBuilder()
                .setTitle("Preferences")
                .setHeaderText("Repair-shop Settings")
                .setDefaultBtn()
                .setContent(FX.view("Preferences.fxml"));
        builder.build().showAndWait().ifPresent(e -> Preferences.get().save());
    }

    public static void showAbout() {
        AlertBuilder builder = new AlertBuilder()
                .setTitle("About")
                .setHeaderText(App.getTitle())
                .setDefaultBtn()
                .setContent(FX.view("About.fxml"));
        Alert alert = builder.build();
        alert.setGraphic(new ImageView("red_car.png"));
        alert.showAndWait();
    }
}
