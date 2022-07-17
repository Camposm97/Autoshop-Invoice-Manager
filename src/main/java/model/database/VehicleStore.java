package model.database;

import model.customer.OwnedVehicle;
import model.work_order.Vehicle;
import org.jetbrains.annotations.NotNull;

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
            prepStmt.setInt(2, ov.getVehicle().getYear());
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
                    "year = ?, model = ?, license_plate = ?, color = ?, engine = ?, " +
                    "transmission = ?, customer_id = ? " +
                    "where vin=\"" + vehicle.getVin() + "\"");
            prepStmt.setString(1, vehicle.getVin());
            prepStmt.setInt(2, vehicle.getYear());
            prepStmt.setString(3, vehicle.getModel());
            prepStmt.setString(4, vehicle.getLicensePlate());
            prepStmt.setString(5, vehicle.getColor());
            prepStmt.setString(6, vehicle.getEngine());
            prepStmt.setString(7, vehicle.getTransmission());
//            prepStmt.setString(8, vehicle.getMileageIn());
//            prepStmt.setString(9, vehicle.getMileageOut());

            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(String vin) {
        try {
            c.createStatement().execute("delete from vehicle where vin=\"" + vin + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vehicle getByVin(String vin) {
        Vehicle vehicle = null;
        try {
            ResultSet rs = c.createStatement().executeQuery("select * from vehicle where vin = \"" + vin + "\"");
            if (rs.next()) {
                int year = rs.getInt(2);
                String make = rs.getString(3);
                String model = rs.getString(4);
                String licensePlate = rs.getString(5);
                String color = rs.getString(6);
                String engine = rs.getString(7);
                String transmission = rs.getString(8);
//                String mileageIn = rs.getString(9);
//                String mileageOut = rs.getString(10);
                vehicle = new Vehicle(vin, year, make, model, licensePlate, color, engine, transmission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public List<Vehicle> getAll() {
        List<Vehicle> list = new LinkedList<>();
        try {
            ResultSet rs = c.createStatement().executeQuery("select vin from vehicle");
            while (rs.next()) {
                String vin = rs.getString(1);
                System.out.println(vin);
                list.add(getByVin(vin));
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
                    "select vin from vehicle " +
                            "where vin like \"" + vehicle.getVin() + "%\" " +
                            "and year like \"" + vehicle.getYear() + "%\" " +
                            "and make like \"" + vehicle.getMake() + "%\" " +
                            "and model like \"" + vehicle.getModel() + "%\" " +
                            "and license_plate like \"" + vehicle.getLicensePlate() + "%\" " +
                            "and color like \"" + vehicle.getColor() + "%\" " +
                            "and engine like \"" + vehicle.getEngine() + "%\" " +
                            "and transmission like \"" + vehicle.getTransmission() + "%\""
//                            "and mileage_in like \"" + vehicle.getMileageIn() + "%\" " +
//                            "and mileage_out like \"" + vehicle.getMileageOut() + "%\""
            );
            while (rs.next()) {
                String vin = rs.getString(1);
                list.add(getByVin(vin));
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
}
