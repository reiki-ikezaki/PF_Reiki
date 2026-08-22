package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.UserData;

public class UserDao {

    public UserData findByLogin(String username, String password) {
    	
    	System.out.println("【findByLogin 開始】");
        System.out.println("受け取った username = " + username);
        System.out.println("受け取った password = " + password);
        
        UserData user = null;

        try (Connection conn = DBManager.getConnection()) {
        	
        	System.out.println("DB接続成功: " + conn);

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            
            System.out.println("SQL = " + sql);
            
            pStmt.setString(1, username);
            pStmt.setString(2, password);

            ResultSet rs = pStmt.executeQuery();
            System.out.println("SQL実行完了");
            

            if (rs.next()) {
                user = new UserData();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
    

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

    public List<UserData> findAll() {
        List<UserData> list = new ArrayList<>();

        try (Connection conn = DBManager.getConnection()) {

            String sql = "SELECT * FROM users WHERE status != 'deleted' ORDER BY id";
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
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
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

    public void logicalDelete(int id) {
        String sql = "UPDATE users SET status = 'deleted' WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserData findById(int id) {
        UserData user = null;

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new UserData();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<UserData> findPage(int offset, int limit) {
        List<UserData> list = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE status != 'deleted' ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UserData user = new UserData();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countUsers() {
        int count = 0;

        String sql = "SELECT COUNT(*) FROM users WHERE status != 'deleted'";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public boolean insertUser(UserData user) {

        String sql = "INSERT INTO users "
                + "(username, email, role, status, furigana, gender, age, bio, profile_image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getStatus());
            pstmt.setString(5, user.getFurigana());
            pstmt.setString(6, user.getGender());
            pstmt.setInt(7, user.getAge()); 
            pstmt.setString(8, user.getBio());
            pstmt.setString(9, user.getProfileImage());

            int result = pstmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<UserData> findDeletedUsers() {
        List<UserData> list = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE status = 'deleted' ORDER BY id";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UserData user = new UserData();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteUserPermanent(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void restoreAccount(int id) {
        String sql = "UPDATE users SET status = 'active' WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // ▼ 一般ユーザー一覧を取得
    public List<UserData> getGeneralUserList() {

        List<UserData> list = new ArrayList<>();

        String sql = "SELECT id, name, furigana, gender, age, intro "
                   + "FROM users "
                   + "WHERE role IN ('general', 'user') AND status != 'deleted'\r\n";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UserData u = new UserData();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setFurigana(rs.getString("furigana"));
                u.setGender(rs.getString("gender"));
                u.setAge(rs.getInt("age"));
                u.setIntro(rs.getString("intro"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<UserData> findAllPublicUsers() {
        List<UserData> list = new ArrayList<>();

        String sql = "SELECT id, name, profile, like_count FROM users WHERE role = 'public'";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserData u = new UserData();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setProfile(rs.getString("profile"));
                u.setLikeCount(rs.getInt("like_count"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



}
