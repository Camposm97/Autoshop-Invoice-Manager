package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AutoPart extends Product implements Comparable<AutoPart> {
//    private int id;
//    private String name;
//    private String desc;
    private double retailPrice;
    private double listPrice;
    private boolean taxable;
    private int quantity;

    public AutoPart(String name, String desc, double retailPrice, double listPrice, int quantity, boolean taxable) {
        super(name, desc);
//        this.name = name;
//        this.desc = desc;
        this.retailPrice = retailPrice;
        this.listPrice = listPrice;
        this.quantity = quantity;
        this.taxable = taxable;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public boolean isNew() {
//        return id <= 0;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDes(String desc) {
//        this.desc = desc;
//    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public double subTotal() {
        return retailPrice * quantity;
    }

    @Override
    public double tax() {
        return taxable ? bill() - subTotal() : 0;
    }

    @Override
    public double bill() {
        return taxable ? TAX_RATE * (retailPrice * quantity) : retailPrice * quantity;
    }

    @Override
    public int compareTo(AutoPart o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "Item{" + super.toString() +
                ", retailPrice=" + retailPrice +
                ", listPrice=" + listPrice +
                ", taxable=" + taxable +
                ", quantity=" + quantity +
                '}';
    }

//    public SimpleStringProperty nameProperty() {
//        return new SimpleStringProperty(name);
//    }
//
//    public SimpleStringProperty descProperty() {
//        return new SimpleStringProperty(desc);
//    }

    public SimpleObjectProperty<Double> retailPriceProperty() {
        return new SimpleObjectProperty<>(retailPrice);
    }

    public SimpleObjectProperty<Double> listPriceProperty() {
        return new SimpleObjectProperty<>(listPrice);
    }

    public SimpleObjectProperty<Integer> quantityProperty() {
        return new SimpleObjectProperty<>(quantity);
    }

    public SimpleObjectProperty<Boolean> taxableProperty() {
        return new SimpleObjectProperty<>(taxable);
    }

    public SimpleStringProperty billProperty() {
        return new SimpleStringProperty(String.format("$ %.2f", bill()));
    }
}
