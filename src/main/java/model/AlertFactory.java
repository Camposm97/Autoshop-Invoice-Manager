package model;

import controller.WorkOrderPartWorkspaceController;
import controller.CustomerWorkspaceController;
import controller.VehicleWorkspaceController;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertFactory {
    public static void showAddCustomer() {
        Alert alert = AlertBuilder.buildDialog("Add Customer");
        CustomerWorkspaceController controller = new CustomerWorkspaceController();
        alert.getDialogPane().setContent(FX.view("Customer_Workspace.fxml", controller));
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.addCustomer();
        });
    }

    public static void showAddVehicle() {
        Alert alert = AlertBuilder.buildDialog("Add Vehicle");
        VehicleWorkspaceController controller = new VehicleWorkspaceController();
        alert.getDialogPane().setContent(FX.view("Vehicle_Workspace.fxml", controller));

        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.addVehicle();
        });
    }

    public static void showAddPart(WorkOrder workOrder) {
        Alert alert = AlertBuilder.buildDialog("Add Part");
        WorkOrderPartWorkspaceController controller = new WorkOrderPartWorkspaceController();
        Parent node = FX.view("Work_Order_Part_Workspace.fxml", controller);
        alert.getDialogPane().setContent(node);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                System.out.println("Save Part");
                controller.savePart(workOrder);
            }
        });
    }

    public static void showEditPart(WorkOrder workOrder, Item selectedItem) {
        Alert alert = AlertBuilder.buildDialog("Update Part");
        WorkOrderPartWorkspaceController controller = new WorkOrderPartWorkspaceController();
        Parent node = FX.view("Work_Order_Part_Workspace.fxml", controller);
        alert.getDialogPane().setContent(node);
        controller.loadPart(selectedItem);
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton())
                controller.updatePart(workOrder, selectedItem);
        });
    }

    public static void showAddLabor(WorkOrder workOrder) { // TODO
        Alert alert = AlertBuilder.buildDialog("Add Labor");
        alert.getDialogPane().setContent(FX.view("Work_Order_Labor_Workspace.fxml"));
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (e.getButtonData().isDefaultButton()) {
                System.out.println("Save Labor");
            }
        });
    }
}
