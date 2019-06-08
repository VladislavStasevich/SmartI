package models;

public class Employee {
    private int id;
    private int role;
    private String firstName;
    private String lastName;
    private String middleName;
    private String login;
    private String password;

    public Employee(int id, int role, String firstName, String lastName, String middleName, String login, String password) {
        this.id = id;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public int getRole() {
        return role;
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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return String.format("Employee(%d: %s %s)", id, firstName, lastName);
    }

    public String toFirstAndLastName() {
        return String.format("%s %s", firstName, lastName);
    }
}
