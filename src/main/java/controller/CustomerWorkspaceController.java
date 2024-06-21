package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.State;
import model.customer.Address;
import model.customer.Customer;
import model.database.DB;
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
        var success = DB.get().customers().add(customer);
        if (success)
            App.get().log("Created Customer: " + customer);
        else
            App.get().log("Failed to create customer: there already exists a customer with the given information");
//        var n = Notifications.create();
//        if (Model.get().preferences().getTheme() == Theme.Dark) n = n.darkStyle();
//        if (success) { // Successfully created the customer
//            n.title("Created Customer").text(customer.toFormattedString());
//        } else { // Failed to create customer (duplicate or write issue)
//            n.title("Failed to Create Customer").text("There already exists a customer with the given information.");
//        }
//        n.showInformation();
    }
}
