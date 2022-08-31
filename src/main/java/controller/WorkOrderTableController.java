package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.DateFilter;
import model.database.DB;
import model.ui.AlertBuilder;
import model.ui.ChangeListenerFactory;
import model.ui.FX;
import model.work_order.WorkOrder;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Function;

public class WorkOrderTableController {
    @FXML
    TextField tfId, tfName, tfVehicle;
    @FXML
    ComboBox<DateFilter> cbDateFilter;
    @FXML
    DatePicker dpBefore, dpAfter;
    @FXML
    TableView<WorkOrder> tv;
    @FXML
    TableColumn<WorkOrder, Integer> colId;
    @FXML
    TableColumn<WorkOrder, String> colCustomer, colCompany, colVehicle, colDateCreated, colDateCompleted, colInvoiceTotal;
    @FXML
    Button btEdit, btDelete;

    @FXML
    public void initialize() {
        ChangeListenerFactory factory = new ChangeListenerFactory();
        factory.initIntFormat(tfId);
        Function<MouseEvent, Boolean> selectWorkOrder = x -> x.getClickCount() == 2 && x.getButton().equals(MouseButton.PRIMARY);
        tfId.textProperty().addListener((o,x,y) -> {
            tfName.clear();
            tfVehicle.clear();
            filter();
        });
        tfName.textProperty().addListener((o, x, y) -> {
            tfId.clear();
            filter();
        });
        tfVehicle.textProperty().addListener((o,x,y) -> {
            tfId.clear();
            filter();
        });
        cbDateFilter.setItems(FXCollections.observableArrayList(DateFilter.values()));
        cbDateFilter.setOnAction(e -> {
            if (cbDateFilter.getValue().equals(DateFilter.Between)) {
                dpAfter.setDisable(false);
            } else {
                dpAfter.setDisable(true);
            }
            filter();
        });
        dpBefore.setOnAction(e -> filter());
        dpAfter.setOnAction(e -> filter());
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colCustomer.setCellValueFactory(c -> c.getValue().getCustomer().nameProperty());
        colCompany.setCellValueFactory(c -> c.getValue().getCustomer().companyProperty());
        colVehicle.setCellValueFactory(c -> c.getValue().vehicleProperty());
        colDateCreated.setCellValueFactory(c -> c.getValue().dateCreatedProperty());
        colDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
        colInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
        tv.getItems().setAll(DB.get().workOrders().getAll(50));
        tv.setOnMouseClicked(e -> {
            if (selectWorkOrder.apply(e)) editWorkOrder();
            if (tv.getSelectionModel().getSelectedItem() != null) {
                btEdit.setDisable(false);
                btDelete.setDisable(false);
            } else {
                btEdit.setDisable(true);
                btDelete.setDisable(true);
            }
        });
        FX.autoResizeColumns(tv,75);
    }

    public void editWorkOrder() {
        WorkOrder workOrder = tv.getSelectionModel().getSelectedItem();
        if (workOrder != null) {
            try {
                FXMLLoader loader = FX.load("WorkOrderWorkspace.fxml");
                Parent node = loader.load();
                WorkOrderWorkspaceController c = loader.getController();
                c.loadWorkOrder(workOrder);
                App.display(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteWorkOrder() {
        WorkOrder x = tv.getSelectionModel().getSelectedItem();
        AlertBuilder builder = new AlertBuilder();
        builder.setAlertType(Alert.AlertType.CONFIRMATION)
                .setTitle("Delete Work Order #" + x.getId())
                .setHeaderText("Are you sure you want to delete work order #" + x.getId() + "?")
                .setContentText(x.toFormattedString())
                .setYesNoBtns();
        Alert alert = builder.build();
        Optional<ButtonType> rs = alert.showAndWait();
        rs.ifPresent(e -> {
            if (!e.getButtonData().isCancelButton()) {
                tv.getItems().remove(x);
                DB.get().workOrders().delete(x);
            }
        });
    }

    public void filter() {
        final String REGEX = "\\s+";
        var strId = tfId.getText();
        var id = -1;
        if (!strId.isEmpty()) id = Integer.parseInt(strId);
        var s1 = tfName.getText();
        var s2 = tfVehicle.getText();
        var arr1 = s1.trim().isEmpty() ? null : s1.trim().split(REGEX);
        var arr2 = s2.trim().isEmpty() ? null : s2.trim().split(REGEX);
        var dateFilter = cbDateFilter.getValue();
        var date1 = dpBefore.getValue();
        var date2 = dpAfter.getValue();
        try {
            tv.getItems().setAll(DB.get().workOrders().filter(id, arr1, arr2, dateFilter, date1, date2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
