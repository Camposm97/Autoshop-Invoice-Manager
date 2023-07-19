package model.customer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Customer implements Comparable<Customer> {
    private int id;
    private String firstName, lastName, phone, email, company;
    private Address address;

    public Customer() { /* empty constructor */
        this.firstName = "";
        this.lastName = "";
        this.phone = "";
        this.email = "";
        this.company = "";
        this.address = new Address("", "", "", "");
    }

    public Customer(String firstName, String lastName, String phone, String email, String company, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.address = address;
    }

    public Customer(int id, String firstName, String lastName, String phone, String email, String company, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public SimpleObjectProperty<Integer> idProperty() {
        return new SimpleObjectProperty<>(id);
    }

    public SimpleStringProperty firstNameProperty() {
        return new SimpleStringProperty(firstName);
    }

    public SimpleStringProperty lastNameProperty() {
        return new SimpleStringProperty(lastName);
    }

    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(getName());
    }

    public SimpleStringProperty phoneProperty() {
        return new SimpleStringProperty(phone);
    }

    public SimpleStringProperty emailProperty() { return new SimpleStringProperty(email); }

    public SimpleStringProperty companyProperty() {
        return new SimpleStringProperty(company);
    }

    /**
     * @return all info of customer in a formatted table except customer id.
     */
    public String toFormattedString() {
        Function<String, Boolean> f = x -> x != null && !x.isEmpty() && !x.isBlank();
        var name = String.format("%s\n", this.getName());
        var phone = String.format("%s\n", this.phone);
        var email = String.format("%s\n", this.email);
        var company = String.format("%s\n", this.company);
        var addr = String.format("%s\n", this.getAddress());
        StringBuilder sb = new StringBuilder();
        if (f.apply(name))
            sb.append(name);
        if (f.apply(phone))
            sb.append(phone);
        if (f.apply(email))
            sb.append(email);
        if (f.apply(company))
            sb.append(company);
        if (f.apply(addr))
            sb.append(addr);
        return sb.toString();
    }

    /**
     * @return first and last name of customer if no company name given, otherwise company name is returned
     */
    public String toPrettyString() {
        if (company.isBlank()) {
            return firstName + " " + lastName;
        } else {
            return company;
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", address=" + address +
                '}';
    }

    @Override
    public int compareTo(@NotNull Customer o) {
        return Integer.compare(id, o.id);
    }
}
