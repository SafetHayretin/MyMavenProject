package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    static Connection connection = null;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db",
                    "root", "1234");
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}