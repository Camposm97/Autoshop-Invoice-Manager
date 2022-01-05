package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import model.*;

import java.util.Optional;

public class WorkOrderWorkspaceController {
    WorkOrder workOrder;
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfEmail, tfCompany, tfAddress, tfCity, tfState, tfZip;
    @FXML
    TextField tfVin, tfLicensePlate, tfColor, tfYear, tfMake, tfModel, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;
    @FXML
    TextField tfPartsTotal, tfTax, tfDiscount, tfLaborTotal, tfSubtotal, tfTotal;
    @FXML
    TableView<Item> tvParts;
    @FXML
    TableColumn<Item, String> colPartNumber, colPartDesc;
    @FXML
    TableColumn<Item, Integer> colPartQuantity;
    @FXML
    TableColumn<Item, Double> colPartRetailPrice, colPartListPrice;
    @FXML
    TableView<Labor> tvLabor;
    @FXML
    TableColumn<Labor, String> colLaborCode, colLaborDesc;
    @FXML
    TableColumn<Labor, Double> colLaborRetailPrice, colLaborBilledHrs, colLaborRate;

    public WorkOrderWorkspaceController() {
        this.workOrder = new WorkOrder();
        init();
    }

    public WorkOrderWorkspaceController(WorkOrder workOrder) {
        this.workOrder = workOrder;
        init();
    }

    private void init() {
        Platform.runLater(() -> {
            if (workOrder.isNew()) {
                System.out.println("New Work Order");
            } else {
                System.out.println("Old Work Order");
            }
            colPartNumber.setCellValueFactory(c -> c.getValue().idProperty());
            colPartDesc.setCellValueFactory(c -> c.getValue().descProperty());
            colPartQuantity.setCellValueFactory(c -> c.getValue().quantityProperty());
            colPartRetailPrice.setCellValueFactory(c -> c.getValue().retailPriceProperty());
            colPartListPrice.setCellValueFactory(c -> c.getValue().listPriceProperty());

            colLaborCode.setCellValueFactory(c -> c.getValue().laborCodeProperty());
            colLaborDesc.setCellValueFactory(c -> c.getValue().descProperty());
            colLaborRetailPrice.setCellValueFactory(c -> c.getValue().billProperty());
            colLaborBilledHrs.setCellValueFactory(c -> c.getValue().billedHrsProperty());
            colLaborRate.setCellValueFactory(c -> c.getValue().billedHrsProperty());

            tvParts.setItems(workOrder.itemObservableList());
            tvLabor.setItems(workOrder.laborObservableList());
        });
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

        if (!workOrder.isNew()) {
            // TODO Update work order
            WorkOrder workOrder = new WorkOrder(customer, vehicle);
            workOrder.setId(this.workOrder.getId());

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
//                    if (printerJob.printPage(node)) {
//                        printerJob.endJob();
//                    }
                }
            }
        });
    }

    public void addPart() {
        AlertFactory.showAddPart(workOrder);
        workOrder.itemIterator().forEachRemaining(e -> {
            System.out.println(e);
        });
    }

    public void editPart() {
        Item selectedItem = tvParts.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            AlertFactory.showEditPart(workOrder, selectedItem);
        }
    }

    public void deletePart() {
        Item selectedItem = tvParts.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            workOrder.removeItem(selectedItem);
        }
    }

    public void addLabor() {
        AlertFactory.showAddLabor(workOrder);
    }

    public void editLabor() {
        Labor labor = tvLabor.getSelectionModel().getSelectedItem();
        if (labor != null) {
            AlertFactory.showEditLabor(workOrder, labor);
        }
    }

    public void deleteLabor() {
        Labor labor = tvLabor.getSelectionModel().getSelectedItem();
        if (labor != null) {
            workOrder.removeLabor(labor);
        }
    }

    public WorkOrder buildWorkOrder() {
        return null; // TODO
    }
}
