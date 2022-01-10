package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML
    TextField tfWorkOrderId, tfDateCreated;
    @FXML
    DatePicker dateCompletedPicker;

    public WorkOrderWorkspaceController() { // New Work Order
        this.workOrder = new WorkOrder();
//        init();
    }

    public WorkOrderWorkspaceController(WorkOrder workOrder) { // Update Work Order
        this.workOrder = workOrder;
//        init();
    }

    /**
     * Initializes what columns hold what values binds tables to
     * display parts and labor of a work order
     */
    @FXML
    public void initialize() {
//        Platform.runLater(() -> {
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

            tfDateCreated.setText(workOrder.getDateCreated().toLocalDate().format(DateTimeFormatter.ofPattern("MM/DD/YYYY")));
            dateCompletedPicker.setConverter(new StringConverter<>() {
                @Override
                public String toString(LocalDate localDate) {
                    return localDate != null ? localDate.format(DateTimeFormatter.ofPattern("MM/DD/YYYY")) : null;
                }

                @Override
                public LocalDate fromString(String s) {
                    return LocalDate.now();
                }
            });

            if (!workOrder.isNew()) {
                loadCustomer(workOrder.getCustomer());
                loadVehicle(workOrder.getVehicle());
                tfWorkOrderId.setText(String.valueOf(workOrder.getId()));
                if (workOrder.getDateCompleted() != null) {
                    dateCompletedPicker.setValue(workOrder.getDateCompleted().toLocalDate());
                }
            } else {
                tfWorkOrderId.setText(String.valueOf(DB.get().getNextWorkOrderId()));
            }
//        });
    }

    public void save() {
        buildWorkOrder();
        System.out.println(workOrder);
        if (workOrder.isNew()) {
            System.out.println("Add Work Order");
            DB.get().addWorkOrder(workOrder);
        } else {
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
        Customer customer = new Customer(firstName, lastName, phone, email, company, address);
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
        tfEmail.setText(customer.getEmail());
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
        LocalDate dateCompleted = dateCompletedPicker.getValue();
        if (dateCompleted != null) {
            workOrder.setDateCompleted(Date.valueOf(dateCompleted));
        }
    }

    public void print() { // TODO
        AlertFactory.showPrintWorkOrder(workOrder);
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
