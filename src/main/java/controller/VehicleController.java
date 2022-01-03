package controller;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

public class VehicleController {
    @FXML
    TextField tfVin, tfYear, tfMake, tfModel, tfLicensePlate,
            tfColor, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;

    public void addVehicle() {
        String vin = tfVin.getText();
        String year = tfYear.getText();
        String make = tfMake.getText();
        String model = tfModel.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
        String mileageIn = tfMileageIn.getText();
        String mileageOut = tfMileageOut.getText();
        String[] arr = {vin, year, make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut};
        for (String s : arr) {
            System.out.println(s);
        }
    }
}
