package smarti;

import dao.NewCar;
import models.Employee;
import dao.TableEmployee;
import models.TableCheck;

import javax.swing.table.TableCellEditor;
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
        System.out.println("Execute SQL: [" + sql + "]");
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

    public static List<TableCheck> getTableChecksForCustomer(TableCheck customer) {
        List<TableCheck> array = new ArrayList<>();
        try {
            ResultSet rs = getExecute(String.format("SELECT manufacturer, model, engine, transmission, year, price " +
                    "FROM Catalog WHERE id in (SELECT car_id FROM \"Check\" WHERE passport = '%s')", customer.getPassport()));
            while (rs.next()) {
                TableCheck newCheck = customer.makeClone();
                newCheck.setManufacturer(rs.getString("manufacturer"));
                newCheck.setModel(rs.getString("model"));
                newCheck.setEngine(rs.getString("engine"));
                newCheck.setTransmission(rs.getString("transmission"));
                newCheck.setYear(String.valueOf(rs.getInt("year")));
                newCheck.setPrice(String.valueOf(rs.getDouble("price")));
                array.add(newCheck);
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

    public static List<String> getEnginesByManufacturerAndModel(String manufacturer, String model) {
        List<String> models = new ArrayList<>();
        try {
            ResultSet rs = getExecute(
                    String.format("SELECT DISTINCT engine FROM Catalog where manufacturer = '%s' and model = '%s'",
                            manufacturer, model));
            while (rs.next()) {
                models.add(rs.getString("engine"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    public static List<String> getTransmissionByManufacturerAndModelAndEngine(String manufacturer, String model, String engine) {
        List<String> models = new ArrayList<>();
        try {
            ResultSet rs = getExecute(
                    String.format("SELECT DISTINCT transmission FROM Catalog where manufacturer = '%s' and model = '%s' and engine = '%s'",
                            manufacturer, model, engine));
            while (rs.next()) {
                models.add(rs.getString("transmission"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    public static List<Integer> getYearByManufacturerAndModelAndEngineAndTransmission(String manufacturer, String model, String engine, String transmission) {
        List<Integer> models = new ArrayList<>();
        try {
            ResultSet rs = getExecute(
                    String.format("SELECT DISTINCT year FROM Catalog where manufacturer = '%s' and model = '%s' and engine = '%s' and transmission = '%s'",
                            manufacturer, model, engine, transmission));
            while (rs.next()) {
                models.add(rs.getInt("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    public static NewCar getCarByManufacturerAndModel(String manufacturer, String model) {
        try {
            ResultSet rs = getExecute(String.format(
                    "SELECT engine, transmission, year, description, price, image" +
                            " FROM Catalog where manufacturer = '%s' AND model = '%s'",
                    manufacturer, model
            ));
            rs.next();
            return new NewCar(
                    rs.getBytes("image"),
                    manufacturer,
                    model,
                    rs.getString("engine"),
                    rs.getString("transmission"),
                    rs.getInt("year"),
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("Could not find '%s' with model '%s'", manufacturer, model));
        }
    }

    public static void addNewCar(NewCar device) throws SQLException {
        String sql = "INSERT INTO Catalog(manufacturer, model, engine, transmission, year, description, price, image)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, device.getManufacturer());
        stmt.setString(2, device.getModel());
        stmt.setString(3, device.getEngine());
        stmt.setString(4, device.getTransmission());
        stmt.setInt(5, device.getYear());
        stmt.setString(6, device.getDescription());
        stmt.setDouble(7, device.getPrice());
        stmt.setBytes(8, device.getImage());
        stmt.executeUpdate();
    }

    public static void addNewOffer(String lastName, String firstName, String middleName, String address, String passport,
                                   String model, String manufacturer, String engine, String transmission, String year) throws SQLException {
        String sql = "INSERT INTO \"Check\" (first_name, middle_name, last_name, address, passport, car_id) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT id FROM Catalog where model = ? AND manufacturer = ? AND engine = ? and transmission = ? and year = ?));";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, middleName);
        stmt.setString(4, address);
        stmt.setString(5, passport);
        stmt.setString(6, model);
        stmt.setString(7, manufacturer);
        stmt.setString(8, engine);
        stmt.setString(9, transmission);
        stmt.setString(10, year);
        stmt.executeUpdate();
    }
}
