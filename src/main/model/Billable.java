package main.model;

public interface Billable {
    double TAX_RATE = 1.08625;

    double bill();
}
