package controller;

import javafx.fxml.FXML;
import model.database.DB;

/**
 *
 */
public class MyCompanyController {
    @FXML
    public void initialize() {
        int count = DB.get().workOrders().getUncompletedWorkOrderCount();
        System.out.println("uncompleted work orders: " + count);
    }
}
