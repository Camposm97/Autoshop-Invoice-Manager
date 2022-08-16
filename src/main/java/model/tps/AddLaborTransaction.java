package model.tps;

import model.work_order.Labor;
import model.work_order.WorkOrder;

public class AddLaborTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private Labor labor;

    public AddLaborTransaction(WorkOrder workOrder, Labor labor) {
        this.workOrder = workOrder;
        this.labor = labor;
    }

    @Override
    public void doTransaction() {
        workOrder.addLabor(labor);
    }

    @Override
    public void undoTransaction() {
        workOrder.removeLabor(labor);
    }
}
