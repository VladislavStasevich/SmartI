package store;

import dao.Patient;
import models.Employee;
import dao.TableEmployee;
import models.TablePatient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection = null;

    private static void connect() throws SQLException {
        if (connection == null) {
            String dbPath = "jdbc:sqlite:%s\\test.db";
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

    public static List<TablePatient> getTableItems() {
        List<TablePatient> array = new ArrayList<>();
        try {
            ResultSet rs = getExecute("SELECT card_number, first_name, last_name, middle_name, passport, referral, address, record FROM Patient");
            while (rs.next()) {
                array.add(new TablePatient(
                        rs.getString("card_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("middle_name"),
                        rs.getString("passport"),
                        rs.getString("referral"),
                        rs.getString("address"),
                        rs.getString("record")
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

    public static void deletePatient(String card_number) {
        try {
            String sql = "DELETE FROM Patient WHERE card_number = ?";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, card_number);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePatient(Patient patient, String cardNumber) throws SQLException {
        String sql = "UPDATE Patient SET card_number = ?, first_name = ?, last_name = ?, middle_name = ?, passport = ?, referral = ?, address = ?, record = ?" +
                " WHERE card_number == ?";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, patient.getCardNumber());
        stmt.setString(2, patient.getFirstName());
        stmt.setString(3, patient.getLastName());
        stmt.setString(4, patient.getMiddleName());
        stmt.setString(5, patient.getPassport());
        stmt.setString(6, patient.getReferral());
        stmt.setString(7, patient.getAddress());
        stmt.setString(8, patient.getRecord());
        stmt.setString(9, cardNumber);
        stmt.executeUpdate();
    }

    public static void addNewPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO Patient(card_number, first_name, middle_name, last_name, passport, referral, address, record) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, patient.getCardNumber());
        stmt.setString(2, patient.getFirstName());
        stmt.setString(3, patient.getMiddleName());
        stmt.setString(4, patient.getLastName());
        stmt.setString(5, patient.getPassport());
        stmt.setString(6, patient.getReferral());
        stmt.setString(7, patient.getAddress());
        stmt.setString(8, patient.getRecord());
        stmt.executeUpdate();
    }
}
