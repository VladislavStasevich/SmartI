package smarti;

import models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection = null;
    private static String dbPath = "jdbc:sqlite:E:\\Soft\\sqlite3\\test.db";

    public static void connect() throws SQLException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(dbPath);
            } catch (SQLException e) {
                throw new SQLException(String.format("Failed to connect to '%s'", dbPath), e);
            }
        }
    }

    private static ResultSet execute(String sql) throws SQLException {
        connect();
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        return stmt.getResultSet();
    }

    private static Employee fillEmployee(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setRole(rs.getInt("role"));
        e.setFirst_name(rs.getString("first_name"));
        e.setLast_name(rs.getString("last_name"));
        e.setMiddle_name(rs.getString("middle_name"));
        e.setLogin(rs.getString("login"));
        e.setPassword(rs.getString("password"));
        return e;
    }

    public static List<Employee> getEmployees() throws SQLException {
        List<Employee> array = new ArrayList<>();
        ResultSet rs = execute("SELECT * FROM Employee");

        while (rs.next()) {
            array.add(fillEmployee(rs));
        }

        return array;
    }

    public static Employee getEmployeeByLogin(String login) throws SQLException  {
        ResultSet rs = execute(String.format("SELECT * FROM Employee where LOGIN = '%s'", login));
        while (rs.next()) {
            return fillEmployee(rs);
        }
        return null;
    }
}
