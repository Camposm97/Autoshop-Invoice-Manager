package app;

/**
 * TODO
 * Implement payment system to keep track of completed work orders.
 * Implement vehicle transfer of ownership between customers (3)
 * Implement re-sizing font system in preferences (2)
 *
 * Parse SQLite database to MySQL (2)
 *
 * When we're in the WorkOrder workspace and if we add a new customer or vehicle, then the program
 * should update the searches for customer and vehicle (2)
 *
 * Auto filling customer and vehicle in Work Order should be customer first then vehicle where
 * the options for auto filling the vehicle fields are customer owned vehicles stored in the
 * database
 *
 * Foolproof fields that require a specific text formats (such as currency when adding parts
 * and labor)
 *
 * Implement configurable tax rate (3)
 * Each work order should have their own tax rate where its default tax rate
 * will be the configration tax rate (1)
 */
public class AppWrapper {
    public static void main(String[] args) {
        App.main(args);
    }
}
