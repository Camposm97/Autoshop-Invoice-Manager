package controller;

import app.App;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.database.DB;
import model.ui.AlertBuilder;
import model.ui.ChangeListenerFactory;
import model.ui.FX;
import model.work_order.WorkOrder;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public class WorkOrderTableController {
    @FXML
    TextField tfId, tfName, tfVehicle;
    @FXML
    ComboBox<String> cbDateCreated;
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
        tfId.textProperty().addListener((o,x,y) -> filter());
        tfName.textProperty().addListener((o, oldText, newText) -> filter());
        cbDateCreated.setItems(FXCollections.observableArrayList("Exactly", "Before", "After", "Between"));
        cbDateCreated.setOnAction(e -> {
            if (cbDateCreated.getValue().equals("Between")) {
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
        String strId = tfId.getText();
        int id = -1;
        if (!strId.isEmpty())
            id = Integer.parseInt(strId);
        String[] nameTokens = tfName.getText().trim().split(REGEX);
        String[] vehicleTokens = tfVehicle.getText().trim().split(REGEX);
        try {
            tv.getItems().setAll(DB.get().workOrders().filter(id, nameTokens, vehicleTokens));
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        String dateFilter = cbDateCreated.getValue();
//        LocalDate localDate1 = dpBefore.getValue();
//        LocalDate localDate2 = dpAfter.getValue();
//        if (cbDateCreated.getValue() == null || localDate1 == null) {
//            var list = DB.get().workOrders().filter(id, nameTokens, vehicleTokens);
//            tv.getItems().setAll(list);
//        } else {
//            Date date1 = Date.valueOf(localDate1);
//            if (cbDateCreated.getValue().equals("Between") && localDate2 != null) {
//                Date date2 = Date.valueOf(localDate2);
//                var list = DB.get().workOrders().filter(id, nameTokens, vehicleTokens, date1, date2);
//                tv.getItems().setAll(list);
//            } else {
//                var list = DB.get().workOrders().filter(id, nameTokens, vehicleTokens, dateFilter, date1);
//                tv.getItems().setAll(list);
//            }
//        }
    }
}
