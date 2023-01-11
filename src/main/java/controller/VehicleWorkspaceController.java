package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.AppModel;
import model.customer.Customer;
import model.customer.OwnedVehicle;
import model.database.DB;
import model.ui.ChangeListenerFactory;
import model.ui.FX;
import model.ui.Theme;
import model.work_order.Vehicle;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class VehicleWorkspaceController {
    private Customer customer;
    @FXML
    TextField tfCustomer;
    @FXML
    Button btCus;
    @FXML
    TextField tfVin, tfYear, tfMake, tfModel, tfLicensePlate, tfColor, tfEngine, tfTransmission;
    @FXML
    PopOver customerPopOver;

    @FXML
    public void initialize() {
        ChangeListenerFactory f = new ChangeListenerFactory();
        f.setVINFormat(tfVin);
        f.setPositiveNums(tfYear);
        f.setUpperCase(tfLicensePlate);
        // TODO maybe make transmission combo box?
        TextFields.bindAutoCompletion(tfYear, DB.get().vehicles().getUniqueYear());
        TextFields.bindAutoCompletion(tfMake, DB.get().vehicles().getUniqueMake());
        TextFields.bindAutoCompletion(tfModel, DB.get().vehicles().getUniqueModel());
        TextFields.bindAutoCompletion(tfColor, DB.get().vehicles().getUniqueColor());
        TextFields.bindAutoCompletion(tfEngine, DB.get().vehicles().getUniqueEngine());
        TextFields.bindAutoCompletion(tfTransmission, DB.get().vehicles().getUniqueTransmission());
        try {
            FXMLLoader fxml = FX.load(("CustomerTable.fxml"));
            this.customerPopOver = new PopOver(fxml.load());
            customerPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            CustomerTableController customerTableController = fxml.getController();
            customerTableController.connect(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomer() {
        customerPopOver.show(btCus);
    }

    public void addVehicle() {
        String vin = tfVin.getText();
        String year = tfYear.getText();
        String make = tfMake.getText();
        String model = tfModel.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
        Vehicle vehicle = new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission);
        int customerId = this.customer.getId();
        OwnedVehicle ownedVehicle = new OwnedVehicle(customerId, vehicle);
        var success = DB.get().vehicles().add(ownedVehicle);
        var n = Notifications.create();
        if (AppModel.get().preferences().getTheme() == Theme.Dark) n = n.darkStyle();
        if (success) {
            n.title("Created Vehicle").text(vehicle.toString());
        } else {
            n.title("Failed to Create Vehicle").text("Cannot write to database");
        }
        n.showInformation();
    }

    public void loadCustomer(Customer customer) {
        this.customer = customer;
        tfCustomer.setText(customer.toPrettyString());
    }
}
