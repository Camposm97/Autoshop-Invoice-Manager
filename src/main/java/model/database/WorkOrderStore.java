package model.database;

import app.App;
import model.customer.Address;
import model.customer.Customer;
import model.work_order.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkOrderStore {
    private Connection c;

    public WorkOrderStore(@NotNull Connection c) throws SQLException {
        this.c = c;
    }

    public int getMaxId() throws SQLException {
        ResultSet rs = c.createStatement().executeQuery("select max(work_order_id) from work_order");
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    // TODO Look this over and maybe delete it
    public int getNextId() {
        try {
            return getMaxId() + 1;
        } catch (SQLException e) {
            return -1;
        }
    }

    public WorkOrder add(@NotNull WorkOrder workOrder) {
        System.out.println(workOrder.getCustomer().getId());
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
                            "customer_address," +
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
            prepStmt.setString(13, workOrder.getVehicle().getYear());
            prepStmt.setString(14, workOrder.getVehicle().getMake());
            prepStmt.setString(15, workOrder.getVehicle().getModel());
            prepStmt.setString(16, workOrder.getVehicle().getLicensePlate());
            prepStmt.setString(17, workOrder.getVehicle().getColor());
            prepStmt.setString(18, workOrder.getVehicle().getEngine());
            prepStmt.setString(19, workOrder.getVehicle().getTransmission());
            prepStmt.setString(20, workOrder.getVehicle().getMileageIn());
            prepStmt.setString(21, workOrder.getVehicle().getMileageOut());
            prepStmt.execute();

            int id = getMaxId();
            workOrder.setId(id);

            // Add work_order_item row(s)
            Iterator<AutoPart> itemIterator = workOrder.autoPartIterator();
            while (itemIterator.hasNext()) {
                AutoPart item = itemIterator.next();
                addAutoPart(workOrder.getId(), item);
            }
            // Add work_order_labor row(s)
            Iterator<Labor> laborIterator = workOrder.laborIterator();
            while (laborIterator.hasNext()) {
                Labor labor = laborIterator.next();
                addLabor(workOrder.getId(), labor);
            }

            // Add work_order_payment row(s)
            Iterator<WorkOrderPayment> paymentIterator = workOrder.paymentIterator();
            while (paymentIterator.hasNext()) {
                WorkOrderPayment payment = paymentIterator.next();
                addPayment(workOrder.getId(), payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workOrder;
    }

    public WorkOrder getById(int workOrderId) {
        WorkOrder workOrder = null;
        try {
            ResultSet rsWorkOrder = c.createStatement().executeQuery(
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
                String year = rsWorkOrder.getString(14);
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

                List<AutoPart> autoParts = getAutoPartsByWorkOrderId(workOrderId);
                for (AutoPart x : autoParts) workOrder.addAutoPart(x);

                List<Labor> labors = getLaborsByWorkOrderId(workOrderId);
                for (Labor x : labors) workOrder.addLabor(x);

                List<WorkOrderPayment> payments = getPaymentsByWorkOrderId(workOrderId);
                for (WorkOrderPayment x : payments) workOrder.addPayment(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workOrder;
    }

    public int getCompletedWorkOrdersThisYear() {
        int year = LocalDate.now().getYear();
        Date date1 = Date.valueOf(LocalDate.of(year, 1, 1));
        Date date2 = Date.valueOf(LocalDate.of(year, 12, 31));
        List<WorkOrder> list = getCompletedWorkOrders();
        list.removeIf(x -> x.getDateCompleted().before(date1));
        list.removeIf(x -> x.getDateCompleted().after(date2));
        return list.size();
    }

    public int getCompletedWorkOrdersThisMonth() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        Date date1 = Date.valueOf(LocalDate.of(year, month, 1));
        Date date2 = Date.valueOf(LocalDate.of(year, month, 31));
        List<WorkOrder> list = getCompletedWorkOrders();
        list.removeIf(x -> x.getDateCompleted().before(date1));
        list.removeIf(x -> x.getDateCompleted().after(date2));
        return list.size();
    }

    public List<WorkOrder> getCompletedWorkOrders() {
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_id from work_order where date_completed is not null");
            while (rs.next()) {
                WorkOrder workOrder = getById(rs.getInt(1));
                list.add(workOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WorkOrder> getIncompletedWorkOrders() {
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_id from work_order where date_completed is null");
            while (rs.next()) {
                WorkOrder workOrder = getById((rs.getInt(1)));
                list.add(workOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WorkOrder> getRecents() {
        List<WorkOrder> list = new LinkedList<>();
        App.getRecentWorkOrders().iterator().forEachRemaining(x -> {
            WorkOrder workOrder = getById(x);
            list.add(workOrder);
        });
        return list;
    }

    public List<WorkOrder> getAll() {
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet workOrderSet = c.createStatement().executeQuery("select work_order_id from work_order");
            while (workOrderSet.next()) {
                int workOrderId = workOrderSet.getInt(1);
                WorkOrder workOrder = getById(workOrderId);
                list.add(workOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WorkOrder> filter(String firstName, String lastName, String company) {
        List<WorkOrder> list = new LinkedList<>();
        try {
             ResultSet rs = c.createStatement().executeQuery(
                     "select work_order_id from work_order where " +
                         "customer_first_name like \"" + firstName + "%\" and " +
                         "customer_last_name like \"" + lastName + "%\" and " +
                         "customer_company like \"" + company + "%\"");
             while (rs.next()) {
                 WorkOrder workOrder = getById(rs.getInt(1));
                 list.add(workOrder);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WorkOrder> filter(String firstName, String lastName, String company, String dateFilter, Date date) {
        return filter(firstName, lastName, company).stream().filter(x -> {
            switch (dateFilter) {
                case "Exactly":
                    return x.getDateCreated().equals(date);
                case "Before":
                    return x.getDateCreated().before(date);
                case "After":
                    return x.getDateCreated().after(date);
                default:
                    return true;
            }
        }).collect(Collectors.toList());
    }

    public List<WorkOrder> filter(String firstName, String lastName, String company, Date date1, Date date2) {
        return filter(firstName, lastName, company)
                .stream()
                .filter(x -> x.getDateCreated().after(date1) && x.getDateCreated().before(date2))
                .collect(Collectors.toList());
    }

    public void update(@NotNull WorkOrder workOrder) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "update work_order set " +
                            "date_created = ?, " +
                            "date_completed = ?," +
                            "customer_first_name = ?," +
                            "customer_last_name = ?," +
                            "customer_phone = ?," +
                            "customer_email = ?," +
                            "customer_company = ?," +
                            "customer_address = ?," +
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
            prepStmt.setString(13, workOrder.getVehicle().getYear());
            prepStmt.setString(14, workOrder.getVehicle().getMake());
            prepStmt.setString(15, workOrder.getVehicle().getModel());
            prepStmt.setString(16, workOrder.getVehicle().getLicensePlate());
            prepStmt.setString(17, workOrder.getVehicle().getColor());
            prepStmt.setString(18, workOrder.getVehicle().getEngine());
            prepStmt.setString(19, workOrder.getVehicle().getTransmission());
            prepStmt.setString(20, workOrder.getVehicle().getMileageIn());
            prepStmt.setString(21, workOrder.getVehicle().getMileageOut());
            prepStmt.setInt(22, workOrder.getId());
            prepStmt.execute();

            Iterator<AutoPart> autoPartIterator = workOrder.autoPartIterator();
            while (autoPartIterator.hasNext()) {
                AutoPart autoPart = autoPartIterator.next();
                if (autoPart.isNew()) {
                    addAutoPart(workOrder.getId(), autoPart);
                } else {
                    updateAutoPart(autoPart);
                }
            }
            Iterator<Labor> laborIterator = workOrder.laborIterator();
            while (laborIterator.hasNext()) {
                Labor labor = laborIterator.next();
                if (labor.isNew()) {
                    addLabor(workOrder.getId(), labor);
                } else {
                    updateLabor(labor);
                }
            }
            Iterator<WorkOrderPayment> paymentIterator = workOrder.paymentIterator();
            while (paymentIterator.hasNext()) {
                WorkOrderPayment payment = paymentIterator.next();
                if (payment.isNew()) {
                    addPayment(workOrder.getId(), payment);
                } else {
                    updatePayment(payment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(@NotNull WorkOrder workOrder) {
        try {
            c.createStatement().execute("delete from work_order where work_order_id = " + workOrder.getId());
            Iterator<AutoPart> itemIterator = workOrder.autoPartIterator();
            while (itemIterator.hasNext()) {
                deleteAutoPart(itemIterator.next());
            }
            Iterator<Labor> laborIterator = workOrder.laborIterator();
            while (laborIterator.hasNext()) {
                deleteLabor(laborIterator.next());
            }

            // Remove work order id from recent work orders
            App.getRecentWorkOrders().remove(workOrder.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAutoPart(int workOrderId, @NotNull AutoPart item) throws SQLException {
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

    public AutoPart getAutoPartById(int id) {
        AutoPart autoPart = null;
        try {
            ResultSet rs = c.createStatement().executeQuery("select * from work_order_item where work_order_item_id = " + id);
            while (rs.next()) {
                String name = rs.getString(3);
                String desc = rs.getString(4);
                double retailPrice = rs.getDouble(5);
                double listPrice = rs.getDouble(6);
                int quantity = rs.getInt(7);
                boolean taxable = rs.getBoolean(8);
                autoPart = new AutoPart(name, desc, retailPrice, listPrice, quantity, taxable);
                autoPart.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return autoPart;
        }
    }

    public List<AutoPart> getAutoPartsByWorkOrderId(int workOrderId) {
        List<AutoPart> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_item_id from work_order_item where work_order_id = " + workOrderId);
            while (rs.next()) {
                int id = rs.getInt(1);
                AutoPart autoPart = getAutoPartById(id);
                list.add(autoPart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateAutoPart(@NotNull AutoPart item) throws SQLException {
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

    public void deleteAutoPart(@NotNull AutoPart item) throws SQLException {
        c.createStatement().execute("delete from work_order_item where work_order_item_id = " + item.getId());
    }

    public void addLabor(int workOrderId, @NotNull Labor labor) throws SQLException {
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

    public Labor getLaborById(int id) {
        Labor labor = null;
        try {
            ResultSet rs = c.createStatement().executeQuery("select * from work_order_labor where work_order_labor_id = " + id);
            while (rs.next()) {
                String laborCode = rs.getString(3);
                String desc = rs.getString(4);
                double billedHrs = rs.getDouble(5);
                double rate = rs.getDouble(6);
                boolean taxable = rs.getBoolean(7);
                labor = new Labor(laborCode, desc, billedHrs, rate, taxable);
                labor.setId(id);
                return labor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
             return labor;
        }
    }

    public List<Labor> getLaborsByWorkOrderId(int workOrderId) {
        List<Labor> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_labor_id from work_order_labor " +
                    "where work_order_id = " + workOrderId);
            while (rs.next()) {
                int id = rs.getInt(1);
                Labor labor = getLaborById(id);
                list.add(labor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void updateLabor(@NotNull Labor labor) throws SQLException {
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

    public void deleteLabor(@NotNull Labor labor) throws SQLException {
        c.createStatement().execute("delete from work_order_labor where work_order_labor_id = " + labor.getId());
    }

    public void addPayment(int workOrderId, @NotNull WorkOrderPayment payment) {
        try {
            PreparedStatement stmt = c.prepareStatement("""
                    insert into work_order_payment (work_order_id, date_of_payment, type, amount) values (?, ?, ?, ?)
                    """);
            stmt.setInt(1, workOrderId);
            stmt.setDate(2, payment.getDate());
            stmt.setString(3, payment.getType().name());
            stmt.setDouble(4, payment.getAmount());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WorkOrderPayment getPaymentById(int id) {
        WorkOrderPayment workOrderPayment = null;
        try {
            ResultSet rs = c.createStatement().executeQuery("select * from work_order_payment where work_order_payment_id = " + id);
            Date date = rs.getDate(3);
            Payment type = Payment.valueOf(rs.getString(4));
            double amount = rs.getDouble(5);
            workOrderPayment = new WorkOrderPayment(id, date, type, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return workOrderPayment;
        }
    }

    public List<WorkOrderPayment> getPaymentsByWorkOrderId(int workOrderId) {
        List<WorkOrderPayment> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_payment_id from work_order_payment where work_order_id = " + workOrderId);
            while (rs.next()) {
                int id = rs.getInt(1);
                WorkOrderPayment x = getPaymentById(id);
                list.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public void updatePayment(WorkOrderPayment payment) {
        try {
            PreparedStatement stmt = c.prepareStatement("""
                    update work_order_payment set 
                    date_of_payment = ?,
                    type = ?,
                    amount = ? where work_order_payment_id = ?
                    """);
            stmt.setDate(1, payment.getDate());
            stmt.setString(2, payment.getType().name());
            stmt.setDouble(3, payment.getAmount());
            stmt.setInt(4, payment.getId());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePaymentById(int id) {
        try {
            c.createStatement().execute("delete from work_order_payment where work_order_payment_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void export(String des) throws SQLException, IOException {
        ResultSet rs1 = c.createStatement().executeQuery("select * from work_order");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet("work_order");
        DB.get().export(rs1, sheet1);

        ResultSet rs2 = c.createStatement().executeQuery("select * from work_order_item");
        XSSFSheet sheet2 = workbook.createSheet("work_order_item");
        DB.get().export(rs2, sheet2);

        ResultSet rs3 = c.createStatement().executeQuery("select * from work_order_labor");
        XSSFSheet sheet3 = workbook.createSheet("work_order_labor");
        DB.get().export(rs3, sheet3);

        ResultSet rs4 = c.createStatement().executeQuery("select * from work_order_payment");
        XSSFSheet sheet4 = workbook.createSheet("work_order_payment");
        DB.get().export(rs4, sheet4);

        FileOutputStream fos = new FileOutputStream(des);
        workbook.write(fos);
        workbook.close();
    }
}
