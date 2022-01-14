package model;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DB {
    private static DB singleton;
    private static final String DB_NAME = "autoshop.db";

    public static DB get() {
        return singleton;
    }

    public static void init() {
        singleton = new DB();
    }

    private Connection c;
    private List<Product> productsMarkedForDeletion;

    private DB() {
        try {
            File file = new File(DB_NAME);
            if (!file.exists()) {
                System.out.println("File " + DB_NAME + " does not exist! Creating new database...");
                file.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:" + file);
            this.productsMarkedForDeletion = new LinkedList<>();
            try {
                System.out.println("Connected to AutoShop Database");
                System.out.println("customer: " + c.createStatement().executeQuery("select count(*) from customer").getInt(1));
                System.out.println("vehicle: " + c.createStatement().executeQuery("select count(*) from vehicle").getInt(1));
                System.out.println("item: " + c.createStatement().executeQuery("select count(*) from item").getInt(1));
                System.out.println("work_order: " + c.createStatement().executeQuery("select count(*) from work_order").getInt(1));
                System.out.println("work_order_item: " + c.createStatement().executeQuery("select count(*) from work_order_item").getInt(1));
                System.out.println("work_order_labor: " + c.createStatement().executeQuery("select count(*) from work_order_labor").getInt(1));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Some of the tables do not exist, re-initializing tables...");
                initTables();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tables are only initialized if one of them do not exist.
     *
     * @throws SQLException
     */
    private void initTables() throws SQLException {
        Statement stmt = c.createStatement();
        stmt.addBatch("""
                    create table if not exists customer (
                    customer_id integer primary key autoincrement,
                    first_name text,
                    last_name text,
                    phone text,
                    email text,
                    company text,
                    street text,
                    city text,
                    state text,
                    zip text)""");
        stmt.addBatch("""
                create table if not exists vehicle (
                vin text primary key,
                year int, make text,
                model text,
                license_plate text,
                color text,
                engine text,
                transmission text,
                mileage_in text,
                mileage_out text)""");
        stmt.addBatch("""
                create table if not exists item (
                item_name text primary key,
                desc text,
                retail_price real,
                list_price real,
                taxable boolean,
                quantity integer)""");
        stmt.addBatch("""
                create table if not exists work_order (
                work_order_id integer primary key autoincrement,
                date_created date,
                date_completed date,
                customer_first_name text,
                customer_last_name text,
                customer_phone text,
                customer_email text,
                customer_company text,
                customer_street text,
                customer_city text,
                customer_state text,
                customer_zip text,
                vehicle_vin text,
                vehicle_year int,
                vehicle_make text,
                vehicle_model text,
                vehicle_license_plate text,
                vehicle_color text,
                vehicle_engine text,
                vehicle_transmission text,
                vehicle_mileage_in text,
                vehicle_mileage_out text)""");
        stmt.addBatch("""
                create table if not exists work_order_item (
                work_order_item_id integer primary key autoincrement,
                work_order_id integer,
                item_name text,
                item_desc text,
                item_retail_price real,
                item_list_price real,
                item_quantity integer,
                item_taxable boolean,
                foreign key(work_order_id) references work_order(work_order_id))""");
        stmt.addBatch("""
                create table if not exists work_order_labor (
                work_order_labor_id integer primary key autoincrement,
                work_order_id integer,
                labor_code text,
                labor_desc text,
                labor_billed_hrs real,
                labor_rate real,
                labor_taxable boolean,
                foreign key(work_order_id) references work_order(work_order_id))""");
        stmt.executeBatch();
    }

    public void addProductMarkedForDeletion(Product product) {
        productsMarkedForDeletion.add(product);
    }

    public void clearAllProductsMarkedForDeletion() {
        productsMarkedForDeletion.clear();
    }

    public void deleteProductsMarkedForDeletion() {
        productsMarkedForDeletion.forEach(product -> {
            try {
                if (product instanceof AutoPart)
                    deleteWorkOrderAutoPart((AutoPart) product);
                else if (product instanceof Labor)
                    deleteWorkOrderLabor((Labor) product);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        clearAllProductsMarkedForDeletion();
    }

    public void addCustomer(Customer customer) {
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

    public Customer getCustomerById(int id) {
        Customer customer = null;
        try {
            Statement stmt = c.createStatement();
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

    public List<Customer> getAllCustomers() {
        List<Customer> list = new LinkedList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select customer_id from customer;");
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                Customer cus = getCustomerById(id);
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public void updateCustomer(Customer customer) {
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

    public void deleteCustomerById(int id) {
        try {
            Statement stmt = c.createStatement();
            stmt.execute("delete from customer where customer_id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getFilteredCustomers(Customer c) {
        List<Customer> list = new LinkedList<>();
        try {
            Statement stmt = this.c.createStatement();
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
                Customer cus = getCustomerById(id);
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
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select distinct street from customer order by street");
            while(rs.next()) {
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
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select distinct city from customer order by city");
            while(rs.next()) {
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
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select distinct zip from customer order by zip");
            while(rs.next()) {
                String street = rs.getString(1);
                list.add(street);
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

    public void updateVehicle(Vehicle vehicle) {
        try {
            PreparedStatement prepStmt = c.prepareStatement("update vehicle set " +
                    "year = ?, model = ?, license_plate = ?, color = ?, engine = ?, " +
                    "transmission = ?, mileage_in = ?, mileage_out = ? " +
                    "where vin=\"" + vehicle.getVin() + "\"");
            prepStmt.setString(1, vehicle.getVin());
            prepStmt.setInt(2, vehicle.getYear());
            prepStmt.setString(3, vehicle.getModel());
            prepStmt.setString(4, vehicle.getLicensePlate());
            prepStmt.setString(5, vehicle.getColor());
            prepStmt.setString(6, vehicle.getEngine());
            prepStmt.setString(7, vehicle.getTransmission());
            prepStmt.setString(8, vehicle.getMileageIn());
            prepStmt.setString(9, vehicle.getMileageOut());

            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVehicleByVin(String vin) {
        try {
            Statement stmt = c.createStatement();
            stmt.execute("delete from vehicle where vin=\"" + vin + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAutoPart(AutoPart part) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into item " +
                            "(item_name,desc,retail_price,list_price,taxable,quantity) " +
                            "values (?,?,?,?,?,?)");
            prepStmt.setString(1, part.getName());
            prepStmt.setString(2, part.getDesc());
            prepStmt.setDouble(3, part.getRetailPrice());
            prepStmt.setDouble(4, part.getListPrice());
            prepStmt.setBoolean(5, part.isTaxable());
            prepStmt.setInt(6, part.getQuantity());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AutoPart> getFilteredItems(String s1, String s2) {
        List<AutoPart> list = new LinkedList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select * from item where " +
                            "item_name like \"" + s1 + "%\" " +
                            "and desc like \"%" + s2 + "%\"");
            while (rs.next()) {
                String partNumber = rs.getString("item_name");
                String desc = rs.getString("desc");
                double retailPrice = rs.getDouble("retail_price");
                double listPrice = rs.getDouble("list_price");
                int quantity = rs.getInt(("quantity"));
                boolean taxable = rs.getBoolean("taxable");
                list.add(new AutoPart(partNumber, desc, retailPrice, listPrice, quantity, taxable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getMaxWorkOrderId() throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select max(work_order_id) from work_order");
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    public int getNextWorkOrderId() {
        try {
            return getMaxWorkOrderId() + 1;
        } catch (SQLException e) {
            return -1;
        }
    }

    public void addWorkOrder(WorkOrder workOrder) {
        try {
            // Add work_order row
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into work_order (" +
                            "date_created," +
                            "date_completed," +
                            "customer_first_name," +
                            "customer_last_name," +
                            "customer_phone," +
                            "customer_email," +
                            "customer_company," +
                            "customer_street," +
                            "customer_city," +
                            "customer_state," +
                            "customer_zip," +
                            "vehicle_vin," +
                            "vehicle_year," +
                            "vehicle_make," +
                            "vehicle_model," +
                            "vehicle_license_plate," +
                            "vehicle_color," +
                            "vehicle_engine," +
                            "vehicle_transmission," +
                            "vehicle_mileage_in," +
                            "vehicle_mileage_out) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prepStmt.setDate(1, workOrder.getDateCreated());
            prepStmt.setDate(2, workOrder.getDateCompleted());
            prepStmt.setString(3, workOrder.getCustomer().getFirstName());
            prepStmt.setString(4, workOrder.getCustomer().getLastName());
            prepStmt.setString(5, workOrder.getCustomer().getPhone());
            prepStmt.setString(6, workOrder.getCustomer().getEmail());
            prepStmt.setString(7, workOrder.getCustomer().getCompany());
            prepStmt.setString(8, workOrder.getCustomer().getAddress().getStreet());
            prepStmt.setString(9, workOrder.getCustomer().getAddress().getCity());
            prepStmt.setString(10, workOrder.getCustomer().getAddress().getState());
            prepStmt.setString(11, workOrder.getCustomer().getAddress().getZip());
            prepStmt.setString(12, workOrder.getVehicle().getVin());
            prepStmt.setInt(13, workOrder.getVehicle().getYear());
            prepStmt.setString(14, workOrder.getVehicle().getMake());
            prepStmt.setString(15, workOrder.getVehicle().getModel());
            prepStmt.setString(16, workOrder.getVehicle().getLicensePlate());
            prepStmt.setString(17, workOrder.getVehicle().getColor());
            prepStmt.setString(18, workOrder.getVehicle().getEngine());
            prepStmt.setString(19, workOrder.getVehicle().getTransmission());
            prepStmt.setString(20, workOrder.getVehicle().getMileageIn());
            prepStmt.setString(21, workOrder.getVehicle().getMileageOut());
            prepStmt.execute();

            int id = getMaxWorkOrderId();
            workOrder.setId(id);

            // Add work_order_item row(s)
            Iterator<AutoPart> itemIterator = workOrder.autoPartIterator();
            while (itemIterator.hasNext()) {
                AutoPart item = itemIterator.next();
                saveAutoPart(workOrder.getId(), item);
            }
            // Add work_order_labor row(s)
            Iterator<Labor> laborIterator = workOrder.laborIterator();
            while (laborIterator.hasNext()) {
                Labor labor = laborIterator.next();
                addLabor(workOrder.getId(), labor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WorkOrder getWorkOrderById(int workOrderId) {
        WorkOrder workOrder = null;
        try {
            Statement stmt = c.createStatement();
            ResultSet rsWorkOrder = stmt.executeQuery(
                    "select * from work_order " +
                            "where work_order_id = " + workOrderId);

            if (rsWorkOrder.next()) {
                Date dateCreated = rsWorkOrder.getDate(2);
                Date dateCompleted = rsWorkOrder.getDate(3);
                String firstName = rsWorkOrder.getString(4);
                String lastName = rsWorkOrder.getString(5);
                String phone = rsWorkOrder.getString(6);
                String email = rsWorkOrder.getString(7);
                String company = rsWorkOrder.getString(8);
                String street = rsWorkOrder.getString(9);
                String city = rsWorkOrder.getString(10);
                String state = rsWorkOrder.getString(11);
                String zip = rsWorkOrder.getString(12);
                Address address = new Address(street, city, state, zip);
                Customer customer = new Customer(firstName, lastName, phone, email, company, address);
                String vin = rsWorkOrder.getString(13);
                int year = rsWorkOrder.getInt(14);
                String make = rsWorkOrder.getString(15);
                String model = rsWorkOrder.getString(16);
                String licensePlate = rsWorkOrder.getString(17);
                String color = rsWorkOrder.getString(18);
                String engine = rsWorkOrder.getString(19);
                String transmission = rsWorkOrder.getString(20);
                String mileageIn = rsWorkOrder.getString(21);
                String mileageOut = rsWorkOrder.getString(22);
                Vehicle vehicle = new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut);
                workOrder = new WorkOrder(customer, vehicle);
                workOrder.setId(workOrderId);
                workOrder.setDateCreated(dateCreated);
                workOrder.setDateCompleted(dateCompleted);

                ResultSet rsItem = stmt.executeQuery("select * from work_order_item " +
                        "where work_order_id = " + workOrderId);
                while (rsItem.next()) {
                    int id = rsItem.getInt(1);
                    String name = rsItem.getString(3);
                    String desc = rsItem.getString(4);
                    double retailPrice = rsItem.getDouble(5);
                    double listPrice = rsItem.getDouble(6);
                    int quantity = rsItem.getInt(7);
                    boolean taxable = rsItem.getBoolean(8);
                    AutoPart item = new AutoPart(name, desc, retailPrice, listPrice, quantity, taxable);
                    item.setId(id);
                    workOrder.addItem(item);
                }

                ResultSet rsLabor = stmt.executeQuery("select * from work_order_labor " +
                        "where work_order_id = " + workOrderId);
                while (rsLabor.next()) {
                    int id = rsLabor.getInt(1);
                    String laborCode = rsLabor.getString(3);
                    String desc = rsLabor.getString(4);
                    double billedHrs = rsLabor.getDouble(5);
                    double rate = rsLabor.getDouble(6);
                    boolean taxable = rsLabor.getBoolean(7);
                    Labor labor = new Labor(laborCode, desc, billedHrs, rate, taxable);
                    labor.setId(id);
                    workOrder.addLabor(labor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workOrder;
    }

    public List<WorkOrder> getAllWorkOrders() {
        List<WorkOrder> list = new LinkedList<>();
        try {
            Statement stmt = c.createStatement();
            ResultSet workOrderSet = stmt.executeQuery("select work_order_id from work_order");
            while (workOrderSet.next()) {
                int workOrderId = workOrderSet.getInt(1);
                WorkOrder workOrder = getWorkOrderById(workOrderId);
                list.add(workOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateWorkOrder(WorkOrder workOrder) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "update work_order set " +
                            "date_completed = ?," +
                            "customer_first_name = ?," +
                            "customer_last_name = ?," +
                            "customer_phone = ?," +
                            "customer_email = ?," +
                            "customer_company = ?," +
                            "customer_street = ?," +
                            "customer_city = ?," +
                            "customer_state = ?," +
                            "customer_zip = ?," +
                            "vehicle_vin = ?," +
                            "vehicle_year = ?," +
                            "vehicle_make = ?," +
                            "vehicle_model = ?," +
                            "vehicle_license_plate = ?," +
                            "vehicle_color = ?," +
                            "vehicle_engine = ?," +
                            "vehicle_transmission = ?," +
                            "vehicle_mileage_in = ?," +
                            "vehicle_mileage_out = ? " +
                            "where work_order_id = ?");
            prepStmt.setDate(1, workOrder.getDateCompleted());
            prepStmt.setString(2, workOrder.getCustomer().getFirstName());
            prepStmt.setString(3, workOrder.getCustomer().getLastName());
            prepStmt.setString(4, workOrder.getCustomer().getPhone());
            prepStmt.setString(5, workOrder.getCustomer().getEmail());
            prepStmt.setString(6, workOrder.getCustomer().getCompany());
            prepStmt.setString(7, workOrder.getCustomer().getAddress().getStreet());
            prepStmt.setString(8, workOrder.getCustomer().getAddress().getCity());
            prepStmt.setString(9, workOrder.getCustomer().getAddress().getState());
            prepStmt.setString(10, workOrder.getCustomer().getAddress().getZip());
            prepStmt.setString(11, workOrder.getVehicle().getVin());
            prepStmt.setInt(12, workOrder.getVehicle().getYear());
            prepStmt.setString(13, workOrder.getVehicle().getMake());
            prepStmt.setString(14, workOrder.getVehicle().getModel());
            prepStmt.setString(15, workOrder.getVehicle().getLicensePlate());
            prepStmt.setString(16, workOrder.getVehicle().getColor());
            prepStmt.setString(17, workOrder.getVehicle().getEngine());
            prepStmt.setString(18, workOrder.getVehicle().getTransmission());
            prepStmt.setString(19, workOrder.getVehicle().getMileageIn());
            prepStmt.setString(20, workOrder.getVehicle().getMileageOut());
            prepStmt.setInt(21, workOrder.getId());
            prepStmt.execute();

            Iterator<AutoPart> autoPartIterator = workOrder.autoPartIterator();
            while (autoPartIterator.hasNext()) {
                AutoPart autoPart = autoPartIterator.next();
                if (autoPart.isNew())
                    saveAutoPart(workOrder.getId(), autoPart);
                else
                    updateItem(autoPart);
            }
            Iterator<Labor> laborIterator = workOrder.laborIterator();
            while (laborIterator.hasNext()) {
                Labor labor = laborIterator.next();
                if (labor.isNew())
                    addLabor(workOrder.getId(), labor);
                else
                    updateLabor(labor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorkOrder(WorkOrder workOrder) {
        try {
            Statement stmt = c.createStatement();
            stmt.execute("delete from work_order where work_order_id = " + workOrder.getId());
            Iterator<AutoPart> itemIterator = workOrder.autoPartIterator();
            while (itemIterator.hasNext()) {
                deleteWorkOrderAutoPart(itemIterator.next());
            }
            Iterator<Labor> laborIterator = workOrder.laborIterator();
            while (laborIterator.hasNext()) {
                deleteWorkOrderLabor(laborIterator.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAutoPart(int workOrderId, AutoPart item) throws SQLException {
        PreparedStatement prepStmt = c.prepareStatement(
                "insert into work_order_item (" +
                        "work_order_id," +
                        "item_name," +
                        "item_desc," +
                        "item_retail_price," +
                        "item_list_price," +
                        "item_quantity," +
                        "item_taxable" +
                        ") " +
                        "values (?, ?, ?, ?, ?, ?, ?)");
        prepStmt.setInt(1, workOrderId);
        prepStmt.setString(2, item.getName());
        prepStmt.setString(3, item.getDesc());
        prepStmt.setDouble(4, item.getRetailPrice());
        prepStmt.setDouble(5, item.getListPrice());
        prepStmt.setInt(6, item.getQuantity());
        prepStmt.setBoolean(7, item.isTaxable());
        prepStmt.execute();
    }

    public void updateItem(AutoPart item) throws SQLException {
        PreparedStatement prepStmt = c.prepareStatement(
                "update work_order_item set " +
                        "item_name = ?," +
                        "item_desc = ?," +
                        "item_retail_price = ?," +
                        "item_list_price = ?," +
                        "item_quantity = ?," +
                        "item_taxable = ? " +
                        "where work_order_item_id = ?");
        prepStmt.setString(1, item.getName());
        prepStmt.setString(2, item.getDesc());
        prepStmt.setDouble(3, item.getRetailPrice());
        prepStmt.setDouble(4, item.getListPrice());
        prepStmt.setInt(5, item.getQuantity());
        prepStmt.setBoolean(6, item.isTaxable());
        prepStmt.setInt(7, item.getId());
        prepStmt.execute();
    }

    public void deleteWorkOrderAutoPart(AutoPart item) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.execute("delete from work_order_item where work_order_item_id = " + item.getId());
    }

    public void addLabor(int workOrderId, Labor labor) throws SQLException {
        PreparedStatement prepStmt = c.prepareStatement(
                "insert into work_order_labor (" +
                        "work_order_id," +
                        "labor_code," +
                        "labor_desc," +
                        "labor_billed_hrs," +
                        "labor_rate," +
                        "labor_taxable" +
                        ") " +
                        "values (?, ?, ?, ?, ?, ?)");
        prepStmt.setInt(1, workOrderId);
        prepStmt.setString(2, labor.getName());
        prepStmt.setString(3, labor.getDesc());
        prepStmt.setDouble(4, labor.getBilledHrs());
        prepStmt.setDouble(5, labor.getRate());
        prepStmt.setBoolean(6, labor.isTaxable());
        prepStmt.execute();
    }

    public void updateLabor(Labor labor) throws SQLException {
        PreparedStatement prepStmt = c.prepareStatement(
                "update work_order_labor set " +
                        "labor_code = ?," +
                        "labor_desc = ?," +
                        "labor_billed_hrs = ?," +
                        "labor_rate = ?," +
                        "labor_taxable = ? " +
                        "where work_order_labor_id = ?");
        prepStmt.setString(1, labor.getName());
        prepStmt.setString(2, labor.getDesc());
        prepStmt.setDouble(3, labor.getBilledHrs());
        prepStmt.setDouble(4, labor.getRate());
        prepStmt.setBoolean(5, labor.isTaxable());
        prepStmt.setInt(6, labor.getId());
        prepStmt.execute();
    }

    public void deleteWorkOrderLabor(Labor labor) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.execute("delete from work_order_labor where work_order_labor_id = " + labor.getId());
    }
}
