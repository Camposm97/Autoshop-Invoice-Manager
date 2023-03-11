package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.work_order.PaymentMethod;
import model.work_order.WorkOrderPayment;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class PaymentWorkspaceController {
    private int paymentId;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<PaymentMethod> cb;
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
        cb.setValue(PaymentMethod.Cash);
        cb.getItems().setAll(PaymentMethod.list());
    }

    public void savePayment(Function<WorkOrderPayment, Void> callback) {
        WorkOrderPayment payment = buildPayment();
//        workOrder.addPayment(payment);
        callback.apply(payment);
    }

    public void loadPayment(WorkOrderPayment payment) {
        this.paymentId = payment.getId();
        datePicker.setValue(payment.getDate().toLocalDate());
        cb.setValue(payment.getType());
        tfAmount.setText(String.valueOf(payment.getAmount()));
    }

    public void updatePayment(Function<WorkOrderPayment, Void> callback) {
        WorkOrderPayment newPayment = buildPayment();
        callback.apply(newPayment);
    }

    public WorkOrderPayment buildPayment() {
        Date date = Date.valueOf(datePicker.getValue());
        PaymentMethod type = cb.getValue();
        double amount;
        try {
            amount = Double.parseDouble(tfAmount.getText());
        } catch (NumberFormatException e) {
            amount = 0.0;
        }
        return new WorkOrderPayment(paymentId, date, type, amount);
    }
}
