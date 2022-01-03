package controller;

import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import model.*;

import java.util.Optional;
import java.util.ResourceBundle;

public class WorkOrderController {
    int workOrderId;
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfCompany,tfAddress, tfCity, tfState, tfZip;
    @FXML
    TextField tfVin, tfLicensePlate, tfColor, tfYear, tfMake, tfModel, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;
    @FXML
    TextField tfPartsTotal, tfTax, tfDiscount, tfLaborTotal, tfSubtotal, tfTotal;
    @FXML
    TableView<Item> tvParts;
    @FXML
    TableView<Labor> tvLabor;
    @FXML
    TableColumn<Object, String> colLaborId;

    public WorkOrderController() {
        this.workOrderId = -1;

    }

    public WorkOrderController(WorkOrder workOrder) {
        this.workOrderId = workOrder.getId();
    }

    public void save() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String company = tfCompany.getText();
        String street = tfAddress.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String zip = tfZip.getText();
        Address address = new Address(street, city, state, zip);
        Customer customer = new Customer(firstName, lastName, phone, company, address);

        String vin = tfVin.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        int year = Integer.parseInt(tfYear.getText());
        String make = tfMake.getText();
        String model = tfModel.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
        String mileageIn = tfMileageIn.getText();
        String mileageOut = tfMileageOut.getText();
        Vehicle vehicle = new Vehicle(vin, year, make, model, licensePlate, color,
                engine, transmission, mileageIn, mileageOut);

        if (workOrderId != -1) {
            // Update work order
            WorkOrder workOrder = new WorkOrder(customer, vehicle);
            workOrder.setId(this.workOrderId);

        } else {
            // Create new work order
            WorkOrder workOrder = new WorkOrder(customer, vehicle);
            for (Item item : tvParts.getItems()) {
                workOrder.addItem(item);
            }
            for (Labor labor : tvLabor.getItems()) {
                workOrder.addLabor(labor);
            }
            DB.get().addWorkOrder(workOrder);
        }
    }

    public void print() { // TODO
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print Work Order");
        alert.setHeaderText("Ready to print Word Order #");
        alert.getDialogPane().setContent(FX.view("Work_Order_Print.fxml"));
        ButtonType bt1 = new ButtonType("Print", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(bt1, bt2);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e == bt1) {
                PrinterJob printerJob = PrinterJob.createPrinterJob();
                if (printerJob.showPrintDialog(alert.getOwner())) {
                    System.out.println("Print Work Order");
                }
            }
        });
    }

    public void addPart() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Part");
        alert.setHeaderText("Please enter the following information");
        alert.getDialogPane().setContent(FX.view("Work_Order_Add_Part.fxml"));
        ButtonType btSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btSave, btCancel);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e == btSave) {
                System.out.println("Save Part");
            }
        });
    }

    public void deletePart() { // TODO

    }

    public void editPart() { // TODO

    }

    public void addLabor() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Labor");
        alert.setHeaderText("Please enter the following information");
        alert.getDialogPane().setContent(FX.view("Work_Order_Add_Labor.fxml"));
        ButtonType btSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btSave, btCancel);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e == btSave) {
                System.out.println("Save Labor");
            }
        });
    }

    public void deleteLabor() { // TODO

    }

    public void editLabor() { // TODO

    }

    public WorkOrder buildWorkOrder() {
        return null; // TODO
    }
}
