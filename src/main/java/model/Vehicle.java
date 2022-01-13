package model;

public class Vehicle {
    private String vin;
    private int year;
    private String make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut;

    public Vehicle(String vin, int year, String make, String model, String licensePlate, String color, String engine, String transmission, String mileageIn, String mileageOut) {
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.licensePlate = licensePlate;
        this.color = color;
        this.engine = engine;
        this.transmission = transmission;
        this.mileageIn = mileageIn;
        this.mileageOut = mileageOut;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getMileageIn() {
        return mileageIn;
    }

    public void setMileageIn(String mileageIn) {
        this.mileageIn = mileageIn;
    }

    public String getMileageOut() {
        return mileageOut;
    }

    public void setMileageOut(String mileageOut) {
        this.mileageOut = mileageOut;
    }

    public String getMileageInAndOut() {
        return mileageIn + '/' + mileageOut + " (" + (Integer.parseInt(mileageOut) - Integer.parseInt(mileageIn)) + ')';
    }

    @Override
    public String toString() {
        return getYear() + ' ' + getMake() + ' ' + getModel();
    }
}
