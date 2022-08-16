package model.tps;

import model.work_order.Labor;
import model.work_order.Product;
import model.work_order.WorkOrder;

import java.util.List;

public class DeleteLaborTransaction implements TPS.Transaction {
    private WorkOrder workOrder;
    private Labor labor;
    private List<Product> productsMarkedForDeletion;

    public DeleteLaborTransaction(WorkOrder workOrder, Labor labor, List<Product> productsMarkedForDeletion) {
        this.workOrder = workOrder;
        this.labor = labor;
        this.productsMarkedForDeletion = productsMarkedForDeletion;
    }

    @Override
    public void doTransaction() {
        workOrder.removeLabor(labor);
        if (!labor.isNew()) {
            productsMarkedForDeletion.add(labor);
        }
    }

    @Override
    public void undoTransaction() {
        workOrder.addLabor(labor);
        if (!labor.isNew()) {
            productsMarkedForDeletion.remove(labor);
        }
    }
}
