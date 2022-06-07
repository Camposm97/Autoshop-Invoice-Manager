package model.work_order;

import java.sql.Date;

public class WorkOrderPayment {
    private int id;
    private int workOrderId;
    private Date date;
    private double amount;
    private Payment type;

    public WorkOrderPayment(int id, int workOrderId, Date date, double amount, Payment type) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public WorkOrderPayment(int workOrderId, Date dateOfPayment, double amount, Payment payment) {
        this.workOrderId = workOrderId;
        this.date = dateOfPayment;
        this.amount = amount;
        this.type = payment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Payment getType() {
        return type;
    }

    public void setType(Payment type) {
        this.type = type;
    }
}
