package controller;

import app.App;
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
    TableView<AutoPart> tvParts;
    @FXML
    TableColumn<AutoPart, String> colPartNumber, colPartDesc;
    @FXML
    TableColumn<AutoPart, Integer> colPartQuantity;
    @FXML
    TableColumn<AutoPart, Double> colPartRetailPrice, colPartListPrice;
    @FXML
    TableColumn<AutoPart, String> colPartTotal;
    @FXML
    TableView<Labor> tvLabor;
    @FXML
    TableColumn<Labor, String> colLaborCode, colLaborDesc;
    @FXML
    TableColumn<Labor, Double> colLaborBilledHrs, colLaborRate;
    @FXML
    TableColumn<Labor, String> colLaborTotal;
    @FXML
    TextField tfPartsTotal, tfTax, tfDiscount, tfLaborTotal, tfSubtotal, tfTotal; // TODO

    public WorkOrderWorkspaceController() { // New Work Order
        this.workOrder = new WorkOrder();
        init();
    }

    public WorkOrderWorkspaceController(WorkOrder workOrder) { // Update Work Order
        this.workOrder = workOrder;
        init();
    }

    /**
     * Initializes what columns hold what values binds tables to
     * display parts and labor of a work order
     */
    private void init() {
        Platform.runLater(() -> {
            colPartNumber.setCellValueFactory(c -> c.getValue().nameProperty());
            colPartDesc.setCellValueFactory(c -> c.getValue().descProperty());
            colPartQuantity.setCellValueFactory(c -> c.getValue().quantityProperty());
            colPartRetailPrice.setCellValueFactory(c -> c.getValue().retailPriceProperty());
            colPartListPrice.setCellValueFactory(c -> c.getValue().listPriceProperty());
            colPartTotal.setCellValueFactory(c -> c.getValue().billProperty());
            tvParts.setItems(workOrder.itemList());

            colLaborCode.setCellValueFactory(c -> c.getValue().nameProperty());
            colLaborDesc.setCellValueFactory(c -> c.getValue().descProperty());
            colLaborBilledHrs.setCellValueFactory(c -> c.getValue().billedHrsProperty());
            colLaborRate.setCellValueFactory(c -> c.getValue().rateProperty());
            colLaborTotal.setCellValueFactory(c -> c.getValue().billProperty());
            tvLabor.setItems(workOrder.laborList());

            if (!workOrder.isNew()) {
                loadCustomer(workOrder.getCustomer());
                loadVehicle(workOrder.getVehicle());
            }
        });
    }

    public void save() {
        buildWorkOrder();
        System.out.println(workOrder);
        if (workOrder.isNew()) {
            System.out.println("Add Work Order");
            DB.get().addWorkOrder(workOrder);
        } else { // TODO Update work order
            System.out.println("Update Work Order");
            DB.get().updateWorkOrder(workOrder);
            DB.get().deleteProductsMarkedForDeletion();
        }
        App.clearDisplay();
    }

    public void close() {
        DB.get().clearAllProductsMarkedForDeletion();
        App.clearDisplay();
    }

    public Customer buildCustomer() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String company = tfCompany.getText();
        String street = tfAddress.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String zip = tfZip.getText();
        Address address = new Address(street, city, state, zip);
        Customer customer = new Customer(firstName, lastName, phone, company, address);
        return customer;
    }

    public Vehicle buildVehicle() {
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
        return vehicle;
    }

    public void loadCustomer(Customer customer) {
        tfFirstName.setText(customer.getFirstName());
        tfLastName.setText(customer.getLastName());
        tfPhone.setText(customer.getPhone());
        tfCompany.setText(customer.getCompany());
        tfAddress.setText(customer.getAddress().getStreet());
        tfCity.setText(customer.getAddress().getCity());
        tfState.setText(customer.getAddress().getState());
        tfZip.setText(customer.getAddress().getZip());
    }

    public void loadVehicle(Vehicle vehicle) {
        tfVin.setText(vehicle.getVin());
        tfLicensePlate.setText(vehicle.getLicensePlate());
        tfColor.setText(vehicle.getColor());
        tfYear.setText(String.valueOf(vehicle.getYear()));
        tfMake.setText(vehicle.getMake());
        tfModel.setText(vehicle.getModel());
        tfEngine.setText(vehicle.getEngine());
        tfTransmission.setText(vehicle.getTransmission());
        tfMileageIn.setText(vehicle.getMileageIn());
        tfMileageOut.setText(vehicle.getMileageOut());
    }

    public void buildWorkOrder() {
        Customer customer = buildCustomer();
        Vehicle vehicle = buildVehicle();
        workOrder.setCustomer(customer);
        workOrder.setVehicle(vehicle);
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
        workOrder.autoPartIterator().forEachRemaining(e -> {
            System.out.println(e);
        });
    }

    public void editPart() {
        AutoPart selectedItem = tvParts.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            AlertFactory.showEditPart(workOrder, selectedItem);
        }
    }

    public void deletePart() {
        AutoPart autoPart = tvParts.getSelectionModel().getSelectedItem();
        if (autoPart != null) {
            if (!autoPart.isNew()) {
                DB.get().addProductMarkedForDeletion(autoPart);
            }
            workOrder.removeItem(autoPart);
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
            if (!labor.isNew()) {
                DB.get().addProductMarkedForDeletion(labor);
            }
            workOrder.removeLabor(labor);
        }
    }
}
