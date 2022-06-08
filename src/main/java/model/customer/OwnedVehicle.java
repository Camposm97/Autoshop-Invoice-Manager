package model.customer;

import model.work_order.Vehicle;

public class OwnedVehicle {
    private int customerId;
    private Vehicle vehicle;

    public OwnedVehicle(int customerId, Vehicle vehicle) {
        this.customerId = customerId;
        this.vehicle = vehicle;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "OwnedVehicle{" +
                "customerId=" + customerId +
                ", vehicle=" + vehicle +
                '}';
    }
}
