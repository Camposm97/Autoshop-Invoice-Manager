package model.work_order;

import model.Observable;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class RecentWorkOrders {
    private static final int SIZE = 10;
    private LinkedList<Integer> integers;
    private LinkedList<Observable> observables;

    public RecentWorkOrders() {
        try {
            File file = new File("./recents.dat");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                integers = (LinkedList<Integer>) ois.readObject();
                ois.close();
            } else {
                integers = new LinkedList<>();
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            observables = new LinkedList<>();
        }
    }

    public void save() {
        try {
            File file = new File("recents.dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(integers);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void add(int workOrderId) {
        if (integers.contains(workOrderId)) {
            integers.removeFirstOccurrence(workOrderId);
        }
        integers.addFirst(workOrderId);
        if (integers.size() > SIZE) {
            integers.removeLast();
        }
        for (Observable o : observables) o.update();
    }

    public void remove(int workOrderId) {
        integers.removeFirstOccurrence(workOrderId);
        for (Observable o : observables) o.update();
    }

    public Iterator<Integer> iterator() {
        return integers.iterator();
    }

    public void addObserver(Observable o) {
        observables.add(o);
    }

    public void removeObserver(Observable o) {
        observables.remove(o);
    }
}
