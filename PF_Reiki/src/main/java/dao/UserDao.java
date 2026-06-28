package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.UserData;

public class UserDao {

    // ログイン用
    public UserData findByLogin(String username, String password) {
        UserData user = null;

        try (Connection conn = DBManager.getConnection()) {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, username);
            pStmt.setString(2, password);

            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                user = new UserData();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status")); // 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // ★ プロフィール更新
    public boolean updateUser(int id, String email, String password, String name) {
        try (Connection conn = DBManager.getConnection()) {

            String sql = "UPDATE users SET email = ?, password = ?, name = ? WHERE id = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, email);
            pStmt.setString(2, password);
            pStmt.setString(3, name);
            pStmt.setInt(4, id);

            int result = pStmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ★ アカウント一覧（全ユーザー取得）
    public List<UserData> findAll() {
        List<UserData> list = new ArrayList<>();

        try (Connection conn = DBManager.getConnection()) {

            String sql = "SELECT * FROM users ORDER BY id";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                UserData user = new UserData();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status")); // ← これも必要
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

   
    public void toggleStatus(int userId) {
        String sql = "UPDATE users "
                   + "SET status = CASE "
                   + "WHEN status = 'active' THEN 'banned' "
                   + "ELSE 'active' END "
                   + "WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
