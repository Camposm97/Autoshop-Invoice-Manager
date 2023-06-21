package model.ui;

import app.App;
import controller.*;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import model.Model;
import model.database.DB;
import model.work_order.AutoPart;
import model.work_order.Labor;
import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class DialogFactory {
    public static void initAddCustomer() {
        CustomerWorkspaceController controller = new CustomerWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Customer",
                FX.view("CustomerWorkspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.addCustomer();
        });
    }

    public static void initAddVehicle() {
        VehicleWorkspaceController controller = new VehicleWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Vehicle",
                FX.view("VehicleWorkspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.addVehicle();
            }
        });
    }

    public static void initAddPart(Function<AutoPart, Void> callback) {
        AutoPartWorkspaceController controller = new AutoPartWorkspaceController();
        AlertBuilder builder = new AlertBuilder();
        Optional<ButtonType> rs = builder.buildAddDialog(
                "Add Part",
                FX.view("AutoPartWorkspace.fxml", controller)).showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.savePart(callback);
            }
        });
    }

    public static void initEditPart(Function<AutoPart, Void> callback, AutoPart selectedItem) {
        AutoPartWorkspaceController controller = new AutoPartWorkspaceController();
        Parent node = FX.view("AutoPartWorkspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Update Part", node);
        controller.loadPart(selectedItem);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.updatePart(callback);
        });
    }

