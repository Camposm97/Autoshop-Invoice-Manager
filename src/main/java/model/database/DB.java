package model.database;

import model.work_order.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import static model.database.DBAttributes.*;

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
    private List<WorkOrderPayment> paymentsMarkedForDeletion;

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
            this.paymentsMarkedForDeletion = new LinkedList<>();
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
        stmt.execute("create table if not exists " + CUSTOMER_TABLE + " (" +
                CUSTOMER_TABLE.CUSTOMER_ID + " integer primary key autoincrement," +
                CUSTOMER_TABLE.FIRST_NAME + " text," +
                CUSTOMER_TABLE.LAST_NAME + " text," +
                CUSTOMER_TABLE.PHONE + " text," +
                CUSTOMER_TABLE.EMAIL + " text," +
                CUSTOMER_TABLE.COMPANY + " text," +
                CUSTOMER_TABLE.STREET + " text," +
                CUSTOMER_TABLE.CITY + " text," +
                CUSTOMER_TABLE.STATE + " text," +
                CUSTOMER_TABLE.ZIP + " text)");
        stmt.execute("create table if not exists " + VEHICLE_TABLE + " (" +
                VEHICLE_TABLE.VEHICLE_ID + " integer primary key autoincrement," +
                VEHICLE_TABLE.VIN + " text," +
                VEHICLE_TABLE.YEAR + " character(4)," +
                VEHICLE_TABLE.MAKE + " text," +
                VEHICLE_TABLE.MODEL + " text," +
                VEHICLE_TABLE.LICENSE_PLATE + " text," +
                VEHICLE_TABLE.COLOR + " text," +
                VEHICLE_TABLE.ENGINE + " text," +
                VEHICLE_TABLE.TRANSMISSION + " text," +
                VEHICLE_TABLE.CUSTOMER_ID + " int," +
                "foreign key (" + VEHICLE_TABLE.CUSTOMER_ID + ")" + " references " +
                CUSTOMER_TABLE + "(" + CUSTOMER_TABLE.CUSTOMER_ID + "))");
        stmt.execute("create table if not exists " + ITEM_TABLE + " (" +
                ITEM_TABLE.ITEM_NAME + " text primary key," +
                ITEM_TABLE.ITEM_DESC + " text," +
                ITEM_TABLE.RETAIL_PRICE + " real," +
                ITEM_TABLE.LIST_PRICE + " real," +
                ITEM_TABLE.TAXABLE + " boolean," +
                ITEM_TABLE.QUANTITY + " integer)");
        stmt.execute("create table if not exists " +  WORK_ORDER_TABLE + " (" +
                WORK_ORDER_TABLE.WORK_ORDER_ID + " integer primary key autoincrement," +
                WORK_ORDER_TABLE.DATE_CREATED + " date," +
                WORK_ORDER_TABLE.DATE_COMPLETED + " date," +
                WORK_ORDER_TABLE.CUSTOMER_FIRST_NAME + " text," +
                WORK_ORDER_TABLE.CUSTOMER_LAST_NAME + " text," +
                WORK_ORDER_TABLE.CUSTOMER_PHONE + " text," +
                WORK_ORDER_TABLE.CUSTOMER_EMAIL + " text," +
                WORK_ORDER_TABLE.CUSTOMER_COMPANY + " text," +
                WORK_ORDER_TABLE.CUSTOMER_STREET + " text," +
                WORK_ORDER_TABLE.CUSTOMER_CITY + " text," +
                WORK_ORDER_TABLE.CUSTOMER_STATE + " text," +
                WORK_ORDER_TABLE.CUSTOMER_ZIP + " text," +
                WORK_ORDER_TABLE.VEHICLE_VIN + " text," +
                WORK_ORDER_TABLE.VEHICLE_YEAR + " character(4)," +
                WORK_ORDER_TABLE.VEHICLE_MAKE + " text," +
                WORK_ORDER_TABLE.VEHICLE_MODEL + " text," +
                WORK_ORDER_TABLE.VEHICLE_LICENSE_PLATE + " text," +
                WORK_ORDER_TABLE.VEHICLE_COLOR + " text," +
                WORK_ORDER_TABLE.VEHICLE_ENGINE + " text," +
                WORK_ORDER_TABLE.VEHICLE_TRANSMISSION + " text," +
                WORK_ORDER_TABLE.VEHICLE_MILEAGE_IN + " text," +
                WORK_ORDER_TABLE.VEHICLE_MILEAGE_OUT + " text)");
        stmt.execute("create table if not exists " + WORK_ORDER_ITEM_TABLE + " (" +
                WORK_ORDER_ITEM_TABLE.ITEM_ID + " integer primary key autoincrement," +
                WORK_ORDER_ITEM_TABLE.WORK_ORDER_ID + " integer," +
                WORK_ORDER_ITEM_TABLE.ITEM_NAME + " text," +
                WORK_ORDER_ITEM_TABLE.ITEM_DESC + " text," +
                WORK_ORDER_ITEM_TABLE.ITEM_RETAIL_PRICE + " real," +
                WORK_ORDER_ITEM_TABLE.ITEM_LIST_PRICE + " real," +
                WORK_ORDER_ITEM_TABLE.ITEM_QUANTITY + " integer," +
                WORK_ORDER_ITEM_TABLE.ITEM_TAXABLE + " boolean," +
                "foreign key(" + WORK_ORDER_ITEM_TABLE.WORK_ORDER_ID + ") references " + WORK_ORDER_TABLE + "(" + WORK_ORDER_TABLE.WORK_ORDER_ID + "))");
        stmt.execute("create table if not exists " + WORK_ORDER_LABOR_TABLE + " (" +
                WORK_ORDER_LABOR_TABLE.LABOR_ID + " integer primary key autoincrement," +
                WORK_ORDER_LABOR_TABLE.WORK_ORDER_ID + " integer," +
                WORK_ORDER_LABOR_TABLE.LABOR_CODE + " text," +
                WORK_ORDER_LABOR_TABLE.LABOR_DESC + " text," +
                WORK_ORDER_LABOR_TABLE.LABOR_BILLED_HRS + " real," +
                WORK_ORDER_LABOR_TABLE.LABOR_RATE + " real," +
                WORK_ORDER_LABOR_TABLE.LABOR_TAXABLE + " boolean," +
                "foreign key(" + WORK_ORDER_LABOR_TABLE.WORK_ORDER_ID + ") references " + WORK_ORDER_TABLE + "(" + WORK_ORDER_TABLE.WORK_ORDER_ID + "))");
        stmt.execute("create table if not exists " + WORK_ORDER_PAYMENT_TABLE + " (" +
                WORK_ORDER_PAYMENT_TABLE.PAYMENT_ID + " integer primary key autoincrement," +
                WORK_ORDER_PAYMENT_TABLE.WORK_ORDER_ID + " integer," +
                WORK_ORDER_PAYMENT_TABLE.PAYMENT_DATE + " date," +
                WORK_ORDER_PAYMENT_TABLE.PAYMENT_TYPE + " character(5)," +
                WORK_ORDER_PAYMENT_TABLE.PAYMENT_AMOUNT + " real," +
                "foreign key(" + WORK_ORDER_PAYMENT_TABLE.WORK_ORDER_ID + ") references " + WORK_ORDER_TABLE + "(" + WORK_ORDER_TABLE.WORK_ORDER_ID + "))");
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

    public void addPaymentMarkedForDeletion(WorkOrderPayment payment) {
        paymentsMarkedForDeletion.add(payment);
    }

    public void clearAllPaymentsMarkedForDeletion() {
        paymentsMarkedForDeletion.clear();
    }

    public void deletePaymentMarkedForDeletion() {
        paymentsMarkedForDeletion.forEach(x -> {
            workOrders.deletePaymentById(x.getId());
        });
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
