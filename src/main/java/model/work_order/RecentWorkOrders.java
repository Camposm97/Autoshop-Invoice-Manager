package model.work_order;

import model.Observable;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class RecentWorkOrders {
    private static final String RECENTS_DAT = "./recents.dat";
    private static final int SIZE = 10;
    private LinkedList<Integer> workOrderIds;
    private LinkedList<Observable> observables;

    public RecentWorkOrders() {
        try {
            File file = new File(RECENTS_DAT);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                workOrderIds = (LinkedList<Integer>) ois.readObject();
                ois.close();
                System.out.println("Loaded " + RECENTS_DAT);
            } else {
                System.out.println("Missing " + RECENTS_DAT);
                workOrderIds = new LinkedList<>();
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            observables = new LinkedList<>();
        }
    }

    public void save() {
        try {
            File file = new File(RECENTS_DAT);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(workOrderIds);
            oos.close();
            System.out.println("Saved " + RECENTS_DAT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void add(int workOrderId) {
        if (workOrderIds.contains(workOrderId)) {
            workOrderIds.removeFirstOccurrence(workOrderId);
        }
        workOrderIds.addFirst(workOrderId);
        if (workOrderIds.size() > SIZE) {
            workOrderIds.removeLast();
        }
        for (Observable o : observables) o.update();
    }

    public void remove(int workOrderId) {
        workOrderIds.removeFirstOccurrence(workOrderId);
        for (Observable o : observables) o.update();
    }

    public Iterator<Integer> iterator() {
        return workOrderIds.iterator();
    }

    public void addObserver(Observable o) {
        observables.add(o);
    }

    public void removeObserver(Observable o) {
        observables.remove(o);
    }
}
