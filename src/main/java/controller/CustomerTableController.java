package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Customer;
import model.DB;
import model.FX;

import java.awt.*;
import java.util.Optional;

public class CustomerTableController {
    @FXML
    TableView<Customer> tv;
    @FXML
    TableColumn<Customer, Integer> colId;
    @FXML
    TableColumn<Customer, String> colFirstName;
    @FXML
    TableColumn<Customer, String> colLastName;
    @FXML
    TableColumn<Customer, String> colPhone;
    @FXML
    TableColumn<Customer, String> colCompany;
    @FXML
    TableColumn<Customer, String> colAddress;
    @FXML
    TableColumn<Customer, String> colCity;
    @FXML
    TableColumn<Customer, String> colState;
    @FXML
    TableColumn<Customer, String> colZip;

    public CustomerTableController() {
        Platform.runLater(() -> {
            colId.setCellValueFactory(c -> c.getValue().idProperty());
            colFirstName.setCellValueFactory(c -> c.getValue().firstNameProperty());
            colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
            colFirstName.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.setFirstName(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colLastName.setCellValueFactory(c -> c.getValue().lastNameProperty());
            colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
            colLastName.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.setLastName(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colPhone.setCellValueFactory(c -> c.getValue().phoneProperty());
            colPhone.setCellFactory(TextFieldTableCell.forTableColumn());
            colPhone.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.setPhone(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colCompany.setCellValueFactory(c -> c.getValue().companyProperty());
            colCompany.setCellFactory(TextFieldTableCell.forTableColumn());
            colCompany.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.setCompany(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colAddress.setCellValueFactory(c -> c.getValue().getAddress().streetProperty());
            colAddress.setCellFactory(TextFieldTableCell.forTableColumn());
            colAddress.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.getAddress().setStreet(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colCity.setCellValueFactory(c -> c.getValue().getAddress().cityProperty());
            colCity.setCellFactory(TextFieldTableCell.forTableColumn());
            colCity.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.getAddress().setCity(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colState.setCellValueFactory(c -> c.getValue().getAddress().stateProperty());
            colState.setCellFactory(TextFieldTableCell.forTableColumn());
            colState.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.getAddress().setState(e.getNewValue());
                DB.get().updateCustomer(customer);
            });
            colZip.setCellValueFactory(c -> c.getValue().getAddress().zipProperty());
            colZip.setCellFactory(TextFieldTableCell.forTableColumn());
            colZip.setOnEditCommit(e -> {
                int index = e.getTablePosition().getRow();
                Customer customer = e.getTableView().getItems().get(index);
                customer.getAddress().setZip(e.getNewValue());
                DB.get().updateCustomer(customer);
            });

            tv.getItems().setAll(DB.get().getAllCustomers());
            ContextMenu cm = initContextMenu();
            tv.setOnContextMenuRequested(e -> {
                if (tv.getSelectionModel().getSelectedItem() != null) {
                    cm.show(tv.getScene().getWindow(), MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
                }
            });
        });
    }

    public ContextMenu initContextMenu() {
//        MenuItem miEdit = new MenuItem("Edit");
//        miEdit.setOnAction(new EditCustomerHandler());
        MenuItem miDelete = new MenuItem("Delete");
        miDelete.setOnAction(new DeleteCustomerHandler());
        ContextMenu cm = new ContextMenu();
//        cm.getItems().addAll(miEdit, miDelete);
        cm.getItems().add(miDelete);
        return cm;
    }

//    private class EditCustomerHandler implements EventHandler<ActionEvent> {
//        @Override
//        public void handle(ActionEvent e) {
//            Customer customer = tv.getSelectionModel().getSelectedItem();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Update Customer");
//            alert.setHeaderText("Please enter the following information:");
//            ButtonType bt = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
//            alert.getButtonTypes().setAll(bt);
//
//            CustomerController controller = new CustomerController(customer);
//            alert.getDialogPane().setContent(FX.view("Customer.fxml", controller));
//
//            Optional<ButtonType> rs = alert.showAndWait();
//            rs.ifPresent(action -> controller.updateCustomer());
//        }
//    }

    private class DeleteCustomerHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Customer customer = tv.getSelectionModel().getSelectedItem();
            DB.get().deleteCustomerById(customer.getId());
            tv.getItems().removeIf(c -> c.getId() == customer.getId());
        }
    }
}
