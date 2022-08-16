package model.tps;

import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

public class AddPaymentTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private WorkOrderPayment payment;

    public AddPaymentTransaction(WorkOrder workOrder, WorkOrderPayment payment) {
        this.workOrder = workOrder;
        this.payment = payment;
    }

    @Override
    public void doTransaction() {
        workOrder.addPayment(payment);
    }

    @Override
    public void undoTransaction() {
        workOrder.removePayment(payment);
    }
}
