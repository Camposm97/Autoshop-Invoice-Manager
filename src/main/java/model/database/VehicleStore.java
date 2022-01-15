package model.database;

import model.work_order.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class VehicleStore {
    private Connection c;
    private Statement stmt;

    public VehicleStore(@NotNull Connection c) throws SQLException {
        this.c = c;
        this.stmt = c.createStatement();
    }

    public void add(Vehicle vehicle) {
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

    public void update(Vehicle vehicle) {
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

    public void deleteById(String vin) {
        try {
            stmt.execute("delete from vehicle where vin=\"" + vin + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
