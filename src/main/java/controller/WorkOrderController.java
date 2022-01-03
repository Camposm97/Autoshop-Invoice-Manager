package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Address;
import model.Customer;
import model.Vehicle;
import model.WorkOrder;

import java.util.Optional;

public class WorkOrderController {
    int workOrderId;
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfCompany,tfAddress, tfCity, tfState, tfZip;
    @FXML
    TextField tfVin, tfLicensePlate, tfColor, tfYear, tfMake, tfModel, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;
    @FXML
    TextField tfPartsTotal, tfTax, tfDiscount, tfLaborTotal, tfSubtotal, tfTotal;
    @FXML
    TableView tvParts;
    @FXML
    TableView tvLabor;
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

        }
    }

    public void print() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print Work Order");
        alert.setHeaderText("Ready to print Word Order #");
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> System.out.println(e));
    }

    public void addPart() {

    }

    public void addLabor() {

    }

    public WorkOrder buildWorkOrder() {
        return null; // TODO
    }
}
