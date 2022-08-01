package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.work_order.Payment;
import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentWorkspaceController {
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<Payment> cb;
    @FXML
    TextField tfAmount;

    @FXML
    public void initialize() {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate x) {
                return x != null ? x.format(DateTimeFormatter.ofPattern("MM/dd/u")) : null;
            }
            @Override
            public LocalDate fromString(String s) {
                return LocalDate.now();
            }
        });
        datePicker.setValue(LocalDate.now());
        cb.setValue(Payment.Cash);
        cb.getItems().setAll(Payment.list());
    }

    public void savePayment(WorkOrder workOrder) {
        WorkOrderPayment payment = buildPayment();
        workOrder.addPayment(payment);
    }

    public void loadPayment(WorkOrderPayment payment) {
        datePicker.setValue(payment.getDate().toLocalDate());
        cb.setValue(payment.getType());
        tfAmount.setText(String.valueOf(payment.getAmount()));
    }

    public void updatePayment(WorkOrder workOrder, WorkOrderPayment oldPayment) {
        WorkOrderPayment newPayment = buildPayment();
        newPayment.setId(oldPayment.getId());
        workOrder.updatePayment(oldPayment, newPayment);
    }

    public WorkOrderPayment buildPayment() {
        Date date = Date.valueOf(datePicker.getValue());
        Payment type = cb.getValue();
        double amount;
        try {
            amount = Double.parseDouble(tfAmount.getText());
        } catch (NumberFormatException e) {
            amount = 0.0;
        }
        return new WorkOrderPayment(date, type, amount);
    }
}
