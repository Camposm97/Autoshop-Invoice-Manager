package main.model;

public class Item implements Billable, Comparable<Item> {
    private String id;
    private String name;
    private String desc;
    private double retailPrice;
    private double listPrice;
    private boolean taxable;
    private int quantity;

    public Item(String id, String name, String desc, double retailPrice, double listPrice, boolean taxable, int quantity) {
        this.id = id;
        this.name = name;
        this.retailPrice = retailPrice;
        this.listPrice = listPrice;
        this.taxable = taxable;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDes(String desc) {
        this.desc = desc;
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
    public double bill() {
        return taxable ? TAX_RATE * (retailPrice * quantity) : retailPrice * quantity;
    }

    @Override
    public int compareTo(Item o) {
        return this.id.compareTo(o.id);
    }
}
