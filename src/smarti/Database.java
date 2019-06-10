package smarti;

import dao.NewDevice;
import models.Employee;
import dao.TableEmployee;
import models.TableCheck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection = null;
    private static String dbPath = "jdbc:sqlite:%s\\test.db";

    public static void connect() throws SQLException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(String.format(dbPath, System.getProperty("user.dir")));
            } catch (SQLException e) {
                throw new SQLException(String.format("Failed to connect to '%s'", dbPath), e);
            }
        }
    }

    private static ResultSet getExecute(String sql) throws SQLException {
        connect();
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        return stmt.getResultSet();
    }

    public static List<TableEmployee> getTableEmployee() {
        List<TableEmployee> array = new ArrayList<>();
        try {
            ResultSet rs = getExecute("select image, first_name, middle_name, last_name, role " +
                    "from Employee e left join Avatar a " +
                    "on e.id == a.id;");
            while (rs.next()) {
                array.add(new TableEmployee(
                        rs.getBytes("image"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return array;
    }

    public static List<TableCheck> getTableCheck() {
        List<TableCheck> array = new ArrayList<>();
        try {
            ResultSet rs = getExecute("SELECT first_name, middle_name, last_name, address, passport FROM \"Check\"");
            while (rs.next()) {
                array.add(new TableCheck(
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("address"),
                        rs.getString("passport")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return array;
    }

    public static Employee getEmployeeByLogin(String login) throws SQLException  {
        ResultSet rs = getExecute(String.format("SELECT * FROM Employee where LOGIN = '%s'", login));
        rs.next();
        return new Employee(rs.getInt("id"),
                rs.getInt("role"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("middle_name"),
                rs.getString("login"),
                rs.getString("password"));
    }

    public static byte[] getFileByUserId(int userId) throws SQLException {
        ResultSet rs = getExecute(String.format("SELECT image FROM Avatar where ID = %d", userId));
        if (rs.next()) {
            return rs.getBytes("image");
        }
        return null;
    }

    public static List<String> getManufacturers() {
        List<String> models = new ArrayList<>();
        try {
            ResultSet rs = getExecute("SELECT DISTINCT manufacturer FROM Catalog");
            while (rs.next()) {
                models.add(rs.getString("manufacturer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    public static List<String> getModelsByManufacturer(String manufacturer) {
        List<String> models = new ArrayList<>();
        try {
            ResultSet rs = getExecute(
                    String.format("SELECT DISTINCT model FROM Catalog where manufacturer = '%s'", manufacturer)
            );
            while (rs.next()) {
                models.add(rs.getString("model"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    public static NewDevice getSmartphoneByManufacturerAndModel(String manufacturer, String model) {
        try {
            ResultSet rs = getExecute(String.format(
                    "SELECT description, image, price FROM Catalog where manufacturer = '%s' AND model = '%s'",
                    manufacturer, model
            ));
            rs.next();
            return new NewDevice(
                    rs.getBytes("image"),
                    manufacturer,
                    model,
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("Could not find '%s' with model '%s'", manufacturer, model));
        }
    }

    public static void addNewDevice(NewDevice device) throws SQLException {
        String sql = "INSERT INTO Catalog(manufacturer, model, description, price, image) VALUES (?, ?, ?, ?, ?)";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, device.getManufacturer());
        stmt.setString(2, device.getModel());
        stmt.setString(3, device.getDescription());
        stmt.setDouble(4, device.getPrice());
        stmt.setBytes(5, device.getImage());
        stmt.executeUpdate();
    }

    public static void addNewOffer(String lastName, String firstName, String middleName, String address, String passport, String model, String manufacturer) throws SQLException {
        String sql = "INSERT INTO \"Check\" (first_name, middle_name, last_name, address, passport, smartphone_id) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT id FROM Catalog where model = ? AND manufacturer = ?));";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, lastName);
        stmt.setString(2, firstName);
        stmt.setString(3, middleName);
        stmt.setString(4, address);
        stmt.setString(5, passport);
        stmt.setString(6, model);
        stmt.setString(7, manufacturer);
        stmt.executeUpdate();
    }
}
