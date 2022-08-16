package model.tps;

import model.work_order.Labor;
import model.work_order.WorkOrder;

public class UpdateLaborTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private Labor oldLabor, newLabor;


    public UpdateLaborTransaction(WorkOrder workOrder, Labor oldLabor, Labor newLabor) {
        this.workOrder = workOrder;
        this.oldLabor = oldLabor;
        this.newLabor = newLabor;
    }


    @Override
    public void doTransaction() {
        workOrder.updateLabor(oldLabor, newLabor);
    }

    @Override
    public void undoTransaction() {
        workOrder.updateLabor(newLabor, oldLabor);
    }
}
