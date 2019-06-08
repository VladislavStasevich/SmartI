package smarti;

import models.Employee;
import dao.TableEmployee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection connection = null;
    private static String dbPath = "jdbc:sqlite:E:\\\\smarti\\СПП\\test.db";

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

    public static List<TableEmployee> getTableEmployee() throws SQLException {
        List<TableEmployee> array = new ArrayList<>();
        ResultSet rs = execute("select image, first_name, middle_name, last_name, role " +
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

        return array;
    }

    public static List<Employee> getEmployees() throws SQLException {
        List<Employee> array = new ArrayList<>();
        ResultSet rs = execute("SELECT * FROM Employee");

        while (rs.next()) {
            array.add(new Employee(rs.getInt("id"),
                    rs.getInt("role"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("middle_name"),
                    rs.getString("login"),
                    rs.getString("password")));
        }

        return array;
    }

    public static Employee getEmployeeByLogin(String login) throws SQLException  {
        ResultSet rs = execute(String.format("SELECT * FROM Employee where LOGIN = '%s'", login));
        if (rs.next()) {
            return new Employee(rs.getInt("id"),
                    rs.getInt("role"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("middle_name"),
                    rs.getString("login"),
                    rs.getString("password"));
        }
        return null;
    }

    public static byte[] getFileByUserId(int userId) throws SQLException {
        ResultSet rs = execute(String.format("SELECT image FROM Avatar where ID = %d", userId));
        if (rs.next()) {
            return rs.getBytes("image");
        }
        return null;
    }
}
