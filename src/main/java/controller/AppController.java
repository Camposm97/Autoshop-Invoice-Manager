package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import model.FX;
import java.util.Optional;

public class AppController {
    @FXML
    BorderPane root;

    public void addCustomer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Customer");
        alert.setHeaderText("Please enter the following information:");
        ButtonType bt = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(bt);

        CustomerController addCustomerController = new CustomerController();
        alert.getDialogPane().setContent(FX.view("Customer.fxml", addCustomerController));

        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> addCustomerController.addCustomer());
    }

    public void addVehicle() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Vehicle");
        alert.setHeaderText("Please enter the following information:");
        ButtonType bt = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(bt);

        VehicleController addVehicleController = new VehicleController();
        alert.getDialogPane().setContent(FX.view("Vehicle_Add.fxml", addVehicleController));

        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> addVehicleController.addVehicle());
    }

    public void addWorkOrder() {
        WorkOrderController controller = new WorkOrderController();
        root.setCenter(FX.view("Work_Order.fxml", controller));
    }

    public void viewCustomers() {
        root.setCenter(FX.view("Customer_Table.fxml"));
    }

    public void viewVehicles() {

    }

    public void viewWorkOrders() {
        root.setCenter(FX.view("Work_Order_Table.fxml"));
    }

    public void exit() {
        Platform.exit();
    }
}
