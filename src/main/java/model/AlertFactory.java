package model;

import controller.*;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

import app.App;

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
        PartWorkspaceController controller = new PartWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Part",
                FX.view("Work_Order_Part_Workspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                System.out.println("Save Part");
                controller.savePart(workOrder);
            }
        });
    }

    public static void showEditPart(WorkOrder workOrder, AutoPart selectedItem) {
        PartWorkspaceController controller = new PartWorkspaceController();
        Parent node = FX.view("Work_Order_Part_Workspace.fxml", controller);
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
        Parent node = FX.view("Work_Order_Labor_Workspace.fxml", controller);
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
        Parent node = FX.view("Work_Order_Labor_Workspace.fxml", controller);
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

    public static void showPrintWorkOrder(WorkOrder workOrder) {
        WorkOrderPrintController controller = new WorkOrderPrintController(workOrder);
        Node node = FX.view("Work_Order_Print.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        builder.setTitle("Print Work Order");
        builder.setHeaderText("Ready to print Work Order #" + workOrder.getId());
        ScrollPane root = new ScrollPane(node);
        root.setFitToWidth(true);
        root.setPrefHeight(400);

        builder.setContent(root);
        builder.setPrintWorkOrderBtns();
        Alert alert = builder.build();
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                PrinterJob printerJob = PrinterJob.createPrinterJob();
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
