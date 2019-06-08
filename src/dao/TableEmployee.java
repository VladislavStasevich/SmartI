package dao;

public class TableEmployee {
    private byte[] picture;
    private String firstName;
    private String middleName;
    private String lastName;
    private String role;

    public TableEmployee(byte[] picture, String firstName, String middleName, String lastName, String role) {
        this.picture = picture;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.role = role;
    }

    public byte[] getPicture() {
        return picture;
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

    public String getRole() {
        return role;
    }
}
