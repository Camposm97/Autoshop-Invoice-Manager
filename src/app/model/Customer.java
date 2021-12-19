package app.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private Name name;
    private String company;
    private Address address;

    public Customer(Name name, String company, Address address) {
        this.name = name;
        this.company = company;
        this.address = address;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
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
