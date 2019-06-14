package dao;

public class Patient {
    private String cardNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String passport;
    private String referral;
    private String address;
    private String record;

    public Patient(String cardNumber, String firstName, String middleName, String lastName, String passport, String referral, String address, String record) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
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

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
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
