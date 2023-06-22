package model.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DateFilter;
import model.Model;
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WorkOrderStore {
    private Connection c;

    public WorkOrderStore(@NotNull Connection c) {
        this.c = c;
    }

    public ObservableList<Integer> getYearOptions() {
        List<Integer> years = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("""
                    select distinct strftime('%Y', datetime(date_completed/1000, 'unixepoch', 'localtime')) as year 
                    from work_order where date_completed is not null order by year desc
                    """);
            while (rs.next()) {
                years.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(years);
    }

    public int getMaxId() throws SQLException {
        ResultSet rs = c.createStatement().executeQuery("select max(work_order_id) from work_order");
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }

    public Integer getNextId() {
        var x = -1;
        try {
            ResultSet rs = c
                    .createStatement()
                    .executeQuery("""
                            select value from counter 
                            where id = \"work.order.id\"
                            """);
            if (rs.next()) x = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return x;
        }
    }

    public void updateNextId() throws SQLException {
        var x = getNextId() + 1;
        PreparedStatement stmt = c.prepareStatement("""
                update counter set value = ? 
                where id = 'work.order.id'
                """);
        stmt.setInt(1, x);
        stmt.execute();
    }

    public WorkOrder add(@NotNull WorkOrder workOrder) {
        try {
            // Add work_order row
            PreparedStatement prepStmt = c.prepareStatement("""
                    insert into work_order (
                    date_created,date_completed,
                    customer_id,
                    customer_first_name,customer_last_name,
                    customer_phone,customer_email,
                    customer_company,
                    customer_address,customer_city,customer_state,customer_zip,
                    vehicle_vin,vehicle_year,vehicle_make,vehicle_model,
                    vehicle_license_plate,vehicle_color,
                    vehicle_engine,vehicle_transmission,vehicle_mileage_in,vehicle_mileage_out)
                    values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """);
            prepStmt.setDate(1, workOrder.getDateCreated());
            prepStmt.setDate(2, workOrder.getDateCompleted());
            prepStmt.setInt(3, workOrder.getCustomer().getId());
            prepStmt.setString(4, workOrder.getCustomer().getFirstName());
            prepStmt.setString(5, workOrder.getCustomer().getLastName());
            prepStmt.setString(6, workOrder.getCustomer().getPhone());
            prepStmt.setString(7, workOrder.getCustomer().getEmail());
            prepStmt.setString(8, workOrder.getCustomer().getCompany());
            prepStmt.setString(9, workOrder.getCustomer().getAddress().getStreet());
            prepStmt.setString(10, workOrder.getCustomer().getAddress().getCity());
            prepStmt.setString(11, workOrder.getCustomer().getAddress().getState());
            prepStmt.setString(12, workOrder.getCustomer().getAddress().getZip());
            prepStmt.setString(13, workOrder.getVehicle().getVin());
            prepStmt.setString(14, workOrder.getVehicle().getYear());
            prepStmt.setString(15, workOrder.getVehicle().getMake());
            prepStmt.setString(16, workOrder.getVehicle().getModel());
            prepStmt.setString(17, workOrder.getVehicle().getLicensePlate());
            prepStmt.setString(18, workOrder.getVehicle().getColor());
            prepStmt.setString(19, workOrder.getVehicle().getEngine());
            prepStmt.setString(20, workOrder.getVehicle().getTransmission());
            prepStmt.setString(21, workOrder.getVehicle().getMileageIn());
            prepStmt.setString(22, workOrder.getVehicle().getMileageOut());
            prepStmt.execute();

            updateNextId(); // Update next work order id value

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

    public ObservableList<WorkOrder> getByCustomerId(int id) {
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_id from work_order where customer_id = " + id);
            while (rs.next()) list.add(getById(rs.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(list);
    }

    public WorkOrder getById(int workOrderId) {
        WorkOrder workOrder = null;
        try {
            ResultSet rs = c.createStatement().executeQuery(
                    "select * from work_order " +
                            "where work_order_id = " + workOrderId);
            if (rs.next()) {
                Date dateCreated = rs.getDate(2);
                Date dateCompleted = rs.getDate(3);
                int id = rs.getInt(4);
                String firstName = rs.getString(5);
                String lastName = rs.getString(6);
                String phone = rs.getString(7);
                String email = rs.getString(8);
                String company = rs.getString(9);
                String street = rs.getString(10);
                String city = rs.getString(11);
                String state = rs.getString(12);
                String zip = rs.getString(13);
                Address address = new Address(street, city, state, zip);
                Customer customer = new Customer(id, firstName, lastName, phone, email, company, address);

                String vin = rs.getString(14);
                String year = rs.getString(15);
                String make = rs.getString(16);
                String model = rs.getString(17);
                String licensePlate = rs.getString(18);
                String color = rs.getString(19);
                String engine = rs.getString(20);
                String transmission = rs.getString(21);
                String mileageIn = rs.getString(22);
                String mileageOut = rs.getString(23);
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
        var date1 = Date.valueOf(LocalDate.of(year, 1, 1)).getTime();
        var date2 = Date.valueOf(LocalDate.of(year, 12, 31)).getTime();
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_id from work_order where date_completed >= " + date1 + " and date_completed <= " + date2);
            while (rs.next()) list.add(getById(rs.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.size();
    }

    public int getCompletedWorkOrdersThisMonth() {
        var currentDate = LocalDate.now();
        var cal = Calendar.getInstance();
        var year = currentDate.getYear();
        var month = currentDate.getMonthValue();
        var minDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        var maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        var date1 = Date.valueOf(LocalDate.of(year, month, minDay)).getTime();
        var date2 = Date.valueOf(LocalDate.of(year, month, maxDay)).getTime();
        List<WorkOrder> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select work_order_id from work_order where date_completed >= " + date1 + " and date_completed <= " + date2);
            while (rs.next()) list.add(getById(rs.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.size();
    }

    public List<WorkOrder> getUncompletedWorkOrders() {
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

    public List<WorkOrder> getRecentWorkOrderEdits() {
        List<WorkOrder> list = new LinkedList<>();
        Model.get().recentWorkOrders().iterator().forEachRemaining(x -> {
            WorkOrder workOrder = getById(x);
            list.add(workOrder);
        });
        return list;
    }

    public List<WorkOrder> getCurrOWOs() {
        List<WorkOrder> list = new LinkedList<>();
        Model.get().currOWOs().iterator().forEachRemaining(x -> {
            WorkOrder workOrder = getById(x);
            list.add(workOrder);
        });
        return list;
    }

    /**
     * Gets the latest {LIMIT} work orders by date
     * @param LIMIT
     * @return latest work orders by date
     */
    public ObservableList<WorkOrder> getAll(final int LIMIT) {
        List<WorkOrder> list = new LinkedList<>();
        String s = "select work_order_id from work_order order by date_created desc limit ?";
        if (LIMIT <= 0)
            s = "select work_order_id from work_order order by date_created desc";
        try {
            PreparedStatement prepStmt = c.prepareStatement(s);
            if (LIMIT > 0) prepStmt.setInt(1, LIMIT);
            ResultSet workOrderSet = prepStmt.executeQuery();
            while (workOrderSet.next()) {
                int workOrderId = workOrderSet.getInt(1);
                WorkOrder workOrder = getById(workOrderId);
                list.add(workOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(list);
    }

    /**
     * Queries work order table to fetch work orders passed on given parameters
     * @param id ID of the work order. If greater than 0, the program will query the database for the work order that
     *           contains the ID, otherwise return empty list
     * @param s1 First name of customer
     * @param s2 Last name of customer
     * @param s3 Name of company
     * @param s4 Vehicle year
     * @param s5 Vehicle make
     * @param s6 Vehicle model
     * @param dateFilter Type of date filter
     * @param date1 Required if date filter is not {None}
     * @param date2 May be null, used for when date filter type is {Between}
     * @return List of work order(s), otherwise empty list if there were no matches
     * @throws SQLException
     * @see DateFilter
     */
    public List<WorkOrder> filter(int id, String s1, String s2, String s3, String s4, String s5, String s6, DateFilter dateFilter, LocalDate date1, LocalDate date2) throws SQLException {
        List<WorkOrder> list = new LinkedList<>();
        if (id > 0) {
            WorkOrder x = getById(id);
            if (x != null) return List.of(x);
        }
        boolean stmtUpdated = false;
        StringBuilder sb = new StringBuilder("select work_order_id from work_order where ");
        /* check for customer name */
        if (!s1.isEmpty()) {
            stmtUpdated = true;
            sb.append("customer_first_name like \"" + s1 + "%\" ");
        }
        if (!s2.isEmpty()) {
            if (stmtUpdated) sb.append("and ");
            stmtUpdated = true;
            sb.append("customer_last_name like \"" + s2 + "%\" ");
        }
        /* check for company name */
        if (!s3.isEmpty()) {
            if (stmtUpdated) sb.append("and ");
            stmtUpdated = true;
            sb.append("customer_company like \"" + s3 + "%\" ");
        }
        /* check for vehicle values */
        if (!s4.isEmpty()) {
            if (stmtUpdated) sb.append("and ");
            stmtUpdated = true;
            sb.append("vehicle_year like \"" + s4 + "%\" ");
        }
        if (!s5.isEmpty()) {
            if (stmtUpdated) sb.append("and ");
            stmtUpdated = true;
            sb.append("vehicle_make like \"" + s5 + "%\" ");
        }
        if (!s6.isEmpty()) {
            if (stmtUpdated) sb.append("and ");
            stmtUpdated = true;
            sb.append("vehicle_model like \"" + s6 + "%\" ");
        }
        /* check for date values */
        if (dateFilter != DateFilter.NONE && date1 != null) {
            var x= Date.valueOf(date1).getTime();
            if (dateFilter == DateFilter.BETWEEN && date2 != null) { /* between filter */
                if (stmtUpdated) sb.append(" and ");
                stmtUpdated = true;
                var y = Date.valueOf(date2).getTime();
                sb.append("date_created >= " + x + " and date_created <= " + y);
            } else { /* otherwise, unary filter */
                if (stmtUpdated) sb.append(" and ");
                stmtUpdated = true;
                switch (dateFilter) {
                    case BEFORE -> sb.append("date_created <= " + x);
                    case AFTER -> sb.append("date_created >= " + x);
                    default -> sb.append(String.format("date_created >= %s and date_created <= %s", x, x + (1000*60*60*24)));
                }
            }
        }
        if (stmtUpdated) {
            System.out.println(sb.append(" order by date_created desc"));
            System.out.println(sb);
            var prepStmt = c.prepareStatement(sb.toString());
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) list.add(getById(rs.getInt(1)));
        }
        return list;
    }

    public void update(@NotNull WorkOrder workOrder) {
        try {
            PreparedStatement prepStmt = c.prepareStatement("""
                    update work_order set 
                    date_created = ?, date_completed = ?,
                    customer_id = ?,
                    customer_first_name = ?, customer_last_name = ?,
                    customer_phone = ?,customer_email = ?,
                    customer_company = ?,
                    customer_address = ?,customer_city = ?,customer_state = ?,customer_zip = ?,
                    vehicle_vin = ?,vehicle_year = ?,vehicle_make = ?,vehicle_model = ?,
                    vehicle_license_plate = ?,vehicle_color = ?,
                    vehicle_engine = ?,vehicle_transmission = ?,vehicle_mileage_in = ?,vehicle_mileage_out = ? 
                    where work_order_id = ?
                    """);
            prepStmt.setDate(1, workOrder.getDateCreated());
            prepStmt.setDate(2, workOrder.getDateCompleted());
            prepStmt.setInt(3, workOrder.getCustomer().getId());
            prepStmt.setString(4, workOrder.getCustomer().getFirstName());
            prepStmt.setString(5, workOrder.getCustomer().getLastName());
            prepStmt.setString(6, workOrder.getCustomer().getPhone());
            prepStmt.setString(7, workOrder.getCustomer().getEmail());
            prepStmt.setString(8, workOrder.getCustomer().getCompany());
            prepStmt.setString(9, workOrder.getCustomer().getAddress().getStreet());
            prepStmt.setString(10, workOrder.getCustomer().getAddress().getCity());
            prepStmt.setString(11, workOrder.getCustomer().getAddress().getState());
            prepStmt.setString(12, workOrder.getCustomer().getAddress().getZip());
            prepStmt.setString(13, workOrder.getVehicle().getVin());
            prepStmt.setString(14, workOrder.getVehicle().getYear());
            prepStmt.setString(15, workOrder.getVehicle().getMake());
            prepStmt.setString(16, workOrder.getVehicle().getModel());
            prepStmt.setString(17, workOrder.getVehicle().getLicensePlate());
            prepStmt.setString(18, workOrder.getVehicle().getColor());
            prepStmt.setString(19, workOrder.getVehicle().getEngine());
            prepStmt.setString(20, workOrder.getVehicle().getTransmission());
            prepStmt.setString(21, workOrder.getVehicle().getMileageIn());
            prepStmt.setString(22, workOrder.getVehicle().getMileageOut());
            prepStmt.setInt(23, workOrder.getId());
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

    public void delete(@NotNull WorkOrder workOrder) {
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
            Model.get().recentWorkOrders().remove(workOrder.getId());

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
            PreparedStatement prepStmt = c.prepareStatement("select work_order_labor_id from work_order_labor where work_order_id = ?");
            prepStmt.setInt(1, workOrderId);
            ResultSet rs = prepStmt.executeQuery();
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
            PreparedStatement stmt = c.prepareStatement("insert into work_order_payment (work_order_id, date_of_payment, type, amount) values (?, ?, ?, ?)");
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
            PaymentMethod type = PaymentMethod.valueOf(rs.getString(4));
            double amount = rs.getDouble(5);
            workOrderPayment = new WorkOrderPayment(id, date, type, amount);
        } catch (IllegalArgumentException | SQLException e) {
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
