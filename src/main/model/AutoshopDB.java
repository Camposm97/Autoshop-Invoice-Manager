package main.model;

import javafx.collections.FXCollections;

import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AutoshopDB {
    private static AutoshopDB autoshopDB;
    private static final URL DB_URL =
            AutoshopDB.class.getResource("../../db/autoshop.db");

    public static AutoshopDB get() {
        if (autoshopDB == null) {
            autoshopDB = new AutoshopDB();
        }
        return autoshopDB;
    }

    private Connection c;

    private AutoshopDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DB_URL.toString());
            System.out.println("Connected to AutoShop Database");
            initializeTables();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes tables: customer, vehicle, work_order, invoice, item
     * Tables are only initialized if they do not exist.
     * @throws SQLException
     */
    private void initializeTables() throws SQLException {
        Statement stmt = c.createStatement();
        stmt.execute(
                "create table if not exists customer " +
                        "(customer_id integer primary key autoincrement, " +
                        "first_name text, " +
                        "last_name text, " +
                        "phone text," +
                        "company text, " +
                        "street text, " +
                        "city text, " +
                        "state text, " +
                        "zip text);");
        stmt.execute(
                "create table if not exists vehicle " +
                        "(vin text primary key," +
                        "year int, " +
                        "make text, " +
                        "model text, " +
                        "license_plate text, " +
                        "color text, " +
                        "engine text, " +
                        "mileage_in text, " +
                        "mileage_out text);");
        stmt.execute(
                "create table if not exists work_order " +
                        "(work_order_id integer primary key autoincrement," +
                        "customer_id integer," +
                        "vehicle_vin text," +
                        "invoice_id integer);");
        stmt.execute(
                "create table if not exists invoice " +
                        "(invoice_id integer primary key autoincrement," +
                        ");");
        stmt.execute(
                "create table if not exists item (" +
                        "item_id integer primary key autoincrement," +
                        "invoice_id integer," +
                        "name text," +
                        "desc text," +
                        "retailPrice real," +
                        "listPrice real," +
                        "taxable boolean," +
                        "quantity integer);");
    }

    public void addCustomer(Customer cus) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into customer " +
                            "(first_name, last_name, phone, company, street, city, state, zip)" +
                            "values (?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, cus.getFirstName());
            prepStmt.setString(2, cus.getLastName());
            prepStmt.setString(3, cus.getPhone());
            prepStmt.setString(4, cus.getCompany());
            prepStmt.setString(5, cus.getAddress().getStreet());
            prepStmt.setString(6, cus.getAddress().getCity());
            prepStmt.setString(7, cus.getAddress().getState());
            prepStmt.setString(8, cus.getAddress().getZip());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new LinkedList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customer;");
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone");
                String company = rs.getString("company");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");
                Address address = new Address(street, city, state, zip);
                Customer cus = new Customer(id, firstName, lastName, phone, company, address);
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Customer> getFilteredCustomers(String strFirstName, String strLastName, String strCompany) {
        List<Customer> list = new LinkedList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select * from customer " +
                            "where first_name like \"" + strFirstName + "%\" " +
                            "and last_name like \"" + strLastName + "%\" " +
                            "and company like \"" + strCompany + "%\";");
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone");
                String company = rs.getString("company");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");
                Address address = new Address(street, city, state, zip);
                Customer cus = new Customer(id, firstName, lastName, phone, company, address);
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addVehicle(Vehicle vehicle) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into vehicle " +
                            "(vin, year, make, model, license_plate, color, engine, mileage_in, mileage_out)," +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prepStmt.setString(1, vehicle.getVin());
            prepStmt.setInt(2, vehicle.getYear());
            prepStmt.setString(3, vehicle.getMake());
            prepStmt.setString(4, vehicle.getModel());
            prepStmt.setString(5, vehicle.getLicensePlate());
            prepStmt.setString(6, vehicle.getColor());
            prepStmt.setString(7, vehicle.getEngine());
            prepStmt.setString(8, vehicle.getMileageIn());
            prepStmt.setString(9, vehicle.getMileageOut());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vehicle> getAllVehicles() {
        return null;
    }

    public List<WorkOrder> getAllWorkOrders() {
        return null;
    }

    public List<Item> getAllItems() {
        return null;
    }

    public List<Item> getItemsByInvoiceId(int invoiceId) {
        return null;
    }

    public Invoice getInvoice(int id) {
        return null;
    }
}
