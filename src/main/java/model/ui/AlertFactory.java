package model.ui;

import app.App;
import controller.*;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import model.*;
import model.work_order.AutoPart;
import model.work_order.Labor;
import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

import java.util.Optional;

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

    public static void showAddPart(WorkOrder workOrder) {
        AutoPartWorkspaceController controller = new AutoPartWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Part",
                FX.view("Auto_Part_Workspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                System.out.println("Save Part");
                controller.savePart(workOrder);
            }
        });
    }

    public static void showEditPart(WorkOrder workOrder, AutoPart selectedItem) {
        AutoPartWorkspaceController controller = new AutoPartWorkspaceController();
        Parent node = FX.view("Auto_Part_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Part", node);
        controller.loadPart(selectedItem);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.updatePart(workOrder, selectedItem);
        });
    }

    public static void showAddLabor(WorkOrder workOrder) {
        LaborWorkspaceController controller = new LaborWorkspaceController();
        Parent node = FX.view("Labor_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Add Labor", node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.saveLabor(workOrder);
            }
        });
    }

    public static void showEditLabor(WorkOrder workOrder, Labor selectedLabor) {
        LaborWorkspaceController controller = new LaborWorkspaceController();
        Parent node = FX.view("Labor_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Labor", node);
        controller.loadLabor(selectedLabor);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.updateLabor(workOrder, selectedLabor);
            }
        });
    }

    public static void showAddPayment(WorkOrder workOrder) {
        PaymentWorkspaceController controller = new PaymentWorkspaceController();
        Parent node = FX.view("Payment_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Add Payment", node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.savePayment(workOrder);
            }
        });
    }

    public static void showEditPayment(WorkOrder workOrder, WorkOrderPayment selectedPayment) {
        PaymentWorkspaceController controller = new PaymentWorkspaceController();
        Parent node = FX.view("Payment_Workspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Payment", node);
        controller.loadPayment(selectedPayment);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.updatePayment(workOrder, selectedPayment);
            }
        });
    }

    public static void showPrintWorkOrder(WorkOrder workOrder, WorkOrderWorkspaceController c) {
        WorkOrderPrintController controller = new WorkOrderPrintController(workOrder);
        Node node = FX.view("Work_Order_Print.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        builder.setTitle("Print Work Order");
        builder.setHeaderText("Ready to print Work Order #" + workOrder.getId());
        ScrollPane root = new ScrollPane(node);
        root.setFitToWidth(true);
        root.setFitToHeight(true);

        builder.setContent(root);
        builder.setPrintWorkOrderBtns();
        Alert alert = builder.build();
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                var set = Printer.getAllPrinters();
                System.out.println(set);
                PrinterJob printerJob = PrinterJob.createPrinterJob();
                if (printerJob == null) {
                    AlertBuilder a = new AlertBuilder();
                    a.setTitle("Error");
                    a.setHeaderText("No Printer Available");
                    a.build().showAndWait();
                    return;
                }
                if (printerJob.showPrintDialog(alert.getOwner())) {
                    WritableImage wi = node.snapshot(null, null);
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
            }
        });
    }

    public static void showPreferences() {
        AlertBuilder builder = new AlertBuilder()
                .setTitle("Preferences")
                .setHeaderText("Repairshop Settings")
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
