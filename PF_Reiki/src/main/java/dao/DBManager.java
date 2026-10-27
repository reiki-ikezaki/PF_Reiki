package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    private static final String HOST     = env("DB_HOST", "localhost");
    private static final String PORT     = env("DB_PORT", "3306");
    private static final String DB_NAME  = env("DB_NAME", "myapp_db");
    private static final String USER     = env("DB_USER", "root");
    private static final String PASS     = env("DB_PASSWORD", "root");

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
