package store;

public enum EmployeeRole {
    ADMIN(0);

    private int role;

    EmployeeRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }
}
