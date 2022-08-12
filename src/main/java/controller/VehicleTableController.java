package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import model.database.DB;
import model.ui.FX;
import model.work_order.Vehicle;
import org.controlsfx.control.tableview2.TableView2;

public class VehicleTableController {
    protected int chosenCustomerId;
    @FXML
    TextField tfVin, tfLicensePlate, tfColor, tfYear, tfMake, tfModel, tfEngine, tfTransmission;
    @FXML
    TableView2<Vehicle> tv;
    @FXML
    TableColumn<Vehicle, String> colVin, colLicensePlate, colColor;
    @FXML
    TableColumn<Vehicle, String> colYear;
    @FXML
    TableColumn<Vehicle, String> colMake, colModel, colEngine, colTransmission;

    @FXML
    public void initialize() {
        tfVin.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfLicensePlate.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfColor.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfYear.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfMake.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfModel.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfEngine.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
        tfTransmission.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
//        tfMileageIn.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));
//        tfMileageOut.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filterWithCustomerId(buildVehicle(), chosenCustomerId)));

        colVin.setCellValueFactory(c -> c.getValue().vinProperty());
        colLicensePlate.setCellValueFactory(c -> c.getValue().licensePlateProperty());
        colColor.setCellValueFactory(c -> c.getValue().colorProperty());
        colYear.setCellValueFactory(c -> c.getValue().yearProperty());
        colMake.setCellValueFactory(c -> c.getValue().makeProperty());
        colModel.setCellValueFactory(c -> c.getValue().modelProperty());
        colEngine.setCellValueFactory(c -> c.getValue().engineProperty());
        colTransmission.setCellValueFactory(c -> c.getValue().transmissionProperty());
//        colMileageIn.setCellValueFactory(c -> c.getValue().mileageInProperty());
//        colMileageOut.setCellValueFactory(c -> c.getValue().mileageOutProperty());
    }

    public void refresh(int customerId) {
        this.chosenCustomerId = customerId;
        tv.getItems().setAll(DB.get().vehicles().getAllByCustomerId(customerId));
        FX.autoResizeColumns(tv,50);
    }

    public void connect(WorkOrderWorkspaceController controller) {
        tv.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 1) {
                Vehicle vehicle = getSelectedVehicle();
                if (vehicle != null) {
                    controller.loadVehicle(vehicle);
                    controller.vehiclePopOver.hide();
                }
            }
        });
    }

    public Vehicle buildVehicle() {
        String vin = tfVin.getText();
        String year = tfYear.getText();
        String make = tfMake.getText();
        String model = tfModel.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
//        String mileageIn = tfMileageIn.getText();
//        String mileageOut = tfMileageOut.getText();
        return new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission);
    }

    public Vehicle getSelectedVehicle() {
        return tv.getSelectionModel().getSelectedItem();
    }
}
