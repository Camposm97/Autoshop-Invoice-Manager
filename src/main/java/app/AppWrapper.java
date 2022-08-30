package app;

/**
 * TODO
 * Add customer ids to work orders
 * Add listeners to Add Customer and Add Vehicle for Customer Table
 * Add a limitation to how many work orders can be displayed
 * Implement a listener when searching for work orders to save processing time
 *  Causes the program to slow down with big data
 *  Move to MySQL? Supports Multi-threading
 * Fix bug when editing preferences, pressing enter while in a text field
 *  Maybe have it where everytime a field is updated, then update Preferences
 */
public class AppWrapper {
    public static void main(String[] args) {
        App.main(args);
    }
}
