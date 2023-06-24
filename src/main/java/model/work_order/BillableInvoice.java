package model.work_order;

public interface BillableInvoice extends Billable {
    double tax();

    double bill();
}
