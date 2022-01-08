package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private int id;
    private String firstName, lastName, phone, email, company;
    private Address address;

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
        return new SimpleStringProperty(firstName + " " + lastName);
    }

    public SimpleStringProperty phoneProperty() {
        return new SimpleStringProperty(phone);
    }

    public SimpleStringProperty emailProperty() { return new SimpleStringProperty(email); }

    public SimpleStringProperty companyProperty() {
        return new SimpleStringProperty(company);
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
}
