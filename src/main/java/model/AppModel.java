package model;

import model.work_order.RecentWorkOrders;

public class AppModel {
    public static final String TITLE = "Autoshop Invoice Manager";
    private RecentWorkOrders recentWorkOrders;
    private Preferences preferences;

    private static AppModel singleton;

    public static AppModel get() {
        if (singleton == null) singleton = new AppModel();
        return singleton;
    }

    private AppModel() {
        recentWorkOrders = new RecentWorkOrders();
        preferences = Preferences.get();
    }

    public RecentWorkOrders recentWorkOrders() {
        return recentWorkOrders;
    }

    public Preferences preferences() { return preferences; }
}
