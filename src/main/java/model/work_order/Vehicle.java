package model.work_order;

import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

public class Vehicle implements Comparable<Vehicle> {
    private int id;
    private String vin;
    private String year;
    private String make, model, licensePlate, color, engine, transmission, mileageIn, mileageOut;

    public Vehicle(int id, String vin, String year, String make, String model, String licensePlate, String color, String engine, String transmission) {
        this(vin,year,make,model,licensePlate,color,engine,transmission);
        this.id = id;
    }


    public Vehicle(String vin, String year, String make, String model, String licensePlate, String color, String engine, String transmission, String mileageIn, String mileageOut) {
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

    public Vehicle(String vin, String year, String make, String model, String licensePlate, String color, String engine, String transmission) {
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.licensePlate = licensePlate;
        this.color = color;
        this.engine = engine;
        this.transmission = transmission;
    }

    public int getId () {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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
        try {
            return mileageIn + '/' + mileageOut + " (" + (Integer.parseInt(mileageOut) - Integer.parseInt(mileageIn)) + ')';
        } catch (NumberFormatException e) {
            System.out.println("Failed to get mileage in and out difference");
            return "";
        }
    }

    public SimpleStringProperty vinProperty() {
        return new SimpleStringProperty(vin);
    }

    public SimpleStringProperty yearProperty() {
        return new SimpleStringProperty(year);
    }

    public SimpleStringProperty makeProperty() {
        return new SimpleStringProperty(make);
    }

    public SimpleStringProperty modelProperty() {
        return new SimpleStringProperty(model);
    }

    public SimpleStringProperty licensePlateProperty() {
        return new SimpleStringProperty(licensePlate);
    }

    public SimpleStringProperty colorProperty() {
        return new SimpleStringProperty(color);
    }

    public SimpleStringProperty engineProperty() {
        return new SimpleStringProperty(engine);
    }

    public SimpleStringProperty transmissionProperty() {
        return new SimpleStringProperty(transmission);
    }

    public SimpleStringProperty mileageInProperty() {
        return new SimpleStringProperty(mileageIn);
    }

    public SimpleStringProperty mileageOutProperty() {
        return new SimpleStringProperty(mileageOut);
    }

    @Override
    public String toString() {
        return getYear() + " " + getMake() + " " + getModel();
    }

    public String toPrettyString() {
        return String.format("""
                Year: %s
                Make: %s
                Model: %s
                Engine: %s
                Transmission: %s
                """, getYear(), getMake(), getModel(), getEngine(), getTransmission());
    }

    @Override
    public int compareTo(@NotNull Vehicle o) {
        return vin.compareTo(((Vehicle) o).getVin());
    }
}
