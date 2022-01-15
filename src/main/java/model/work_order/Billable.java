package model.work_order;

public interface Billable {
    double TAX_RATE = 1.08625;

    double subtotal();

    double tax();

    double bill();
}
