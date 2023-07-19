package model.work_order;

import model.Observable;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @brief Keeps track of recent edited work orders by storing their ids in a list
 */
public class RecentWorkOrders {
    private static final String DAT_FILE = "./recents.dat";
    private static final int SIZE = 10;
    private LinkedList<Integer> ids;
    private LinkedList<Observable> observables;

    public RecentWorkOrders() {
        try {
            File file = new File(DAT_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ids = (LinkedList<Integer>) ois.readObject();
                ois.close();
                System.out.println("Loaded " + DAT_FILE + ": " + ids);
            } else {
                System.out.println("Missing " + DAT_FILE);
                ids = new LinkedList<>();
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Failed to load " + DAT_FILE);
        } finally {
            observables = new LinkedList<>();
        }
    }

    public void save() {
        try {
            File file = new File(DAT_FILE);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ids);
            oos.close();
            System.out.println("Saved " + DAT_FILE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void add(int workOrderId) {
        if (ids.contains(workOrderId)) {
            ids.removeFirstOccurrence(workOrderId);
        }
        ids.addFirst(workOrderId);
        if (ids.size() > SIZE) {
            ids.removeLast();
        }
        for (Observable o : observables) o.update();
        System.out.println("Add recent work order (" + workOrderId + "): " + ids);
    }

    public boolean remove(int workOrderId) {
        var flag = ids.removeFirstOccurrence(workOrderId);
        for (Observable o : observables) o.update();
        System.out.println("Remove recent work order (" + workOrderId + "): " + ids);
        return flag;
    }

    public Iterator<Integer> iterator() {
        return ids.iterator();
    }

    public void addObserver(Observable o) {
        observables.add(o);
    }

    public void removeObserver(Observable o) {
        observables.remove(o);
    }
}
