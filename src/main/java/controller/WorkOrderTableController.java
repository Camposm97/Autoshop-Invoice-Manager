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
import model.customer.Customer;
import model.database.DB;
import model.ui.AlertBuilder;
import model.ui.ChangeListenerFactory;
import model.ui.FX;
import model.work_order.WorkOrder;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class WorkOrderTableController {
    @FXML
    TextField tfId, tfFirst, tfLast, tfComp, tfYear, tfMake, tfModel;
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
    boolean embedded; // decides whether when the program filters work orders when user types in name or company field

    @FXML
    public void initialize() {
        Function<MouseEvent, Boolean> selectWorkOrder = x -> x.getClickCount() == 2 && x.getButton().equals(MouseButton.PRIMARY);
        ChangeListenerFactory factory = new ChangeListenerFactory();
        factory.setPositiveNums(tfId);
        Runnable r0 = () -> {
            tfFirst.clear();
            tfLast.clear();
            tfComp.clear();
            tfYear.clear();
            tfMake.clear();
            tfModel.clear();
            filter();
        };
        Runnable r1 = () -> {
            if (!embedded) {
                tfId.clear();
                filter();
            }
        };
        Runnable r2 = () -> {
            tfId.clear();
            filter();
        };
        factory.setTimer(tfId, r0);
        factory.setTimer(tfFirst, r1);
        factory.setTimer(tfLast, r1);
        factory.setTimer(tfComp, r1);
        factory.setTimer(tfYear, r2);
        factory.setTimer(tfMake, r2);
        factory.setTimer(tfModel, r2);

        TextFields.bindAutoCompletion(tfYear, DB.get().vehicles().getUniqueYear());
        TextFields.bindAutoCompletion(tfMake, DB.get().vehicles().getUniqueMake());
        TextFields.bindAutoCompletion(tfModel, DB.get().vehicles().getUniqueModel());

        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Show Complete List");
        mi1.setOnAction(e -> tv.setItems(DB.get().workOrders().getAll(0)));
        cm.getItems().add(mi1);
        tv.setContextMenu(cm);

        cbDateFilter.setItems(FXCollections.observableArrayList(DateFilter.values()));
        cbDateFilter.setValue(DateFilter.None);
        cbDateFilter.setOnAction(e -> {
            if (cbDateFilter.getValue().equals(DateFilter.Between))
                dpAfter.setDisable(false);
            else
                dpAfter.setDisable(true);
            tfId.clear();
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
                App.get().display(node);
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
        var strId = tfId.getText();
        var id = -1;
        if (!strId.isEmpty()) id = Integer.parseInt(strId);
        var s1 = tfFirst.getText();
        var s2 = tfLast.getText();
        var s3 = tfComp.getText();
        var s4 = tfYear.getText();
        var s5 = tfMake.getText();
        var s6 = tfModel.getText();
        var dateFilter = cbDateFilter.getValue();
        var date1 = dpBefore.getValue();
        var date2 = dpAfter.getValue();
        try {
            tv.getItems().setAll(DB.get().workOrders().filter(id, s1, s2, s3, s4, s5, s6, dateFilter, date1, date2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        tv.getItems().clear();
    }

    public void load(Customer c) {
        var workOrders = DB.get().workOrders().getByCustomerId(c.getId());
        tfFirst.setText(c.getFirstName());
        tfLast.setText(c.getLastName());
        tfComp.setText(c.getCompany());
        tv.setItems(workOrders);
    }

    public void disableFields() {
        tfId.setDisable(true);
        tfFirst.setDisable(true);
        tfLast.setDisable(true);
        tfComp.setDisable(true);
    }
}
