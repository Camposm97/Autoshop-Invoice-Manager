package model.work_order;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

public class WorkOrderPayment implements Comparable<WorkOrderPayment> {
    private int id;
    private int workOrderId;
    private Payment type;
    private Date date;
    private double amount;

    public WorkOrderPayment(int id, int workOrderId, Date date, Payment type, double amount) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    public WorkOrderPayment(int workOrderId, Date dateOfPayment, Payment type, double amount) {
        this.id = -1;
        this.workOrderId = workOrderId;
        this.date = dateOfPayment;
        this.type = type;
        this.amount = amount;
    }

    public boolean isNew() {
        return id <= 0;
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

    public Payment getType() {
        return type;
    }

    public void setType(Payment type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(@NotNull WorkOrderPayment o) {
        return Integer.compare(id, o.id);
    }
}
