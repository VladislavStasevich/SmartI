package models;

public class TableCheck {
    private String lastName;
    private String firstName;
    private String middleName;
    private String address;
    private String passport;

    public TableCheck(String lastName, String firstName, String middleName, String address, String passport) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.address = address;
        this.passport = passport;
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
}
