package model.work_order;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Model;

public class AutoPart extends Product implements Comparable<AutoPart> {
    private double retailPrice;
    private double listPrice;
    private int quantity;
    private boolean taxable;

    public AutoPart(String name, String desc, double retailPrice, double listPrice, int quantity, boolean taxable) {
        super(name, desc);
        this.retailPrice = retailPrice;
        this.listPrice = listPrice;
        this.quantity = quantity;
        this.taxable = taxable;
    }

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
    public double subtotal() {
        return retailPrice * quantity;
    }

    @Override
    public double tax() {
        return taxable ? bill() - subtotal() : 0;
    }

    @Override
    public double bill() {
        return taxable ? Model.get().preferences().getTaxRate() * (retailPrice * quantity) : retailPrice * quantity;
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

    public SimpleStringProperty retailPriceProperty() {
        return new SimpleStringProperty(String.format("%.2f", retailPrice));
    }

    public SimpleStringProperty listPriceProperty() {
        return new SimpleStringProperty(String.format("%.2f", listPrice));
    }

    public SimpleObjectProperty<Integer> quantityProperty() {
        return new SimpleObjectProperty<>(quantity);
    }

    public SimpleStringProperty subtotalProperty() {
        return new SimpleStringProperty(String.format("$ %.2f", subtotal()));
    }

    public SimpleStringProperty billProperty() {
        return new SimpleStringProperty(String.format("$ %.2f", bill()));
    }
}
