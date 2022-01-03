package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class WorkOrder implements Billable {
    private int id;
    private Date dateCreated, dateCompleted;
    private Customer customer;
    private Vehicle vehicle;
    private List<Item> itemList;
    private List<Labor> laborList;

    public WorkOrder(Customer customer, Vehicle vehicle) {
        this.id = id;
        this.customer = customer;
        this.vehicle = vehicle;
        this.dateCreated = Date.valueOf(LocalDate.now().toString());
        this.dateCompleted = null;
        this.itemList = new LinkedList<>();
        this.laborList = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
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

    public boolean addItem(Item item) {
        return itemList.add(item);
    }

    public boolean removeItem(Item item) {
        return itemList.remove(item);
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public boolean addLabor(Labor labor) {
        return laborList.add(labor);
    }

    public boolean removeLabor(Labor labor) {
        return laborList.remove(labor);
    }

    public List<Labor> getLaborList() {
        return laborList;
    }

    @Override
    public double bill() {
        double total = 0;
        for (Item item : itemList) {
            total += item.bill();
        }
        for (Labor labor : laborList) {
            total += labor.bill();
        }
        return total;
    }
}
