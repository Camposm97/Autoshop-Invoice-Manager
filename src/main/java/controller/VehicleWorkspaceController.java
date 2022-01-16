package controller;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import model.database.DB;
import model.work_order.Vehicle;
import org.controlsfx.control.textfield.TextFields;

public class VehicleWorkspaceController {
    @FXML
    TextField tfVin, tfYear, tfMake, tfModel, tfLicensePlate,
            tfColor, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;

    @FXML
    public void initialize() {
        TextFields.bindAutoCompletion(tfYear, DB.get().vehicles().getUniqueYear());
        TextFields.bindAutoCompletion(tfMake, DB.get().vehicles().getUniqueMake());
        TextFields.bindAutoCompletion(tfModel, DB.get().vehicles().getUniqueModel());
        TextFields.bindAutoCompletion(tfColor, DB.get().vehicles().getUniqueColor());
        TextFields.bindAutoCompletion(tfEngine, DB.get().vehicles().getUniqueEngine());
        TextFields.bindAutoCompletion(tfTransmission, DB.get().vehicles().getUniqueYear());
    }

    public void addVehicle() {
        String vin = tfVin.getText();
        int year = Integer.parseInt(tfYear.getText());
        String make = tfMake.getText();
        String model = tfModel.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
        String mileageIn = tfMileageIn.getText();
        String mileageOut = tfMileageOut.getText();
        Vehicle vehicle = new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut);
        DB.get().vehicles().add(vehicle);
    }
}
