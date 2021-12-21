package main.model;

import java.util.LinkedList;
import java.util.List;

public class Invoice implements Billable {
    private int id;
    private List<Item> items;

    public Invoice() {
        this.items = new LinkedList<>();
    }

    public boolean addItem(Item item) {
        return this.items.add(item);
    }

    public boolean removeItem(Item item) {
        return this.items.remove(item);
    }

    @Override
    public double bill() {
        double total = 0;
        for (Item item : items) {
            total += item.bill();
        }
        return total;
    }
}
