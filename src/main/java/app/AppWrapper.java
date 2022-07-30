package app;

/**
 * TODO
 * Implement payment system to keep track of completed work orders.
 * Implement vehicle transfer of ownership between customers
 * Implement re-sizing font system in preferences
 * Implement vehicle table view in customer view to show vehicles owned by a selected customer
 *
 * Parse SQLite database to MySQL
 *
 * When we're in the WorkOrder workspace and if we add a new customer or vehicle, then the program
 * should update the searches for customer and vehicle
 *
 * Implement configurable tax rate (LATER)
 * Each work order should have their own tax rate where its default tax rate
 * will be the configration tax rate
 */
public class AppWrapper {
    public static void main(String[] args) {
        App.main(args);
    }
}
