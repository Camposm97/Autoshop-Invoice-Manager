package model.database;

import model.customer.Address;
import model.customer.Customer;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static model.database.DBAttributes.CUSTOMER_TABLE;

public class CustomerStore {
    private Connection c;

    protected CustomerStore(@NotNull Connection c) throws SQLException {
        this.c = c;
    }

    /**
     * Checks if a customer exists in the database with the information given.
     * @param cus
     * @return true if all customer attributes matches an existing customer in database, otherwise false
     */
    public boolean exists(@NotNull Customer cus) {
        try {
            PreparedStatement prepStmt = c.prepareStatement("""
                    select %s from %s
                    where %s =  ? and %s = ?
                    and %s = ? and %s = ?
                    and %s = ? and %s = ?
                    and %s = ? and %s = ? and %s = ?
                    """.formatted(CUSTOMER_TABLE.CUSTOMER_ID, CUSTOMER_TABLE,
                    CUSTOMER_TABLE.FIRST_NAME, CUSTOMER_TABLE.LAST_NAME,
                    CUSTOMER_TABLE.PHONE, CUSTOMER_TABLE.EMAIL,
                    CUSTOMER_TABLE.COMPANY, CUSTOMER_TABLE.STREET,
                    CUSTOMER_TABLE.CITY, CUSTOMER_TABLE.STATE, CUSTOMER_TABLE.ZIP));
            prepStmt.setString(1, cus.getFirstName());
            prepStmt.setString(2, cus.getLastName());
            prepStmt.setString(3, cus.getPhone());
            prepStmt.setString(4, cus.getEmail());
            prepStmt.setString(5, cus.getCompany());
            prepStmt.setString(6, cus.getAddress().getStreet());
            prepStmt.setString(7, cus.getAddress().getCity());
            prepStmt.setString(8, cus.getAddress().getState());
            prepStmt.setString(9, cus.getAddress().getZip());
            ResultSet rs = prepStmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void add(@NotNull Customer cus) {
        try {
            if (!exists(cus)) {
                PreparedStatement prepStmt = c.prepareStatement(
                        "insert into " + CUSTOMER_TABLE + "(" +
                                CUSTOMER_TABLE.FIRST_NAME + "," + CUSTOMER_TABLE.LAST_NAME + "," +
                                CUSTOMER_TABLE.PHONE + "," + CUSTOMER_TABLE.EMAIL + "," + CUSTOMER_TABLE.COMPANY + "," +
                                CUSTOMER_TABLE.STREET + "," + CUSTOMER_TABLE.CITY + "," + CUSTOMER_TABLE.STATE + "," + CUSTOMER_TABLE.ZIP + ")" +
                                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                prepStmt.setString(1, cus.getFirstName());
                prepStmt.setString(2, cus.getLastName());
                prepStmt.setString(3, cus.getPhone());
                prepStmt.setString(4, cus.getEmail());
                prepStmt.setString(5, cus.getCompany());
                prepStmt.setString(6, cus.getAddress().getStreet());
                prepStmt.setString(7, cus.getAddress().getCity());
                prepStmt.setString(8, cus.getAddress().getState());
                prepStmt.setString(9, cus.getAddress().getZip());
                prepStmt.execute();
            } else {
                // Report this duplicate to user
                System.out.println("Duplicate customer profile!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getById(int id) {
        Customer customer = null;
        try {
            ResultSet rs = c.createStatement().executeQuery("select * from " + CUSTOMER_TABLE + " where " + CUSTOMER_TABLE.CUSTOMER_ID + " = " + id);
            if (rs.next()) {
                String firstName = rs.getString(CUSTOMER_TABLE.FIRST_NAME);
                String lastName = rs.getString(CUSTOMER_TABLE.LAST_NAME);
                String phone = rs.getString(CUSTOMER_TABLE.PHONE);
                String email = rs.getString(CUSTOMER_TABLE.EMAIL);
                String company = rs.getString(CUSTOMER_TABLE.COMPANY);
                String street = rs.getString(CUSTOMER_TABLE.STREET);
                String city = rs.getString(CUSTOMER_TABLE.CITY);
                String state = rs.getString(CUSTOMER_TABLE.STATE);
                String zip = rs.getString(CUSTOMER_TABLE.ZIP);
                Address address = new Address(street, city, state, zip);
                customer = new Customer(id, firstName, lastName, phone, email, company, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> getAll() {
        List<Customer> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select " + CUSTOMER_TABLE.CUSTOMER_ID + " from " + CUSTOMER_TABLE);
            while (rs.next()) {
                int id = rs.getInt(CUSTOMER_TABLE.CUSTOMER_ID);
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
            PreparedStatement prepStmt = c.prepareStatement("""
                    update %s set %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? where %s = %d
                    """.formatted(CUSTOMER_TABLE, CUSTOMER_TABLE.FIRST_NAME, CUSTOMER_TABLE.LAST_NAME, CUSTOMER_TABLE.PHONE,
                    CUSTOMER_TABLE.EMAIL, CUSTOMER_TABLE.COMPANY, CUSTOMER_TABLE.STREET, CUSTOMER_TABLE.CITY, CUSTOMER_TABLE.STATE,
                    CUSTOMER_TABLE.ZIP, CUSTOMER_TABLE.CUSTOMER_ID, customer.getId()));
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
            c.createStatement().execute("delete from " + CUSTOMER_TABLE + " where " + CUSTOMER_TABLE.CUSTOMER_ID + "=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> filter(@NotNull Customer customer) {
        List<Customer> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery(
                    "select " + CUSTOMER_TABLE.CUSTOMER_ID + " from " + CUSTOMER_TABLE +
                            " where " + CUSTOMER_TABLE.FIRST_NAME + " like \"" + customer.getFirstName() + "%\"" +
                            "and " + CUSTOMER_TABLE.LAST_NAME + " like \"" + customer.getLastName() + "%\"" +
                            "and " + CUSTOMER_TABLE.COMPANY + " like \"" + customer.getCompany() + "%\"" +
                            "and " + CUSTOMER_TABLE.PHONE + " like \"" + customer.getPhone() + "%\"" +
                            "and " + CUSTOMER_TABLE.EMAIL + " like \"" + customer.getEmail() + "%\"" +
                            "and " + CUSTOMER_TABLE.STREET + " like \"" + customer.getAddress().getStreet() + "%\"" +
                            "and " + CUSTOMER_TABLE.CITY + " like \"" + customer.getAddress().getCity() + "%\"" +
                            "and " + CUSTOMER_TABLE.STATE + " like \"" + customer.getAddress().getState() + "%\"" +
                            "and " + CUSTOMER_TABLE.ZIP + " like \"" + customer.getAddress().getZip() + "%\"");
            while (rs.next()) {
                int id = rs.getInt(CUSTOMER_TABLE.CUSTOMER_ID);
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
            ResultSet rs = c.createStatement().executeQuery("select distinct " + CUSTOMER_TABLE.STREET +
                    " from " + CUSTOMER_TABLE + " order by " + CUSTOMER_TABLE.STREET);
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
            ResultSet rs = c.createStatement().executeQuery("select distinct " + CUSTOMER_TABLE.CITY +
                    " from " + CUSTOMER_TABLE + " order by " + CUSTOMER_TABLE.CITY);
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
            ResultSet rs = c.createStatement().executeQuery("select distinct " + CUSTOMER_TABLE.ZIP + " from " + CUSTOMER_TABLE + " order by " + CUSTOMER_TABLE.ZIP);
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
