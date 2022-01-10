package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Labor extends Product implements Comparable<Labor> {
//    private int id;
//    private String name, desc;
    private double billedHrs, rate;
    private boolean taxable;

    public Labor(String name, String desc, double billedHrs, double rate, boolean taxable) {
        super(name, desc);
//        this.name = name;
//        this.desc = desc;
        this.billedHrs = billedHrs;
        this.rate = rate;
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
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }

    public double getBilledHrs() {
        return billedHrs;
    }

    public void setBilledHrs(double billedHrs) {
        this.billedHrs = billedHrs;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    @Override
    public double subtotal() {
        return billedHrs * rate;
    }

    @Override
    public double tax() {
        return taxable ? bill() - subtotal() : 0;
    }

    @Override
    public double bill() {
        return taxable ? TAX_RATE * (billedHrs * rate) : billedHrs * rate;
    }

    @Override
    public String toString() {
        return "Labor{" + super.toString() +
                ", billedHrs=" + billedHrs +
                ", rate=" + rate +
                ", taxable=" + taxable +
                '}';
    }

//    public SimpleStringProperty nameProperty() {
//        return new SimpleStringProperty(name);
//    }
//
//    public SimpleStringProperty descProperty() {
//        return new SimpleStringProperty(desc);
//    }

    public SimpleObjectProperty<Double> billedHrsProperty() {
        return new SimpleObjectProperty<>(billedHrs);
    }

    public SimpleObjectProperty<Double> rateProperty() {
        return new SimpleObjectProperty<>(rate);
    }

    public SimpleObjectProperty<Boolean> taxableProperty() {
        return new SimpleObjectProperty<>(taxable);
    }

    public SimpleStringProperty billProperty() {
        return new SimpleStringProperty(String.format("$ %.2f", bill()));
    }

    @Override
    public int compareTo(Labor o) {
       return getName().compareTo(o.getName());
    }
}
