package controller;

import app.App;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.DB;
import model.FX;
import model.WorkOrder;

import java.sql.Date;

public class WorkOrderTableController {
    @FXML
    TextField tfFirstName, tfLastName, tfCompanyName;
    @FXML
    ComboBox<String> cbDateCreated;
    @FXML
    DatePicker dateCreatedPicker;
    @FXML
    TableView<WorkOrder> tv;
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
    TableColumn<WorkOrder, String> colInvoiceTotal;

    public WorkOrderTableController() {
        Platform.runLater(() -> {
            tfFirstName.textProperty().addListener((o, oldText, newText) -> { // TODO

            });
            tfLastName.textProperty().addListener((o, oldText, newText) -> { // TODO

            });
            tfCompanyName.textProperty().addListener((o, oldText, newText) -> { // TODO

            });
            cbDateCreated.setItems(FXCollections.observableArrayList("Exactly", "Before", "After"));
            cbDateCreated.setValue("Exactly");
            dateCreatedPicker.setOnAction(e -> { // TODO
                Date date = Date.valueOf(dateCreatedPicker.getValue());
                System.out.println(cbDateCreated.getValue() + " " + date);

            });
            colId.setCellValueFactory(c -> c.getValue().idProperty());
            colCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
            colCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
            colDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
            colDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
            colInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
            tv.getItems().setAll(DB.get().getAllWorkOrders());
        });
    }

    public void editWorkOrder() {
        WorkOrder workOrder = tv.getSelectionModel().getSelectedItem();
        if (workOrder != null) {
            WorkOrderWorkspaceController controller = new WorkOrderWorkspaceController(workOrder);
            Parent node = FX.view("Work_Order_Workspace.fxml", controller);
            App.setDisplay(node);
        }
    }

    public void deleteWorkOrder() {
        WorkOrder workOrder = tv.getSelectionModel().getSelectedItem();
        if (workOrder != null) {
            tv.getItems().remove(workOrder);
            DB.get().deleteWorkOrder(workOrder);
        }
    }
}
