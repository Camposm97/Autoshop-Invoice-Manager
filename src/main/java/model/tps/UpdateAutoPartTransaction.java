package model.tps;

import model.work_order.AutoPart;
import model.work_order.WorkOrder;

public class UpdateAutoPartTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private AutoPart oldAutoPart, newAutoPart;

    public UpdateAutoPartTransaction(WorkOrder workOrder, AutoPart oldAutoPart, AutoPart newAutoPart) {
        this.workOrder = workOrder;
        this.oldAutoPart = oldAutoPart;
        this.newAutoPart = newAutoPart;
    }

    @Override
    public void doTransaction() {
        workOrder.updateAutoPart(oldAutoPart, newAutoPart);
    }

    @Override
    public void undoTransaction() {
        workOrder.updateAutoPart(newAutoPart, oldAutoPart);
    }
}
