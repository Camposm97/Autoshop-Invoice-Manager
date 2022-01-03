package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Address;
import model.Customer;
import model.WorkOrder;

public class WorkOrderController {
    WorkOrder workOrder;
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

    }

    public WorkOrderController(WorkOrder workOrder) {
        this.workOrder = workOrder;
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
        if (workOrder != null) {
            // Update work order
        } else {
            // create new work order

        }
    }

    public void print() {

    }

    public void addPart() {

    }

    public void addLabor() {

    }
}
