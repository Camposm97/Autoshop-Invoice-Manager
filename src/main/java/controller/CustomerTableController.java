package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import model.customer.Address;
import model.customer.Customer;
import model.database.DB;
import model.ui.FX;

import java.awt.*;

public class CustomerTableController {
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfEmail, tfCompany, tfStreet, tfCity, tfState, tfZip;
    @FXML
    TableView<Customer> tv;
    @FXML
    TableColumn<Customer, String> colFirstName, colLastName, colPhone, colEmail, colCompany, colAddress, colCity, colState, colZip;

    @FXML
    public void initialize() {
        tfFirstName.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfLastName.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfPhone.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfEmail.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfCompany.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfStreet.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfCity.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfState.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        tfZip.textProperty().addListener((o, oldValue, newValue) -> tv.getItems().setAll(DB.get().customers().filter(buildCustomer())));
        colFirstName.setCellValueFactory(c -> c.getValue().firstNameProperty());
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.setFirstName(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colLastName.setCellValueFactory(c -> c.getValue().lastNameProperty());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.setLastName(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colPhone.setCellValueFactory(c -> c.getValue().phoneProperty());
        colPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhone.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.setPhone(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colEmail.setCellValueFactory(c -> c.getValue().emailProperty());
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.setEmail(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colCompany.setCellValueFactory(c -> c.getValue().companyProperty());
        colCompany.setCellFactory(TextFieldTableCell.forTableColumn());
        colCompany.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.setCompany(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colAddress.setCellValueFactory(c -> c.getValue().getAddress().streetProperty());
        colAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        colAddress.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.getAddress().setStreet(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colCity.setCellValueFactory(c -> c.getValue().getAddress().cityProperty());
        colCity.setCellFactory(TextFieldTableCell.forTableColumn());
        colCity.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.getAddress().setCity(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colState.setCellValueFactory(c -> c.getValue().getAddress().stateProperty());
        colState.setCellFactory(TextFieldTableCell.forTableColumn());
        colState.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.getAddress().setState(e.getNewValue());
            DB.get().customers().update(customer);
        });
        colZip.setCellValueFactory(c -> c.getValue().getAddress().zipProperty());
        colZip.setCellFactory(TextFieldTableCell.forTableColumn());
        colZip.setOnEditCommit(e -> {
            int index = e.getTablePosition().getRow();
            Customer customer = e.getTableView().getItems().get(index);
            customer.getAddress().setZip(e.getNewValue());
            DB.get().customers().update(customer);
        });
        tv.getItems().setAll(DB.get().customers().getAll());
        FX.autoResizeColumns(tv);
        ContextMenu cm = initContextMenu();
        tv.setOnContextMenuRequested(e -> {
            if (tv.getSelectionModel().getSelectedItem() != null) {
                cm.show(tv.getScene().getWindow(), MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
            }
        });
    }

    public Customer buildCustomer() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String company = tfCompany.getText();
        String street = tfStreet.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String zip = tfZip.getText();
        Address address = new Address(street, city, state, zip);
       return new Customer(firstName, lastName, phone, email, company, address);
    }

    public Customer getSelectedCustomer() {
        return tv.getSelectionModel().getSelectedItem();
    }

    public ContextMenu initContextMenu() {
        MenuItem miDelete = new MenuItem("Delete");
        miDelete.setOnAction(new DeleteCustomerHandler());
        ContextMenu cm = new ContextMenu();
        cm.getItems().add(miDelete);
        return cm;
    }

    private class DeleteCustomerHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Customer customer = tv.getSelectionModel().getSelectedItem();
            DB.get().customers().deleteById(customer.getId());
            tv.getItems().removeIf(c -> c.getId() == customer.getId());
        }
    }
}
