package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.customer.Address;
import model.customer.Customer;
import model.database.DB;
import model.State;
import model.ui.ChangeListenerFactory;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.textfield.TextFields;

public class CustomerWorkspaceController {
    @FXML
    TextField tfFirstName, tfLastName, tfPhone, tfEmail, tfCompany, tfStreet, tfCity, tfZip;
    @FXML
    SearchableComboBox<State> cbState;

    @FXML
    public void initialize() {
        ChangeListenerFactory f = new ChangeListenerFactory();
        f.setAlphaNums(tfFirstName);
        f.setAlphaNums(tfLastName);
        TextFields.bindAutoCompletion(tfCompany, DB.get().customers().getUniqueCompanies());
        TextFields.bindAutoCompletion(tfStreet, DB.get().customers().getUniqueAddresses());
        TextFields.bindAutoCompletion(tfCity, DB.get().customers().getUniqueCities());
        cbState.setValue(State.UNKNOWN);
        cbState.setItems(State.list());
        TextFields.bindAutoCompletion(tfZip, DB.get().customers().getUniqueZips());
    }

    public void addCustomer() {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String phone = tfPhone.getText();
        String email = tfEmail.getText();
        String company = tfCompany.getText();
        String street = tfStreet.getText();
        String city = tfCity.getText();
        State state = cbState.getValue();
        if (state == null) state = State.UNKNOWN;
        String zip = tfZip.getText();

        Address address = new Address(street, city, state.name(), zip);
        Customer customer = new Customer(firstName, lastName, phone, email, company, address);
        DB.get().customers().add(customer);
    }
}
