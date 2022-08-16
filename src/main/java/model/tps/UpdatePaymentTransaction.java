package model.tps;

import model.work_order.WorkOrder;
import model.work_order.WorkOrderPayment;

public class UpdatePaymentTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private WorkOrderPayment oldPayment, newPayment;

    public UpdatePaymentTransaction(WorkOrder workOrder, WorkOrderPayment oldPayment, WorkOrderPayment newPayment) {
        this.workOrder = workOrder;
        this.oldPayment = oldPayment;
        this.newPayment = newPayment;
    }

    @Override
    public void doTransaction() {
        workOrder.updatePayment(oldPayment, newPayment);
    }

    @Override
    public void undoTransaction() {
        workOrder.updatePayment(newPayment, oldPayment);
    }
}
