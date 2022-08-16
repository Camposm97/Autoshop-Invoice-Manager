package model.tps;

import model.work_order.AutoPart;
import model.work_order.Product;
import model.work_order.WorkOrder;

import java.util.List;

public class DeleteAutoPartTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private AutoPart autoPart;
    private List<Product> productsMarkedForDeletion;

    public DeleteAutoPartTransaction(WorkOrder workOrder, AutoPart autoPart, List<Product> productsMarkedForDeletion) {
        this.workOrder = workOrder;
        this.autoPart = autoPart;
        this.productsMarkedForDeletion = productsMarkedForDeletion;
    }

    @Override
    public void doTransaction() {
        workOrder.removeAutoPart(autoPart);
        if (!autoPart.isNew()) {
            productsMarkedForDeletion.add(autoPart);
        }
    }

    @Override
    public void undoTransaction() {
        workOrder.addAutoPart(autoPart);
        if (!autoPart.isNew()) {
            productsMarkedForDeletion.remove(autoPart);
        }
    }
}
