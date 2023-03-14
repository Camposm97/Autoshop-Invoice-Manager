package model;

import model.work_order.CurrentlyOpenedWorkOrders;
import model.work_order.RecentWorkOrders;

/**
 * @brief Contains global variables to be referenced throughout the program
 */
public class Model {
    public static final String TITLE = "Autoshop Invoice Manager";
    private RecentWorkOrders recentWorkOrders;
    private CurrentlyOpenedWorkOrders currOWOs;
    private Preferences preferences;

    private static Model singleton;

    public static Model get() {
        if (singleton == null) singleton = new Model();
        return singleton;
    }

    private Model() {
        recentWorkOrders = new RecentWorkOrders();
        currOWOs = new CurrentlyOpenedWorkOrders();
        preferences = Preferences.get();
    }

    public RecentWorkOrders recentWorkOrders() {
        return recentWorkOrders;
    }

    public CurrentlyOpenedWorkOrders currOWOs() {
        return currOWOs;
    }

    public Preferences preferences() { return preferences; }
}
