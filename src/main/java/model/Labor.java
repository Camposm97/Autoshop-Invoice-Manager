package model;

public class Labor implements Billable{
    private String laborCode, desc;
    private double billedHrs, rate;
    private boolean taxable;

    public Labor(String laborCode, String desc, double billedHrs, double rate, boolean taxable) {
        this.laborCode = laborCode;
        this.desc = desc;
        this.billedHrs = billedHrs;
        this.rate = rate;
        this.taxable = taxable;
    }

    public String getLaborCode() {
        return laborCode;
    }

    public void setLaborCode(String laborCode) {
        this.laborCode = laborCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getBilledHrs() {
        return billedHrs;
    }

    public void setBilledHrs(double billedHrs) {
        this.billedHrs = billedHrs;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    @Override
    public double bill() {
        return taxable ? TAX_RATE * (billedHrs * rate) : billedHrs * rate;
    }
}
