package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.work_order.Payment;
import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

public class PaymentWorkspaceController {
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<Payment> cb;
    @FXML
    TextField tfAmount;

    @FXML
    public void initialize() {
        tfAmount.setText(null);

    }

    public void savePayment(WorkOrder workOrder) {

    }

    public void loadPayment(WorkOrderPayment selectedPayment) {

    }

    public void updatePayment(WorkOrder workOrder, WorkOrderPayment selectedPayment) {

    }
}
