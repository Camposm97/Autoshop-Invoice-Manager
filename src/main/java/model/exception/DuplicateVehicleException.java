package model.exception;

/**
 * @brief Thrown whenever a customer is being assigned a vehicle with duplicate VIN
 */
public class DuplicateVehicleException extends Exception {
    public DuplicateVehicleException(int customerId, String vin) {
        super("customer [" + customerId + "] already has vin [" + vin + "] assigned to them");
    }
}
