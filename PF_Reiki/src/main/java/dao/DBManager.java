package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/your_db?useSSL=false&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
