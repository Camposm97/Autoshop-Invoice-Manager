package model.work_order;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @brief Keeps track of currently opened work orders in tabs by storing their ids in a list
 */
public class CurrentlyOpenedWorkOrders {
    private static final String DAT_FILE = "./currowos.dat";
    private LinkedList<Integer> ids;

    public CurrentlyOpenedWorkOrders() {
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
        ids.addLast(workOrderId);
        System.out.println("Add currowo (" + workOrderId + "): " + ids);
    }

    public void remove(int workOrderId) {
        var flag = ids.removeFirstOccurrence(workOrderId);
        System.out.println("Remove currowo (" + workOrderId + "): " + ids);
    }

    public boolean contains(int workOrderId) {
        return ids.contains(workOrderId);
    }

    public Iterator<Integer> iterator() {
        return ids.iterator();
    }
}
