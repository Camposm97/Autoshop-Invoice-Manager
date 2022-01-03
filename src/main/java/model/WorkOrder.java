package model;

import java.time.LocalDate;

public class WorkOrder {
    private int id;
    private LocalDate dateCreated, dateCompleted;
    private Customer customer;
    private Vehicle vehicle;
    private Invoice invoice;

    public WorkOrder(int id, Customer customer, Vehicle vehicle, Invoice invoice) {
        this.id = id;
        this.dateCreated = LocalDate.now();
        this.customer = customer;
        this.vehicle = vehicle;
        this.invoice = invoice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
