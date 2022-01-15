package model.database;

import model.customer.Address;
import model.customer.Customer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CustomerStore {
    private Connection c;
    private Statement stmt;

    protected CustomerStore(@NotNull Connection c) throws SQLException {
        this.c = c;
        this.stmt = c.createStatement();
    }

    public void add(@NotNull Customer customer) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into customer " +
                            "(first_name, last_name, phone, email, company, street, city, state, zip)" +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prepStmt.setString(1, customer.getFirstName());
            prepStmt.setString(2, customer.getLastName());
            prepStmt.setString(3, customer.getPhone());
            prepStmt.setString(4, customer.getEmail());
            prepStmt.setString(5, customer.getCompany());
            prepStmt.setString(6, customer.getAddress().getStreet());
            prepStmt.setString(7, customer.getAddress().getCity());
            prepStmt.setString(8, customer.getAddress().getState());
            prepStmt.setString(9, customer.getAddress().getZip());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getById(int id) {
        Customer customer = null;
        try {
            ResultSet rs = stmt.executeQuery("select * from customer where customer_id = " + id);
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String company = rs.getString("company");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");
                Address address = new Address(street, city, state, zip);
                customer = new Customer(id, firstName, lastName, phone, email, company, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return customer;
        }
    }

    public List<Customer> getAll() {
        List<Customer> list = new LinkedList<>();
        try {
            ResultSet rs = stmt.executeQuery("select customer_id from customer;");
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                Customer cus = getById(id);
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public void update(@NotNull Customer customer) {
        try {
            PreparedStatement prepStmt = c.prepareStatement("update customer set " +
                    "first_name = ?, last_name = ?, phone = ?, email = ?, company = ?, " +
                    "street = ?, city = ?, state = ?, zip = ?" +
                    "where customer_id=" + customer.getId());
            prepStmt.setString(1, customer.getFirstName());
            prepStmt.setString(2, customer.getLastName());
            prepStmt.setString(3, customer.getPhone());
            prepStmt.setString(4, customer.getEmail());
            prepStmt.setString(5, customer.getCompany());
            prepStmt.setString(6, customer.getAddress().getStreet());
            prepStmt.setString(7, customer.getAddress().getCity());
            prepStmt.setString(8, customer.getAddress().getState());
            prepStmt.setString(9, customer.getAddress().getZip());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            stmt.execute("delete from customer where customer_id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getFiltered(@NotNull Customer c) {
        List<Customer> list = new LinkedList<>();
        try {
            ResultSet rs = stmt.executeQuery(
                    "select customer_id from customer " +
                            "where first_name like \"" + c.getFirstName() + "%\"" +
                            "and last_name like \"" + c.getLastName() + "%\"" +
                            "and company like \"" + c.getCompany() + "%\"" +
                            "and phone like \"" + c.getPhone() + "%\"" +
                            "and email like \"" + c.getEmail() + "%\"" +
                            "and street like \"" + c.getAddress().getStreet() + "%\"" +
                            "and city like \"" + c.getAddress().getCity() + "%\"" +
                            "and state like \"" + c.getAddress().getState() + "%\"" +
                            "and zip like \"" + c.getAddress().getZip() + "%\"");
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                Customer cus = getById(id);
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getUniqueStreets() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = stmt.executeQuery("select distinct street from customer order by street");
            while (rs.next()) {
                String street = rs.getString(1);
                list.add(street);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getUniqueCities() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = stmt.executeQuery("select distinct city from customer order by city");
            while (rs.next()) {
                String street = rs.getString(1);
                list.add(street);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getUniqueZips() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = stmt.executeQuery("select distinct zip from customer order by zip");
            while (rs.next()) {
                String street = rs.getString(1);
                list.add(street);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
