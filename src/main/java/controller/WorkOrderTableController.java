package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.DateFilter;
import model.customer.Customer;
import model.database.DB;
import model.ui.*;
import model.work_order.WorkOrder;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

public class WorkOrderTableController implements IOffsets {
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
    HBox controlBox;
    @FXML
    Button btEdit, btDelete, btAll;

    private boolean isEmbedded; /* decides whether when the program filters work orders when user types in name or company field */

    @FXML
    public void initialize() {
        Function<MouseEvent, Boolean> doubleClick = x -> x.getClickCount() == 2 && x.getButton().equals(MouseButton.PRIMARY);
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
            if (!isEmbedded) {
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

        TextFields.bindAutoCompletion(tfMake, DB.get().vehicles().getUniqueMake());
        TextFields.bindAutoCompletion(tfModel, DB.get().vehicles().getUniqueModel());

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
        colDateCreated.comparatorProperty().set((s1, s2) -> {
            var d1 = LocalDate.parse(s1, DateTimeFormatter.ofPattern("MM/dd/u"));
            var d2 = LocalDate.parse(s2, DateTimeFormatter.ofPattern("MM/dd/u"));
            return d1.compareTo(d2);
        });
        colDateCompleted.setCellValueFactory(c -> c.getValue().dateCompletedProperty());
        colDateCompleted.comparatorProperty().set((s1, s2) -> {
            if (s1 == null) return (s2 == null) ? 0 : -1;
            if (s2 == null) return 1;
            var d1 = LocalDate.parse(s1, DateTimeFormatter.ofPattern("MM/dd/u"));
            var d2 = LocalDate.parse(s2, DateTimeFormatter.ofPattern("MM/dd/u"));
            return d1.compareTo(d2);
        });
        colInvoiceTotal.setCellValueFactory(c -> c.getValue().billProperty());
        colInvoiceTotal.comparatorProperty().set((s1, s2) -> {
            if (s1 == null) return (s2 == null) ? 0 : -1;
            if (s2 == null) return 1;
            var d1 = Double.parseDouble(s1.substring(1));
            var d2 = Double.parseDouble(s2.substring(1));
            return Double.compare(d1, d2);
        });
        tv.getItems().setAll(DB.get().workOrders().getAll(50));
        tv.setOnMouseClicked(e -> {
            if (doubleClick.apply(e)) editWorkOrder();
            if (tv.getSelectionModel().getSelectedItem() != null) {
                btEdit.setDisable(false);
                btDelete.setDisable(false);
            } else {
                btEdit.setDisable(true);
                btDelete.setDisable(true);
            }
        });
        FX.autoResizeColumns(tv, WO_OFFSET);
    }

    public void fetchAllWorkOrders() {
        tv.setItems(DB.get().workOrders().getAll(0));
    }

    public void editWorkOrder() {
        WorkOrder workOrder = tv.getSelectionModel().getSelectedItem();
        if (workOrder != null) App.get().showWorkOrder(workOrder);
    }

    public void deleteWorkOrder() {
        WorkOrder x = tv.getSelectionModel().getSelectedItem();
        DialogFactory.initDeleteWorkOrder(tv, x);
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

    public void embed() {
        isEmbedded = true;
        controlBox.getChildren().remove(btAll);
    }

    public void clear() {
        tv.getItems().clear();
        btEdit.setDisable(true);
        btDelete.setDisable(true);
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
