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
import model.ui.FX;
import model.work_order.WorkOrder;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public class WorkOrderTableController {
    @FXML
    TextField tfFirstName, tfLastName, tfCompanyName;
    @FXML
    ComboBox<String> cbDateCreated;
    @FXML
    DatePicker dpBefore, dpAfter;
    @FXML
    TableView<WorkOrder> tv;
    @FXML
    TableColumn<WorkOrder, Integer> colId;
    @FXML
    TableColumn<WorkOrder, String> colCustomer;
    @FXML
    TableColumn<WorkOrder, String> colCompany;
    @FXML
    TableColumn<WorkOrder, String> colVehicle;
    @FXML
    TableColumn<WorkOrder, String> colDateCreated;
    @FXML
    TableColumn<WorkOrder, String> colDateCompleted;
    @FXML
    TableColumn<WorkOrder, String> colInvoiceTotal;
    @FXML
    Button btEdit, btDelete;

    public WorkOrderTableController() {
        Platform.runLater(() -> {
            Function<MouseEvent, Boolean> selectWorkOrder = x -> x.getClickCount() == 2 && x.getButton().equals(MouseButton.PRIMARY);

            tfFirstName.textProperty().addListener((o, oldText, newText) -> filter());
            tfLastName.textProperty().addListener((o, oldText, newText) -> filter());
            tfCompanyName.textProperty().addListener((o, oldText, newText) -> filter());
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
            tv.getItems().setAll(DB.get().workOrders().getAll());
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
        });
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
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String company = tfCompanyName.getText();
        String dateFilter = cbDateCreated.getValue();
        LocalDate localDate1 = dpBefore.getValue();
        LocalDate localDate2 = dpAfter.getValue();
        if (cbDateCreated.getValue() == null || localDate1 == null) {
            var list = DB.get().workOrders().filter(firstName, lastName, company);
            tv.getItems().setAll(list);
        } else {
            Date date1 = Date.valueOf(localDate1);
            if (cbDateCreated.getValue().equals("Between") && localDate2 != null) {
                Date date2 = Date.valueOf(localDate2);
                var list = DB.get().workOrders().filter(firstName, lastName, company, date1, date2);
                tv.getItems().setAll(list);
            } else {
                var list = DB.get().workOrders().filter(firstName, lastName, company, dateFilter, date1);
                tv.getItems().setAll(list);
            }
        }
    }
}
