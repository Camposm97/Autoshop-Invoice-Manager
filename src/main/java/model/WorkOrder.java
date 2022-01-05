package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class WorkOrder implements Billable {
    private int id;
    private Date dateCreated, dateCompleted;
    private Customer customer;
    private Vehicle vehicle;
    private ObservableList<Item> itemList;
    private ObservableList<Labor> laborList;

    public WorkOrder() {
        this.id = -1;
        this.dateCreated = Date.valueOf(LocalDate.now().toString());
        this.dateCompleted = null;
        this.customer = null;
        this.vehicle = null;
        this.itemList = FXCollections.observableArrayList();
        this.laborList = FXCollections.observableArrayList();
    }

    public WorkOrder(Customer customer, Vehicle vehicle) {
        this.id = -1;
        this.dateCreated = Date.valueOf(LocalDate.now().toString());
        this.dateCompleted = null;
        this.customer = customer;
        this.vehicle = vehicle;
        this.itemList = FXCollections.observableArrayList();
        this.laborList = FXCollections.observableArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNew() {
        return id != -1;
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

    public void updateItem(Item oldItem, Item newItem) {
        ListIterator<Item> iterator = itemList.listIterator();
        while (iterator.hasNext()) {
            Item currentItem = iterator.next();
            if (currentItem.equals(oldItem)) {
                iterator.set(newItem);
                break;
            }
        }
    }

    public boolean removeItem(Item item) {
        return itemList.remove(item);
    }

    public Iterator<Item> itemIterator() {
        return itemList.iterator();
    }

    public boolean addLabor(Labor labor) {
        return laborList.add(labor);
    }

    public boolean removeLabor(Labor labor) {
        return laborList.remove(labor);
    }

    public Iterator<Labor> laborIterator() {
        return laborList.iterator();
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

    @Override
    public String toString() {
        return "WorkOrder{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateCompleted=" + dateCompleted +
                ", customer=" + customer +
                ", vehicle=" + vehicle +
                ", itemList=" + itemList +
                ", laborList=" + laborList +
                '}';
    }

    public SimpleObjectProperty<Integer> idProperty() {
        return new SimpleObjectProperty<>(id);
    }

    public SimpleObjectProperty<Date> dateCreatedProperty() {
        return new SimpleObjectProperty<>(dateCreated);
    }

    public SimpleObjectProperty<Date> dateCompletedProperty() {
        return new SimpleObjectProperty<>(dateCompleted);
    }

    public SimpleObjectProperty<Double> billProperty() {
        return new SimpleObjectProperty<>(bill());
    }

    public ObservableList<Item> itemObservableList() {
        return itemList;
    }

    public ObservableList<Labor> laborObservableList() {
        return laborList;
    }
}
