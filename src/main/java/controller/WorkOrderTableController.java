package controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.DB;
import model.WorkOrder;

import java.sql.Date;

public class WorkOrderTableController {
    @FXML
    TableView tv;
    @FXML
    TableColumn<WorkOrder, Integer> colId;
    @FXML
    TableColumn<WorkOrder, String> colCustomer;
    @FXML
    TableColumn<WorkOrder, String> colCompany;
    @FXML
    TableColumn<WorkOrder, Date> colDateCreated;
    @FXML
    TableColumn<WorkOrder, Date> colDateCompleted;
    @FXML
    TableColumn<WorkOrder, Double> colInvoiceTotal;

    public WorkOrderTableController() {
        Platform.runLater(() -> {
            colId.setCellValueFactory(c -> c.getValue().idProperty());
            colCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
            colCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
            colDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
            colDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
            colInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
            tv.getItems().setAll(DB.get().getAllWorkOrders());
        });
    }
}
