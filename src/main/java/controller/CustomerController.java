package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Address;
import model.Customer;
import model.DB;

public class CustomerController {
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfCompany, tfStreet, tfCity, tfState, tfZip;

    public void addCustomer() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String company = tfCompany.getText();
        String street = tfStreet.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String zip = tfZip.getText();

        Address address = new Address(street, city, state, zip);
        Customer customer = new Customer(firstName, lastName, phone, company, address);
        DB.get().addCustomer(customer);
    }

//    public CustomerController(Customer customer) {
//        this.id = customer.getId();
//        Platform.runLater(() -> {
//            tfFirstName.setText(customer.getFirstName());
//            tfLastName.setText(customer.getLastName());
//            tfPhone.setText(customer.getPhone());
//            tfCompany.setText(customer.getCompany());
//            tfStreet.setText(customer.getAddress().getStreet());
//            tfCity.setText(customer.getAddress().getCity());
//            tfState.setText(customer.getAddress().getState());
//            tfZip.setText(customer.getAddress().getZip());
//        });
//    }

//    public void updateCustomer() {
//        String firstName = tfFirstName.getText();
//        String lastName = tfLastName.getText();
//        String phone = tfPhone.getText();
//        String company = tfCompany.getText();
//        String street = tfStreet.getText();
//        String city = tfCity.getText();
//        String state = tfState.getText();
//        String zip = tfZip.getText();
//
//        Address address = new Address(street, city, state, zip);
//        Customer customer = new Customer(id, firstName, lastName, phone, company, address);
//        DB.get().updateCustomer(customer);
//    }
}
