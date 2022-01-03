package controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.WorkOrder;

import java.time.LocalDate;

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
    TableColumn<WorkOrder, LocalDate> colDateCreated;
    @FXML
    TableColumn<WorkOrder, LocalDate> colDateCompleted;
    @FXML
    TableColumn<WorkOrder, Double> colInvoiceTotal;

    public WorkOrderTableController() {
        Platform.runLater(() -> {
            colId.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getId()));
            colCustomer.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getCustomer().getName()));
            colCompany.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCustomer().getCompany()));
            colDateCreated.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDateCreated()));
            colDateCompleted.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDateCompleted()));
            colInvoiceTotal.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getInvoice().bill()));
        });
    }
}
