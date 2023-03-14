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
        ids.addLast(workOrderId);
        System.out.println("currOWOs: " + ids);
    }

    public void remove(int workOrderId) {
        ids.removeFirstOccurrence(workOrderId);
        System.out.println("currOWOs: " + ids);
    }

    public Iterator<Integer> iterator() {
        return ids.iterator();
    }
}
