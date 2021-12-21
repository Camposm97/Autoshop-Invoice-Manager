package main.model;

public class Customer {
    private int id;
    private String firstName, lastName, phone, company;
    private Address address;

    public Customer(String firstName, String lastName, String phone, String company, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.company = company;
        this.address = address;
    }

    public Customer(int id, String firstName, String lastName, String phone, String company, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
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
}
