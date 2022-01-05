package model;

import controller.WorkOrderPartController;
import controller.CustomerController;
import controller.VehicleController;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertFactory {
    public static void showAddCustomer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Customer");
        alert.setHeaderText("Please fill out the following information");
        ButtonType bt = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(bt);

        CustomerController controller = new CustomerController();
        alert.getDialogPane().setContent(FX.view("Customer.fxml", controller));

        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> controller.addCustomer());
    }

    public static void showAddVehicle() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Vehicle");
        alert.setHeaderText("Please fill out the following information");
        ButtonType bt = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(bt);

        VehicleController controller = new VehicleController();
        alert.getDialogPane().setContent(FX.view("Vehicle_Add.fxml", controller));

        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> controller.addVehicle());
    }

    public static void showAddPart(WorkOrder workOrder) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Part");
        alert.setHeaderText("Please fill out the following information");

        WorkOrderPartController controller = new WorkOrderPartController(workOrder);
        Parent node = FX.view("Work_Order_Add_Part.fxml", controller);
        alert.getDialogPane().setContent(node);
        ButtonType btSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btSave, btCancel);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                System.out.println("Save Part");
                controller.savePart();
            }
        });
    }

    public static void showAddLabor(WorkOrder workOrder) { // TODO
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Labor");
        alert.setHeaderText("Please fill out the following information");
        alert.getDialogPane().setContent(FX.view("Work_Order_Add_Labor.fxml"));
        ButtonType btSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btSave, btCancel);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                System.out.println("Save Labor");
            }
        });
    }
}
