package model.work_order;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RecentWorkOrders {
    private static final int SIZE = 10;
    private LinkedList<Integer> list;

    public RecentWorkOrders() {
        try {
            File file = new File("recents.dat");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                list = (LinkedList<Integer>) ois.readObject();
                ois.close();
            } else {
                list = new LinkedList<>();
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            File file = new File("recents.dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void add(int workOrderId) {
        if (list.contains(workOrderId)) {
            list.removeFirstOccurrence(workOrderId);
        }
        list.addFirst(workOrderId);
        if (list.size() > SIZE) {
            list.removeLast();
        }
    }

    public void remove(int workOrderId) {
        list.removeFirstOccurrence(workOrderId);
    }

    public Iterator<Integer> iterator() {
        return list.iterator();
    }
}
