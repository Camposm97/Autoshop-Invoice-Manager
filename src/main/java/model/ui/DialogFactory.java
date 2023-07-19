package model.ui;

import app.App;
import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import model.Model;
import model.VehicleDataFetcher;
import model.database.DB;
import model.work_order.*;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DialogFactory {
    private DialogFactory() {}

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
        Alert alert = builder.buildAddDialog(
                "Add Part",
                FX.view("AutoPartWorkspace.fxml", controller));
        alert.getButtonTypes().add(new ButtonType("Save & Add Another", ButtonBar.ButtonData.APPLY));
        alert.showAndWait().ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                controller.savePart(callback);
            } else if (e.getButtonData().equals(ButtonBar.ButtonData.APPLY)) {
                controller.savePart(callback);
                initAddPart(callback);
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
                            notifyInfo("Printing Work Order #" + workOrder.getId(),
                                    workOrder.toFormattedString());
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

    public static void notifyInfo(String title, String text) {
        var n = Notifications.create()
                .title(title).text(text);
        if (Model.get().preferences().getTheme() == Theme.Dark)
            n = n.darkStyle();
        n.showInformation();
    }

    public static void initDeleteWorkOrder(@NotNull TableView<WorkOrder> tv, @NotNull WorkOrder x) {
        AlertBuilder builder = new AlertBuilder();
        builder.setTitle("Delete Work Order #" + x.getId());
        if (Model.get().currOWOs().contains(x.getId())) { /* show error dialog */
            builder.setAlertType(Alert.AlertType.ERROR)
                    .setContentText("Close work order #" + x.getId() + " and try again.")
                    .build().showAndWait();
        } else { /* show confirmation dialog */
            var rs = builder.setAlertType(Alert.AlertType.CONFIRMATION)
                    .setHeaderText("Are you sure you want to delete work order #" + x.getId() + "?")
                    .setContentText(x.toFormattedString())
                    .setConfirmBtns()
                    .build().showAndWait();
            rs.ifPresent(e -> {
                if (e.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                    tv.getItems().remove(x); /* Remove the work order from table view */
                    DB.get().workOrders().delete(x);
                    App.get().log("Deleted Work Order #" + x.getId());
                }
            });
        }
    }

    public static void initPreferences() {
        Alert alert = new AlertBuilder()
        .setTitle("Preferences")
        .setHeaderText(Model.APP_TITLE)
        .setDefaultBtn()
        .addApplyBtn()
        .setContent(FX.view("Preferences.fxml")).build();
        alert.showAndWait().ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                Model.get().preferences().save();
                App.get().log("Updated preferences");
            } else {
                /* apply button */
                Model.get().preferences().save();
                App.get().log("Applied preferences");
                initPreferences();
            }
        });
    }

    public static void initAbout() {
        AlertBuilder builder = new AlertBuilder()
                .setTitle("About")
                .setHeaderText(Model.APP_TITLE)
                .setDefaultBtn()
                .setContent(FX.view("About.fxml"));
        Alert alert = builder.build();
        alert.setGraphic(new ImageView("icon.png"));
        alert.showAndWait();
    }

    public static File initExport(String title, String initialFileName) {
        FileChooser.ExtensionFilter ef1 = new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx");
        FileChooser.ExtensionFilter ef2 = new FileChooser.ExtensionFilter("CSV", "*.csv");
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(initialFileName);
        fc.setTitle(title);
        fc.getExtensionFilters().setAll(ef1, ef2);
        fc.setSelectedExtensionFilter(ef1);
        return fc.showSaveDialog(App.get().window());
    }

    public static void initConfirmExit() {
        FXMLLoader fxmlLoader = FX.load("ConfirmExit.fxml");
        AlertBuilder builder = new AlertBuilder();
        try {
            var rs = builder.setTitle("Exit")
                    .setAlertType(Alert.AlertType.CONFIRMATION)
                    .setHeaderText("Confirm Exit")
                    .setContent(fxmlLoader.load())
                    .setExitConfirmBtns()
                    .build().showAndWait();
            rs.ifPresent(e -> {
                if (e.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                    System.out.println("User clicked exit");
                    App.exit();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initDecodeVIN(String vin, Consumer<Vehicle> callback) {
        if (vin.isBlank()) return;
        try {
            var fetcher = new VehicleDataFetcher(vin);
            var builder = new AlertBuilder();
            builder.setConfirmBtns();
            if (fetcher.isFetchSuccess()) {
                /* successfully decoded vin  */
                Vehicle v = fetcher.get();
                builder.setAlertType(Alert.AlertType.CONFIRMATION)
                        .setTitle("Vehicle Confirmation")
                        .setHeaderText("Vehicle Data Retrieved")
                        .setContentText("The program has successfully retrieved the vehicle data. Please review the details below to confirm if it matches your vehicle.\n\n" + v.toPrettyString())
                        .setConfirmBtns()
                        .build().showAndWait().ifPresent(e -> {
                            if (e.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
                                callback.accept(v);
                        });
            } else {
                /* decoded vin with an error */
                Vehicle v = fetcher.get();
                builder.setAlertType(Alert.AlertType.WARNING)
                        .setTitle("Warning")
                        .setHeaderText("Vehicle data fetched may not be 100% accurate (Error Code(s): " + fetcher.getErrorCodes() + ")")
                        .setContentText(v.toPrettyString() + '\n' + fetcher.getErrorText())
                        .build().showAndWait().ifPresent(e -> {
                            if (e.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
                                callback.accept(v);
                        });
            }
        } catch (IOException e) {
            /* failed to decode vin (io error) */
            AlertBuilder builder = new AlertBuilder();
            builder.setAlertType(Alert.AlertType.ERROR);
            builder.setTitle("Connection Error")
                    .setHeaderText("Failed to Connect to API")
                    .setContentText("Sorry, the program was unable to establish a connection to the API site. Please check your internet connection and try again later.")
                    .build().showAndWait();
        }
    }

    public static void initError(Exception ex) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        var exceptionText = sw.toString();

        var label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        var expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);



        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText("Could not find file blabla.txt!");
        alert.getDialogPane().setExpandableContent(expContent);

        var bt1 = new ButtonType("Report", ButtonBar.ButtonData.OK_DONE);
        var bt2 = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(bt1, bt2);

        alert.showAndWait().ifPresent(e -> {
            if (e.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                try {
                    var localDateTime = LocalDateTime.now();
                    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    var formattedDateTime = localDateTime.format(formatter);
                    var pw1 = new PrintWriter(new FileWriter("./log.txt", true));
                    pw1.println(formattedDateTime);
                    pw1.println(exceptionText);
                    pw1.println();
                    pw1.close();
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        });
    }
}
