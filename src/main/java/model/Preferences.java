package model;

import java.io.*;
import java.util.Scanner;

public class Preferences {
    private static Preferences preferences;

    public static Preferences get() {
        if (preferences == null) {
            preferences = new Preferences();
        }
        return preferences;
    }

    private File file;
    private String company, address, city;
    private State state;
    private String zip, phone, repairShopId;
    private Double laborRate;
//    private Double taxRate;

    private Preferences() {
        try {
            this.file = new File("preferences.config");
            if (!file.exists()) {
                company = "Your Company";
                address = "123 Some Street";
                city = "Some City";
                state = State.UNKNOWN;
                zip = "11355";
                phone = "000-000-0000";
                repairShopId = "0000000";
                laborRate = 90.0;
//                taxRate = 1.08625;
            } else {
                Scanner in = new Scanner(file);
                while (in.hasNextLine()) {
                    String currentLine = in.nextLine();
                    String[] arr = currentLine.split("=");
                    if (arr.length == 2) {
                        String prop = arr[0];
                        String value = arr[1];
                        switch (prop) {
                            case "company":
                                setCompany(value);
                                break;
                            case "address":
                                setAddress(value);
                            case "city":
                                setCity(value);
                                break;
                            case "state":
                                setState(State.valueOfName(value));
                                break;
                            case "zip":
                                setZip(value);
                                break;
                            case "phone":
                                setPhone(value);
                                break;
                            case "repair-shop-id":
                                setRepairShopId(value);
                                break;
                            case "labor-rate":
                                try {
                                    Double laborRate = Double.parseDouble(value);
                                    setLaborRate(laborRate);
                                } catch (NumberFormatException e) {
                                    System.out.println("Failed to parse labor rate");
                                }
                                break;
//                            case "tax-rate":
//                                try {
//                                    Double taxRate = Double.parseDouble(value);
//                                    setTaxRate(taxRate);
//                                } catch (NumberFormatException e) {
//                                    System.out.println("Failed to parse tax rate");
//                                }
//                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println("company=" + company);
            pw.println("address=" + address);
            pw.println("city=" + city);
            pw.println("state=" + state);
            pw.println("zip=" + zip);
            pw.println("phone=" + phone);
            pw.println("repair-shop-id=" + repairShopId);
            pw.println("labor-rate=" + laborRate);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRepairShopId() {
        return repairShopId;
    }

    public void setRepairShopId(String repairShopId) {
        this.repairShopId = repairShopId;
    }

    public Double getLaborRate() {
        return laborRate;
    }

    public void setLaborRate(Double laborRate) {
        this.laborRate = laborRate;
    }

//    public Double getTaxRate() { return taxRate; }
//
//    public void setTaxRate(Double taxRate) { this.taxRate = taxRate; }
}
