package model;

public interface Billable {
    double TAX_RATE = 1.08625;

    double subtotal();

    double tax();

    double bill();
}
