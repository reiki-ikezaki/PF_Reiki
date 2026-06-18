package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/myapp_db?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; 

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
