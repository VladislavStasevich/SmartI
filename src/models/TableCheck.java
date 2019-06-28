package models;

public class TableCheck {
    private String lastName;
    private String firstName;
    private String middleName;
    private String address;
    private String passport;

    private String manufacturer;
    private String model;
    private String engine;
    private String transmission;
    private String year;
    private String price;

    public TableCheck(String lastName, String firstName, String middleName, String address, String passport) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.address = address;
        this.passport = passport;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getEngine() {
        return engine;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getYear() {
        return year;
    }

    public String getPrice() {
        return price;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getAddress() {
        return address;
    }

    public String getPassport() {
        return passport;
    }

    public String getLastFirstMiddleName() {
        return String.format("%s %s %s", this.lastName, this.firstName, this.middleName);
    }

    public TableCheck makeClone() {
        return new TableCheck(lastName, firstName, middleName, address, passport);
    }
}
