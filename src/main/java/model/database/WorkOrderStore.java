package model.database;

import app.App;
import model.customer.Address;
import model.customer.Customer;
import model.work_order.*;
import org.jetbrains.annotations.NotNull;

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

                ResultSet rsItem = c.createStatement().executeQuery("select * from work_order_item " +
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
                    workOrder.addAutoPart(item);
                }

                ResultSet rsLabor = c.createStatement().executeQuery("select * from work_order_labor " +
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
        for (int x :  App.getRecentWorkOrders()) {
            WorkOrder workOrder = getById(x);
            list.add(workOrder);
        }
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

    public List<WorkOrder> filter(String firstName, String lastName, String company, String dateFilter, Date date) {
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_id from work_order where " +
                    "customer_first_name like \"" + firstName + "%\" " +
                    "and customer_last_name like \"" + lastName + "%\" " +
                    "and customer_company like \"" + company + "%\"");
            while (rs.next()) {
                list.add(getById(rs.getInt(1)));
                list = list.stream().filter(workOrder -> {
                   switch (dateFilter) {
                       case "Exactly":
                           return workOrder.getDateCreated().equals(date);
                       case "Before":
                           return workOrder.getDateCreated().before(date);
                       case "After":
                           return workOrder.getDateCreated().after(date);
                       default:
                           return true;
                   }
                }).collect(Collectors.toList());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(@NotNull WorkOrder workOrder) {
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
            prepStmt.setString(12, workOrder.getVehicle().getYear());
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
                    updateAutoPart(autoPart);
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

            /*
            Check if the work order id is in our recent work orders, if it is then
            remove it from the recent work orders list
             */
            if (App.getRecentWorkOrders().contains(workOrder.getId())) {
                App.getRecentWorkOrders().removeFirstOccurrence(workOrder.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAutoPart(int workOrderId, @NotNull AutoPart item) throws SQLException {
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

    public void addPayment(@NotNull WorkOrderPayment payment) {
        try {
            PreparedStatement stmt = c.prepareStatement("""
                    insert into work_order_payment (work_order_id, date_of_payment, type, amount) values (?, ?, ?, ?)
                    """);
            stmt.setInt(1, payment.getWorkOrderId());
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
            int workOrderId = rs.getInt(1);
            Date date = rs.getDate(2);
            Payment type = Payment.valueOf(rs.getString(3));
            double amount = rs.getDouble(4);
            workOrderPayment = new WorkOrderPayment(id, workOrderId, date, type, amount);
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
                    type = ?
                    amount = ?,
                    """);
            stmt.setDate(1, payment.getDate());
            stmt.setString(2, payment.getType().name());
            stmt.setDouble(3, payment.getAmount());
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
}
