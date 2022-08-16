package model.tps;

import model.work_order.AutoPart;
import model.work_order.WorkOrder;

public class AddAutoPartTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private AutoPart autoPart;

    public AddAutoPartTransaction(WorkOrder workOrder, AutoPart autoPart) {
        this.workOrder = workOrder;
        this.autoPart = autoPart;
    }

    @Override
    public void doTransaction() {
        workOrder.addAutoPart(autoPart);
    }

    @Override
    public void undoTransaction() {
        workOrder.removeAutoPart(autoPart);
    }
}
