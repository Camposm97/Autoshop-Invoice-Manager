package app;

/**
 * TODO
 * Add customer ids to work orders
 * Add listeners to Add Customer and Add Vehicle for Customer Table
 * Add a limitation to how many work orders can be displayed
 * Implement a listener when searching for work orders to save processing time
 *  Causes the program to slow down with big data
 *      Such as when starting program, work order table, and customers
 *  Move to MySQL? Supports Multi-threading
 * Change field for searching work order by first/last name and company to one field.
 */
public class AppWrapper {
    public static void main(String[] args) {
        App.main(args);
    }
}
