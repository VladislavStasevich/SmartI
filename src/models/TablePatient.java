package models;

public class TablePatient {
    private String cardNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String passport;
    private String referral;
    private String address;
    private String record;

    public TablePatient(String cardNumber, String firstName, String lastName, String middleName, String passport, String referral, String address, String record) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.passport = passport;
        this.referral = referral;
        this.address = address;
        this.record = record;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPassport() {
        return passport;
    }

    public String getReferral() {
        return referral;
    }

    public String getAddress() {
        return address;
    }

    public String getRecord() {
        return record;
    }
}
