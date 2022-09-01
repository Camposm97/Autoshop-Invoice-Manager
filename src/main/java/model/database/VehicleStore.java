package model.database;

import model.customer.OwnedVehicle;
import model.work_order.Vehicle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class VehicleStore {
    private Connection c;

    public VehicleStore(@NotNull Connection c) {
        this.c = c;
    }

    public void add(@NotNull OwnedVehicle ov) {
        try {
            PreparedStatement prepStmt = c.prepareStatement(
                    "insert into vehicle " +
                            "(vin, year, make, model, license_plate, color, engine, transmission, customer_id) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prepStmt.setString(1, ov.getVehicle().getVin());
            prepStmt.setString(2, ov.getVehicle().getYear());
            prepStmt.setString(3, ov.getVehicle().getMake());
            prepStmt.setString(4, ov.getVehicle().getModel());
            prepStmt.setString(5, ov.getVehicle().getLicensePlate());
            prepStmt.setString(6, ov.getVehicle().getColor());
            prepStmt.setString(7, ov.getVehicle().getEngine());
            prepStmt.setString(8, ov.getVehicle().getTransmission());
            prepStmt.setInt(9, ov.getCustomerId());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(@NotNull Vehicle vehicle) {
        try {
            PreparedStatement prepStmt = c.prepareStatement("update vehicle set " +
                    "vin = ?, year = ?, make = ?, model = ?, license_plate = ?, color = ?, engine = ?, " +
                    "transmission = ? " +
                    "where vehicle_id=\"" + vehicle.getId() + "\"");
            prepStmt.setString(1, vehicle.getVin());
            prepStmt.setString(2, vehicle.getYear());
            prepStmt.setString(3, vehicle.getMake());
            prepStmt.setString(4, vehicle.getModel());
            prepStmt.setString(5, vehicle.getLicensePlate());
            prepStmt.setString(6, vehicle.getColor());
            prepStmt.setString(7, vehicle.getEngine());
            prepStmt.setString(8, vehicle.getTransmission());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            c.createStatement().execute("delete from vehicle where vehicle_id=\"" + id + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByCustomerId(int id) {
        try {
            c.createStatement().execute("delete from vehicle where customer_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vehicle getById(int id) {
        Vehicle vehicle = null;
        try {
            ResultSet rs = c.createStatement().executeQuery("select * from vehicle where vehicle_id = \"" + id + "\"");
            if (rs.next()) {
                String vin = rs.getString("vin");
                String year = rs.getString("year");
                String make = rs.getString("make");
                String model = rs.getString("model");
                String licensePlate = rs.getString("license_plate");
                String color = rs.getString("color");
                String engine = rs.getString("engine");
                String transmission = rs.getString("transmission");
                vehicle = new Vehicle(id, vin, year, make, model, licensePlate, color, engine, transmission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public List<Vehicle> getAll() {
        List<Vehicle> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select vehicle_id from vehicle");
            while (rs.next()) {
                int id = rs.getInt(1);
                list.add(getById(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Vehicle> getAllByCustomerId(int customerId) {
        List<Vehicle> list = new LinkedList<>();
        try {
            PreparedStatement prepStmt = c.prepareStatement("select vehicle_id from vehicle where customer_id = ?");
            prepStmt.setInt(1, customerId);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                Vehicle v = getById(id);
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Vehicle> filter(Vehicle vehicle) {
        List<Vehicle> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery(
                    "select vehicle_id from vehicle " +
                            "where vin like \"" + vehicle.getVin() + "%\" " +
                            "and year like \"" + vehicle.getYear() + "%\" " +
                            "and make like \"" + vehicle.getMake() + "%\" " +
                            "and model like \"" + vehicle.getModel() + "%\" " +
                            "and license_plate like \"" + vehicle.getLicensePlate() + "%\" " +
                            "and color like \"" + vehicle.getColor() + "%\" " +
                            "and engine like \"" + vehicle.getEngine() + "%\" " +
                            "and transmission like \"" + vehicle.getTransmission() + "%\""
            );
            while (rs.next()) {
                int id = rs.getInt(1);
                list.add(getById(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Vehicle> filterWithCustomerId(Vehicle vehicle, int customerId) {
        List<Vehicle> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery(
                    "select vehicle_id from vehicle " +
                            "where vin like \"" + vehicle.getVin() + "%\" " +
                            "and year like \"" + vehicle.getYear() + "%\" " +
                            "and make like \"" + vehicle.getMake() + "%\" " +
                            "and model like \"" + vehicle.getModel() + "%\" " +
                            "and license_plate like \"" + vehicle.getLicensePlate() + "%\" " +
                            "and color like \"" + vehicle.getColor() + "%\" " +
                            "and engine like \"" + vehicle.getEngine() + "%\" " +
                            "and transmission like \"" + vehicle.getTransmission() + "%\" " +
                            "and customer_id = " + customerId
            );
            while (rs.next()) {
                int id = rs.getInt(1);
                list.add(getById(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getUniqueYear() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct year from vehicle");
            while (rs.next()) {
                list.add(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> getUniqueMake() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct make from vehicle");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> getUniqueModel() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct model from vehicle");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> getUniqueColor() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct color from vehicle");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> getUniqueEngine() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct engine from vehicle");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<String> getUniqueTransmission() {
        List<String> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select distinct transmission from vehicle");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void export(String des) throws SQLException, IOException {
        ResultSet rs = c.createStatement().executeQuery("select * from vehicle");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("vehicle");

        DB.get().export(rs, sheet);

        FileOutputStream fos = new FileOutputStream(des);
        workbook.write(fos);
        workbook.close();
    }
}
