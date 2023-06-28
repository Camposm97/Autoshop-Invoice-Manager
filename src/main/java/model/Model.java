package model;

import model.work_order.CurrentlyOpenedWorkOrders;
import model.work_order.RecentWorkOrders;

import java.util.LinkedList;
import java.util.List;

/**
 * @brief Contains global variables to be referenced throughout the program
 */
public class Model {
    public static final String TITLE = "Autoshop Invoice Manager";
    private List<ExitObservable> exitObservables;
    private RecentWorkOrders recentWorkOrders;
    private CurrentlyOpenedWorkOrders currOWOs;
    private Preferences preferences;

    private static Model singleton;

    public static Model get() {
        if (singleton == null) singleton = new Model();
        return singleton;
    }

    private Model() {
        exitObservables = new LinkedList<>();
        recentWorkOrders = new RecentWorkOrders();
        currOWOs = new CurrentlyOpenedWorkOrders();
        preferences = Preferences.get();
    }

    public void save() {
        for (ExitObservable observable : exitObservables)
            observable.exit();
        recentWorkOrders.save();
        currOWOs.save();
    }

    public RecentWorkOrders recentWorkOrders() {
        return recentWorkOrders;
    }

    public CurrentlyOpenedWorkOrders currOWOs() {
        return currOWOs;
    }

    public Preferences preferences() { return preferences; }

    public void addExitObservable(ExitObservable exitObservable) {
        this.exitObservables.add(exitObservable);
    }

    public void removeExitObservable(ExitObservable exitObservable) {
        this.exitObservables.remove(exitObservable);
    }
}
