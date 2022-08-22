package model.database;

import model.work_order.AutoPart;
import model.work_order.Labor;
import model.work_order.Product;
import model.work_order.WorkOrderPayment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.File;
import java.sql.*;
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

    private DB() {
        try {
            File file = new File(DB_NAME);
            if (!file.exists()) {
                System.out.println("Creating new database...");
                file.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:" + file);
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
        String s1 = """
                create table if not exists customer (
                customer_id integer primary key autoincrement,
                first_name text,
                last_name text,
                phone text,
                email text,
                company text,
                address text,
                city text,
                state text,
                zip text);
                """;
        String s2 = """
                create table if not exists vehicle (
                vehicle_id integer primary key autoincrement,
                customer_id integer,
                vin text,
                year character(4), make text, model text,
                license_plate text,
                color text,
                engine text,
                transmission text,
                foreign key (customer_id) references customer(customer_id));
                """;
        String s3 = """
                create table if not exists item (
                item_name text primary key,
                desc text,
                retail_price real,
                list_price real,
                taxable boolean,
                quantity tinyint);
                """;
        String s4 = """
                create table if not exists work_order (
                work_order_id integer primary key autoincrement,
                date_created date,
                date_completed date,
                customer_first_name text,customer_last_name text,
                customer_phone text,customer_email text,customer_company text,
                customer_address text,customer_city text,customer_state text,customer_zip text,
                vehicle_vin text,vehicle_year character(4),vehicle_make text,vehicle_model text,
                vehicle_license_plate text,vehicle_color text,vehicle,
                vehicle_engine text,vehicle_transmission,
                vehicle_mileage_in text, vehicle_mileage_out text);
                """;
        String s5 = """
                create table if not exists work_order_item (
                work_order_item_id integer primary key autoincrement,
                work_order_id integer,
                item_name text,
                desc text,
                retail_price real,
                list_price real,
                quantity tinyint,
                taxable boolean,
                foreign key(work_order_id) references work_order(work_order_id));
                """;
        String s6 = """
                create table if not exists work_order_labor (
                work_order_labor_id integer primary key autoincrement,
                work_order_id integer,
                labor_code text,
                labor_desc text,
                labor_billed_hrs real,
                labor_rate real,
                labor_taxable boolean,
                foreign key (work_order_id) references work_order(work_order_id));
                """;
        String s7 = """
                create table if not exists work_order_payment (
                work_order_payment_id integer primary key autoincrement,
                work_order_id integer,
                date_of_payment date,
                type character(5),
                amount real,
                foreign key (work_order_id) references work_order(work_order_id));
                """;
        stmt.execute(s1);
        stmt.execute(s2);
        stmt.execute(s3);
        stmt.execute(s4);
        stmt.execute(s5);
        stmt.execute(s6);
        stmt.execute(s7);
    }

    public void deleteProductsMarkedForDeletion(List<Product> list) {
        list.forEach(x -> {
            try {
                if (x instanceof AutoPart)
                    workOrders.deleteAutoPart((AutoPart) x);
                else if (x instanceof Labor)
                    workOrders.deleteLabor((Labor) x);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        list.clear();
    }

    public void deletePaymentMarkedForDeletion(List<WorkOrderPayment> list) {
        list.forEach(x -> workOrders.deletePaymentById(x.getId()));
        list.clear();
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

    public void export(ResultSet rs, XSSFSheet sheet) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        Row headerRow = sheet.createRow(0);

        // Write headers
        for (int i = 1; i <= columns; i++) {
            String columnName = metaData.getColumnName(i);
            Cell headerCell = headerRow.createCell(i - 1);
            headerCell.setCellValue(columnName);
        }

        int rowIndex = 1;

        // Write table data
        while (rs.next()) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                String type = metaData.getColumnTypeName(i+1);
                Cell cell = row.createCell(i);
                if (type.equals("DATE")) {
                    Date date = rs.getDate(i + 1);
                    if (date != null) {
                        cell.setCellValue(date.toLocalDate().toString());
                    }
                } else {
                    cell.setCellValue(rs.getString(i + 1));
                }
            }
        }
    }
}
