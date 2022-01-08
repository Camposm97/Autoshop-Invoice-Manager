package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WorkOrder implements Billable {
    private int id;
    private Date dateCreated, dateCompleted;
    private Customer customer;
    private Vehicle vehicle;
    private ObservableList<AutoPart> itemList;
    private ObservableList<Labor> laborList;

    public WorkOrder() {
        this.dateCreated = Date.valueOf(LocalDate.now().toString());
        this.dateCompleted = null;
        this.customer = null;
        this.vehicle = null;
        this.itemList = FXCollections.observableArrayList();
        this.laborList = FXCollections.observableArrayList();
    }

    public WorkOrder(Customer customer, Vehicle vehicle) {
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
        return id <= 0;
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

    public boolean addItem(AutoPart item) {
        return itemList.add(item);
    }

    public void updateItem(AutoPart oldItem, AutoPart newItem) {
        ListIterator<AutoPart> iterator = itemList.listIterator();
        while (iterator.hasNext()) {
            AutoPart currentItem = iterator.next();
            if (currentItem.equals(oldItem)) {
                iterator.set(newItem);
                break;
            }
        }
    }

    public boolean removeItem(AutoPart item) {
        return itemList.remove(item);
    }

    public Iterator<AutoPart> autoPartIterator() {
        return itemList.iterator();
    }

    public boolean addLabor(Labor labor) {
        return laborList.add(labor);
    }

    public void updateLabor(Labor oldLabor, Labor newLabor) {
        ListIterator<Labor> iterator = laborList.listIterator();
        while (iterator.hasNext()) {
            Labor currentLabor = iterator.next();
            if (currentLabor.equals(oldLabor)) {
                iterator.set(newLabor);
                break;
            }
        }
    }

    public boolean removeLabor(Labor labor) {
        return laborList.remove(labor);
    }

    public Iterator<Labor> laborIterator() {
        return laborList.iterator();
    }

    public SimpleObjectProperty<Integer> idProperty() {
        return new SimpleObjectProperty<>(id);
    }

    public SimpleStringProperty dateCreatedProperty() {
        return new SimpleStringProperty(dateCreated.toLocalDate().format(DateTimeFormatter.ofPattern("MM/DD/YYYY")));
    }

    public SimpleStringProperty dateCompletedProperty() {
        if (dateCompleted != null) {
            return new SimpleStringProperty(dateCompleted.toLocalDate().format(DateTimeFormatter.ofPattern("MM/DD/YYYY")));
        }
        return new SimpleStringProperty();
    }

    public SimpleStringProperty billProperty() {
        return new SimpleStringProperty(String.format("$ %.2f", bill()));
    }

    public ObservableList<AutoPart> itemList() {
        return itemList;
    }

    public ObservableList<Labor> laborList() {
        return laborList;
    }

    @Override
    public double subTotal() {
        double itemSum = itemList.stream()
                .map(item -> item.subTotal())
                .reduce((x, y) -> x + y)
                .get();
        double laborSum = laborList.stream()
                .map(labor -> labor.subTotal())
                .reduce((x, y) -> x + y)
                .get();
        return itemSum + laborSum;
    }

    @Override
    public double tax() {
        double itemTaxSum = itemList.stream()
                .filter(item -> item.isTaxable())
                .map(item -> item.tax())
                .reduce((x, y) -> x + y)
                .get();
        double laborTaxSum = laborList.stream()
                .filter(labor -> labor.isTaxable())
                .map(labor -> labor.tax())
                .reduce((x, y) -> x + y)
                .get();
        return itemTaxSum + laborTaxSum;
    }

    @Override
    public double bill() {
        double total = 0;
        for (AutoPart item : itemList) {
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
}
