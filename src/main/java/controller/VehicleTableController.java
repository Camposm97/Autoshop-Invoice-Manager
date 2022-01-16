package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import model.database.DB;
import model.ui.FX;
import model.work_order.Vehicle;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.tableview2.TableView2;
import org.controlsfx.control.tableview2.cell.TextField2TableCell;

public class VehicleTableController {
    @FXML
    TextField tfVin, tfLicensePlate, tfColor, tfYear, tfMake, tfModel, tfEngine, tfTransmission, tfMileageIn, tfMileageOut;
    @FXML
    TableView2<Vehicle> tv;
    @FXML
    TableColumn<Vehicle, String> colVin, colLicensePlate, colColor;
    @FXML
    TableColumn<Vehicle, Integer> colYear;
    @FXML
    TableColumn<Vehicle, String> colMake, colModel, colEngine, colTransmission, colMileageIn, colMileageOut;

    @FXML
    public void initialize() {
        tfVin.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfLicensePlate.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfColor.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfYear.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfMake.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfModel.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfEngine.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfTransmission.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfMileageIn.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));
        tfMileageOut.textProperty().addListener((o, s1, s2) -> tv.getItems().setAll(DB.get().vehicles().filter(buildVehicle())));

        colVin.setCellValueFactory(c -> c.getValue().vinProperty());
        colLicensePlate.setCellValueFactory(c -> c.getValue().licensePlateProperty());
        colLicensePlate.setCellFactory(TextFieldTableCell.forTableColumn());
        colLicensePlate.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setLicensePlate(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colColor.setCellValueFactory(c -> c.getValue().colorProperty());
        colColor.setCellFactory(TextFieldTableCell.forTableColumn());
        colColor.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setColor(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colYear.setCellValueFactory(c -> c.getValue().yearProperty());
//        colYear.setCellFactory(TextFieldTableCell.forTableColumn());
//        colYear.setOnEditCommit(e -> {
//            int index = e.getTablePosition().getRow();
//            Vehicle vehicle = e.getTableView().getItems().get(index);
//            vehicle.setColor(e.getNewValue());
//            DB.get().vehicles().update(vehicle);
//        });
        colMake.setCellValueFactory(c -> c.getValue().makeProperty());
        colMake.setCellFactory(TextFieldTableCell.forTableColumn());
        colMake.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setMake(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colModel.setCellValueFactory(c -> c.getValue().modelProperty());
        colModel.setCellFactory(TextFieldTableCell.forTableColumn());
        colModel.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setModel(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colEngine.setCellValueFactory(c -> c.getValue().engineProperty());
        colEngine.setCellFactory(TextFieldTableCell.forTableColumn());
        colEngine.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setEngine(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colTransmission.setCellValueFactory(c -> c.getValue().transmissionProperty());
        colTransmission.setCellFactory(TextFieldTableCell.forTableColumn());
        colTransmission.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setTransmission(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colMileageIn.setCellValueFactory(c -> c.getValue().mileageInProperty());
        colMileageIn.setCellFactory(TextFieldTableCell.forTableColumn());
        colMileageIn.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setMileageIn(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        colMileageOut.setCellValueFactory(c -> c.getValue().mileageOutProperty());
        colMileageOut.setCellFactory(TextFieldTableCell.forTableColumn());
        colMileageOut.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Vehicle vehicle = e.getTableView().getItems().get(index);
            vehicle.setMileageOut(e.getNewValue());
            DB.get().vehicles().update(vehicle);
        });
        tv.getItems().setAll(DB.get().vehicles().getAll());
        FX.autoResizeColumns(tv);
    }

    public void disableEditing() {
        colVin.setEditable(false);
        colLicensePlate.setEditable(false);
        colColor.setEditable(false);
        colYear.setEditable(false);
        colMake.setEditable(false);
        colModel.setEditable(false);
        colEngine.setEditable(false);
        colTransmission.setEditable(false);
        colMileageIn.setEditable(false);
        colMileageOut.setEditable(false);
    }

    public void connect(WorkOrderWorkspaceController controller) {
        disableEditing();
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
        int year = Integer.parseInt(tfYear.getText());
        String make = tfMake.getText();
        String model = tfModel.getText();
        String licensePlate = tfLicensePlate.getText();
        String color = tfColor.getText();
        String engine = tfEngine.getText();
        String transmission = tfTransmission.getText();
        String mileageIn = tfMileageIn.getText();
        String mileageOut = tfMileageOut.getText();
        return new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut);
    }

    public Vehicle getSelectedVehicle() {
        return tv.getSelectionModel().getSelectedItem();
    }
}
