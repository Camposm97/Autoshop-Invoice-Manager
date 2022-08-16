package model.tps;

import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

import java.util.List;

public class DeletePaymentTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private WorkOrderPayment payment;
    private List<WorkOrderPayment> paymentsMarkedForDeletion;

    public DeletePaymentTransaction(WorkOrder workOrder, WorkOrderPayment payment, List<WorkOrderPayment> paymentsMarkedForDeletion) {
        this.workOrder = workOrder;
        this.payment = payment;
        this.paymentsMarkedForDeletion = paymentsMarkedForDeletion;
    }

    @Override
    public void doTransaction() {
        workOrder.removePayment(payment);
        if (!payment.isNew()) {
            paymentsMarkedForDeletion.add(payment);
        }
    }

    @Override
    public void undoTransaction() {
        workOrder.addPayment(payment);
        if (!payment.isNew()) {
            paymentsMarkedForDeletion.remove(payment);
        }
    }
}
