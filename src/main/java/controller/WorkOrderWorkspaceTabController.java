package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import model.work_order.WorkOrder;

public class WorkOrderWorkspaceTabController {
    @FXML private Tab tab;
    @FXML private WorkOrderWorkspaceController workspaceController;

    @FXML
    public void initialize() {

    }

    public void load(WorkOrder workOrder) {
        workspaceController.loadWorkOrder(workOrder);
    }
}
