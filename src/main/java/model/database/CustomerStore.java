package model.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.customer.Address;
import model.customer.Customer;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

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
                    select customer_id from customer
                    where first_name =  ? and last_name = ?
                    and phone = ? and email = ?
                    and company = ? and address = ?
                    and city = ? and state = ? and zip = ?
                    """);
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
                        "insert into customer (first_name,last_name,phone,email,company,address,city,state,zip) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
            ResultSet rs = c.createStatement().executeQuery("select * from customer where customer_id = " + id);
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String company = rs.getString("company");
                String street = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");
                Address address = new Address(street, city, state, zip);
                customer = new Customer(id, firstName, lastName, phone, email, company, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public ObservableList<Customer> getAll(final int LIMIT) {
        List<Customer> list = new LinkedList<>();
        String s = "select customer_id from customer order by last_name limit " + LIMIT;
        if (LIMIT <= 0) s = "select customer_id from customer order by last_name";
        try {
            ResultSet rs = c.createStatement().executeQuery(s);
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                Customer cus = getById(id);
                if (cus != null) list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return FXCollections.observableList(list);
        }
    }

    public void update(@NotNull Customer c) {
        try {
            PreparedStatement prepStmt = this.c.prepareStatement("""
                    update customer set 
                    first_name = ?, last_name = ?, phone = ?, 
                    email = ?, company = ?, address = ?,
                    city = ?, state = ?, zip = ? where customer_id = ?
                    """);
            prepStmt.setString(1, c.getFirstName());
            prepStmt.setString(2, c.getLastName());
            prepStmt.setString(3, c.getPhone());
            prepStmt.setString(4, c.getEmail());
            prepStmt.setString(5, c.getCompany());
            prepStmt.setString(6, c.getAddress().getStreet());
            prepStmt.setString(7, c.getAddress().getCity());
            prepStmt.setString(8, c.getAddress().getState());
            prepStmt.setString(9, c.getAddress().getZip());
            prepStmt.setInt(10, c.getId());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            c.createStatement().execute("delete from customer where customer_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> filter(@NotNull Customer c) {
        List<Customer> list = new LinkedList<>();
        try {
            ResultSet rs = this.c.createStatement().executeQuery(
                    "select customer_id from customer " +
                            "where first_name like \"" + c.getFirstName() + "%\"" +
                            "and last_name like \"" + c.getLastName() + "%\"" +
                            "and company like \"" + c.getCompany() + "%\"" +
                            "and phone like \"" + c.getPhone() + "%\"" +
                            "and email like \"" + c.getEmail() + "%\"" +
                            "and address like \"" + c.getAddress().getStreet() + "%\"" +
                            "and city like \"" + c.getAddress().getCity() + "%\"" +
                            "and state like \"" + c.getAddress().getState() + "%\"" +
                            "and zip like \"" + c.getAddress().getZip() + "%\"");
            while (rs.next()) {
                int id = rs.getInt(1);
                Customer cus = getById(id);
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getUniqueCompanies() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct company from customer order by company");
            while (rs.next()) {
                String x = rs.getString(1);
                list.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getUniqueAddresses() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("""
                    select distinct address
                    from customer
                    order by address
                    """);
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
            ResultSet rs = c.createStatement().executeQuery("""
                    select distinct city
                    from customer order by city
                    """);
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
            ResultSet rs = c.createStatement().executeQuery("""
                    select distinct zip from customer order by zip
                    """);
            while (rs.next()) {
                String street = rs.getString(1);
                list.add(street);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void export(String des) throws SQLException, IOException {
        ResultSet rs = c.createStatement().executeQuery("select * from customer");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("customer");

        DB.get().export(rs, sheet);

        FileOutputStream fos = new FileOutputStream(des);
        workbook.write(fos);
        workbook.close();
    }
}
