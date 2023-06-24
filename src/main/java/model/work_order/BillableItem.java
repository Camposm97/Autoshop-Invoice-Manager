package model.work_order;

public interface BillableItem extends Billable {
    double tax(double taxRate);

    double bill(double taxRate);
}
