package models;

import javafx.scene.image.ImageView;

public class TableEmployee {
    private ImageView picture;
    private String firstName;
    private String middleName;
    private String lastName;
    private String role;

    public TableEmployee(ImageView picture, String firstName, String middleName, String lastName, String role) {
        this.picture = picture;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.role = role;
    }

    public ImageView getPicture() {
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
