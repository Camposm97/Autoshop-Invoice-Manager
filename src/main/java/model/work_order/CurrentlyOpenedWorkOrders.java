package model.work_order;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @brief Keeps track of currently opened work orders in tabs by storing their ids in a list
 */
public class CurrentlyOpenedWorkOrders {
    private static final String DAT_FILE = "./currowos.dat";
    private LinkedList<Integer> ids;

    public CurrentlyOpenedWorkOrders() {
        /* TODO: Implement save feature */
        ids = new LinkedList<>();
    }

    public void add(int workOrderId) {
        if (ids.contains(workOrderId)) {
            ids.removeFirstOccurrence(workOrderId);
        }
        ids.addFirst(workOrderId);
    }

    public void remove(int workOrderId) {
        ids.removeFirstOccurrence(workOrderId);
    }

    public Iterator<Integer> iterator() {
        return ids.iterator();
    }
}
