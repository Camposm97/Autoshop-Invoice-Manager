package app;

/**
 * TODO
 * Save preferences to database instead of having a separate file called preferences.config
 * Refactor database names and attributes for item and work_order_item
 * Implement payment system to keep track of completed work orders.
 * Implement vehicle transfer of ownership between customers
 * Implement notification system of in-completed work orders and work orders that are still being edited.
 * Implement re-sizing font system in preferences
 * Implement Tab System for work orders (v2.0)
 *
 * Parse SQLite database to MySQL
 *
 * When editing a work order labor description, when the user pressed TAB on the labor description, the program should change focus to the next field
 *
 * When we're in the WorkOrder workspace and if we add a new customer or vehicle, then the program should update the searches for customer and vehicle
 *
 * Include in My_Company.fxml
 *  Report the number of completed work orders in the current month
 *
 *  Display a title saying "Uncompleted work orders" and the amount
 *  If there are uncompleted work orders, display those work orders using TableView
 *  If there are no uncompleted work orders, display an empty TableView
 *
 */
public class AppWrapper {
    public static void main(String[] args) {
        App.main(args);
    }
}
