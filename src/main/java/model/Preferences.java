package model;

import app.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Preferences {
    private static Preferences preferences;
    private static final String SRC = "preferences.config";

    public static Preferences get() {
        if (preferences == null) {
            preferences = new Preferences();
        }
        return preferences;
    }

    private String company, address, city, tempCompany, tempAddress, tempCity;
    private State state, tempState;
    private String zip, phone, repairShopId, tempZip, tempPhone, tempRepairShopId;
    private String title, tempTitle;
    private Double laborRate, tempLaborRate;
    private Double taxRate, tempTaxRate;
    private GUIScale guiScale, tempGuiScale;
    private List<PrefObservable> observables;

    private Preferences() {
        init();
        load();
        App.setScale(GUIScale.getStyleClass(this.guiScale));
    }

    public void init() {
        company = "Your Company";
        address = "123 Some Street";
        city = "Some City";
        state = State.UNKNOWN;
        zip = "11355";
        phone = "000-000-0000";
        repairShopId = "0000000";
        title = "Title Goes Here";
        laborRate = 90.0;
        taxRate = 1.08625;
        guiScale = GUIScale.Small;
        observables = new LinkedList<>();
    }

    public void load() {
        try {
            File file = new File(SRC);
            if (file.exists()) {
                Scanner in = new Scanner(file);
                while (in.hasNextLine()) {
                    String currentLine = in.nextLine();
                    String[] arr = currentLine.split("=");
                    if (arr.length == 2) {
                        String prop = arr[0];
                        String value = arr[1];
                        switch (prop) {
                            case "company":
                                this.company = value;
                                break;
                            case "address":
                                this.address = value;
                            case "city":
                                this.city = value;
                                break;
                            case "state":
                                this.state = State.valueOfName(value);
                                break;
                            case "zip":
                                this.zip = value;
                                break;
                            case "phone":
                                this.phone = value;
                                break;
                            case "repair-shop-id":
                                this.repairShopId = value;
                                break;
                            case "title":
                                this.title = value;
                                break;
                            case "labor-rate":
                                try {
                                    Double laborRate = Double.parseDouble(value);
                                    this.laborRate = laborRate;
                                } catch (NumberFormatException e) {
                                    System.out.println("Failed to parse labor rate");
                                }
                                break;
                            case "tax-rate":
                                try {
                                    Double taxRate = Double.parseDouble(value);
                                    this.taxRate = taxRate;
                                } catch (NumberFormatException e) {
                                    System.out.println("Failed to parse tax rate");
                                }
                                break;
                            case "gui-scale":
                                try {
                                    this.guiScale = GUIScale.valueOf(value);
                                    if (guiScale == null) throw new Exception();
                                } catch (Exception e) {
                                    this.guiScale = GUIScale.Small;
                                }
                                break;
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
            if (tempCompany != null) company = tempCompany;
            if (tempAddress != null) address = tempAddress;
            if (tempCity != null) city = tempCity;
            if (tempState != null) state = tempState;
            if (tempZip != null) zip = tempZip;
            if (tempPhone != null) phone = tempPhone;
            if (tempRepairShopId != null) repairShopId = tempRepairShopId;
            if (tempTitle != null) title = tempTitle;
            if (tempLaborRate != null) laborRate = tempLaborRate;
            if (tempTaxRate != null) taxRate = tempTaxRate;
            if (tempGuiScale != null) guiScale = tempGuiScale;
            PrintWriter pw = new PrintWriter(SRC);
            pw.println("company=" + company);
            pw.println("address=" + address);
            pw.println("city=" + city);
            pw.println("state=" + state);
            pw.println("zip=" + zip);
            pw.println("phone=" + phone);
            pw.println("repair-shop-id=" + repairShopId);
            pw.println("title=" + title);
            pw.println("labor-rate=" + laborRate);
            pw.println("tax-rate=" + taxRate);
            pw.println("gui-scale=" + guiScale);
            pw.close();
            System.out.println("Saved preferences");
            for (PrefObservable o : observables) {
                o.update();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            String styleClass = GUIScale.getStyleClass(this.guiScale);
            App.setScale(styleClass);
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.tempCompany = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.tempAddress = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.tempCity = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.tempState = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.tempZip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.tempPhone = phone;
    }

    public String getRepairShopId() {
        return repairShopId;
    }

    public void setRepairShopId(String repairShopId) {
        this.tempRepairShopId = repairShopId;
    }

    public String getSpecialTitle() {
        return title;
    }

    public void setSpecialTitle(String title) {
        this.tempTitle = title;
    }

    public Double getLaborRate() {
        return laborRate;
    }

    public void setLaborRate(Double laborRate) {
        this.tempLaborRate = laborRate;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public String getTaxRatePrettyString() {
        return (taxRate * 100) - 100 + " %";
    }

    public void setTaxRate(Double taxRate) {
        this.tempTaxRate = taxRate;
    }

    public GUIScale getGuiScale() {
        return guiScale;
    }

    public void setGuiScale(GUIScale guiScale) {
        this.tempGuiScale = guiScale;
    }

    public void addObserver(PrefObservable observable) {
        this.observables.add(observable);
    }

    public void removeObserver(PrefObservable observable) {
        this.observables.remove(observable);
    }
}
