package store;

import dao.Item;
import models.Employee;
import dao.TableEmployee;
import models.TableItem;

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

    public static List<TableItem> getTableItems() {
        List<TableItem> array = new ArrayList<>();
        try {
            ResultSet rs = getExecute("SELECT number, name, type, count, price FROM Items");
            while (rs.next()) {
                array.add(new TableItem(
                        rs.getString("number"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("count"),
                        rs.getString("price")
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

    public static void deleteItem(String number) {
        try {
            String sql = "DELETE FROM Items WHERE number = ?";
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, number);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateItem(Item item, String number) throws SQLException {
        String sql = "UPDATE Items SET name = ?, type = ?, count = ?, price = ? WHERE number == ?";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, item.getName());
        stmt.setString(2, item.getType());
        stmt.setDouble(3, item.getCount());
        stmt.setDouble(4, item.getPrice());
        stmt.setString(5, number);
        stmt.executeUpdate();
    }

    public static void addNewItem(Item item) throws SQLException {
        String sql = "INSERT INTO Items(number, name, type, count, price) VALUES (?, ?, ?, ?, ?)";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);

        String number = String.format("%s%s", item.getType().charAt(0), item.getName().charAt(0));
        stmt.setString(1, String.format("%s-%d", number, indexOfNumber(number)));
        stmt.setString(2, item.getName());
        stmt.setString(3, item.getType());
        stmt.setDouble(4, item.getCount());
        stmt.setDouble(5, item.getPrice());
        stmt.executeUpdate();
    }

    private static int indexOfNumber(String prefix) throws SQLException {
        String sql = "SELECT MAX(CAST(substr(number, 4, 12) as integer)) + 1 as new_number_index FROM Items where number like ?";
        connect();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, prefix + "-");
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        rs.next();
        return rs.getInt("new_number_index");
    }
}
