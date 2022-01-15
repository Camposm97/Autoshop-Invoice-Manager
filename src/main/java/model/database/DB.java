package model.database;

import model.work_order.*;

import java.io.File;
import java.sql.*;
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
    private CustomerStore customers;
    private VehicleStore vehicles;
    private AutoPartStore autoParts;
    private WorkOrderStore workOrders;

    private Statement stmt;
    private List<Product> productsMarkedForDeletion;

    private DB() {
        try {
            File file = new File(DB_NAME);
            if (!file.exists()) {
                System.out.println("Creating new database...");
                file.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:" + file);
            this.productsMarkedForDeletion = new LinkedList<>();
            this.customers = new CustomerStore(c);
            this.vehicles = new VehicleStore(c);
            this.autoParts = new AutoPartStore(c);
            this.workOrders = new WorkOrderStore(c);
            this.stmt = c.createStatement();
            try {
                System.out.println("Connected to database!");
                displayInfo();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Some of the tables do not exist, re-initializing tables...");
                initTables();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayInfo() throws SQLException {
        System.out.println("customer: " + stmt.executeQuery("select count(*) from customer").getInt(1));
        System.out.println("vehicle: " + stmt.executeQuery("select count(*) from vehicle").getInt(1));
        System.out.println("item: " + stmt.executeQuery("select count(*) from item").getInt(1));
        System.out.println("work_order: " + stmt.executeQuery("select count(*) from work_order").getInt(1));
        System.out.println("work_order_item: " + stmt.executeQuery("select count(*) from work_order_item").getInt(1));
        System.out.println("work_order_labor: " + stmt.executeQuery("select count(*) from work_order_labor").getInt(1));
        System.out.println("work_order_payment: " + stmt.executeQuery("select count(*) from work_order_payment").getInt(1));
    }

    /**
     * Tables are only initialized if one of them do not exist.
     *
     * @throws SQLException
     */
    private void initTables() throws SQLException {
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
        stmt.addBatch("""
                create table if not exists work_order_payment (
                work_order_payment_id integer primary key autoincrement,
                work_order_id integer,
                date_of_payment date,
                amount real,
                type character(5),
                foreign key(work_order_id) references work_order(work_order_id))""");
        stmt.executeBatch();
        stmt.clearBatch();
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
                    workOrders.deleteAutoPart((AutoPart) product);
                else if (product instanceof Labor)
                    workOrders.deleteLabor((Labor) product);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        clearAllProductsMarkedForDeletion();
    }

    public CustomerStore customers() {
        return customers;
    }

    public VehicleStore vehicles() {
        return vehicles;
    }

    public AutoPartStore autoParts() {
        return autoParts;
    }

    public WorkOrderStore workOrders() {
        return workOrders;
    }
}