//    public static void initAddLabor(Function<Labor, Void> callback) {
//        LaborWorkspaceController controller = new LaborWorkspaceController();
//        Parent node = FX.view("LaborWorkspace.fxml", controller);
//        AlertBuilder builder = new AlertBuilder();
//        Alert alert = builder.buildAddDialog("Add Labor", node);
//        Optional<ButtonType> rs = alert.showAndWait();
//        rs.ifPresent(e -> {
//            if (e.getButtonData().isDefaultButton()) {
//                controller.saveLabor(callback);
//            }
//        });
//    }

    public static void initAddLabor(Iterator<AutoPart> items, Function<Labor, Void> callback) {
        LaborWorkspaceController controller = new LaborWorkspaceController();
        controller.loadItems(items);
        Parent node = FX.view("LaborWorkspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Add Labor", node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.saveLabor(callback);
            }
        });
    }

    public static void initEditLabor(Iterator<AutoPart> items, Function<Labor, Void> callback, Labor selectedLabor) {
        LaborWorkspaceController controller = new LaborWorkspaceController();
        controller.loadItems(items);
        Parent node = FX.view("LaborWorkspace.fxml", controller);
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

    public static void initAddPayment(Function<WorkOrderPayment, Void> callback) {
        PaymentWorkspaceController controller = new PaymentWorkspaceController();
        Parent node = FX.view("PaymentWorkspace.fxml", controller);
        AlertBuilder builder = new AlertBuilder();
        Alert alert = builder.buildAddDialog("Add Payment", node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.savePayment(callback);
            }
        });
    }

    public static void initEditPayment(Function<WorkOrderPayment, Void> callback, WorkOrderPayment selectedPayment) {
        PaymentWorkspaceController controller = new PaymentWorkspaceController();
        Parent node = FX.view("PaymentWorkspace.fxml", controller);
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

    public static void initPrintWorkOrder(WorkOrder workOrder) {
        final var SCALE = 1.75; /* To scale the work order form to be readable on preview */
        final var PREF_HEIGHT = 640; /* Represents the pref. height of the scroll pane */
        /* Create WorkOrder Form  */
        WorkOrderFormController controller = new WorkOrderFormController(workOrder);
        Parent formPane = FX.view("WorkOrderForm.fxml", controller);
        formPane.getTransforms().add(new Scale(SCALE, SCALE));
        Group group = new Group(formPane);
        ScrollPane scrollPane = new ScrollPane(group);
        /* Hide the scroll bars */
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefViewportHeight(PREF_HEIGHT);
        /* Create aloert */
        Alert alert = new AlertBuilder()
                .setTitle("Print Work Order")
                .setHeaderText("Ready to print Work Order #" + workOrder.getId())
                .setContent(scrollPane)
                .setPrintWorkOrderBtns()
                .build();
        /* Display alert */
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) { /* Print the work order */
                PrinterJob printerJob = PrinterJob.createPrinterJob();
                if (printerJob != null) { /* If there is a printer available */
                    if (printerJob.showPrintDialog(App.get().window())) {
                        WorkOrderFormController tempController = new WorkOrderFormController(workOrder);
                        Parent tempForm = FX.view("WorkOrderForm.fxml", tempController);
                        tempForm.getTransforms().add(new Scale(SCALE, SCALE));
                        tempController.lightMode();
                        Group tempGroup = new Group(tempForm);
                        ScrollPane tempScrollPane = new ScrollPane(tempGroup);
                        Alert tempAlert = new AlertBuilder().setContent(tempScrollPane).build();
                        tempAlert.show();
                        tempAlert.close();
                        WritableImage wi = tempForm.snapshot(null, null);
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
                            var n = Notifications.create()
                                    .title("Printing Work Order #" + workOrder.getId())
                                    .text(workOrder.toFormattedString());
                            if (Model.get().preferences().getTheme() == Theme.Dark) n = n.darkStyle();
                            n.showInformation();
                        }
                    }
                } else { /* Otherwise, display error */
                    AlertBuilder a = new AlertBuilder();
                    a.setTitle("Error");
                    a.setHeaderText("No Printer Available");
                    a.build().showAndWait();
                }

            }
        });
    }

    public static void initDeleteWorkOrder(@NotNull TableView<WorkOrder> tv, @NotNull WorkOrder x) {
        AlertBuilder builder = new AlertBuilder();
        builder.setTitle("Delete Work Order #" + x.getId());
        if (Model.get().currOWOs().contains(x.getId())) { /* show error dialog */
            Alert alert = builder.setAlertType(Alert.AlertType.ERROR)
                    .setContentText("Close work order #" + x.getId() + " and try again.").build();
            alert.showAndWait();
        } else { /* show confirmation dialog */
            builder.setAlertType(Alert.AlertType.CONFIRMATION)
                    .setHeaderText("Are you sure you want to delete work order #" + x.getId() + "?")
                    .setContentText(x.toFormattedString())
                    .setConfirmBtns();
            Alert alert = builder.build();
            Optional<ButtonType> rs = alert.showAndWait();
            rs.ifPresent(e -> {
                if (!e.getButtonData().isCancelButton()) {
                    tv.getItems().remove(x);
                    DB.get().workOrders().delete(x);
                }
            });
        }
    }

    public static void initPreferences() {
        Alert alert = new AlertBuilder()
        .setTitle("Preferences")
        .setHeaderText("Auto-shop Settings")
        .setDefaultBtn()
        .addApplyBtn()
        .setContent(FX.view("Preferences.fxml")).build();
        alert.showAndWait().ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                Model.get().preferences().save();
            } else {
                /* apply button */
                Model.get().preferences().save();
                initPreferences();
            }
        });
    }

    public static void initAbout() {
        AlertBuilder builder = new AlertBuilder()
                .setTitle("About")
                .setHeaderText(Model.TITLE)
                .setDefaultBtn()
                .setContent(FX.view("About.fxml"));
        Alert alert = builder.build();
        alert.setGraphic(new ImageView("icon.png"));
        alert.showAndWait();
    }

    public File initExport(String title, String initialFileName) {
        FileChooser.ExtensionFilter ef1 = new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx");
        FileChooser.ExtensionFilter ef2 = new FileChooser.ExtensionFilter("CSV", "*.csv");
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(initialFileName);
        fc.setTitle(title);
        fc.getExtensionFilters().setAll(ef1, ef2);
        fc.setSelectedExtensionFilter(ef1);
        return fc.showSaveDialog(App.get().window());
    }
}
