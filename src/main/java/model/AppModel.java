package model;

import model.work_order.RecentWorkOrders;

public class AppModel {
    public static final String TITLE = "Autoshop Invoice Manager";
    private RecentWorkOrders recentWorkOrders;

    public AppModel() {
        recentWorkOrders = new RecentWorkOrders();
    }

    public RecentWorkOrders getRecentWorkOrders() {
        return recentWorkOrders;
    }
}
