package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.UserData;
import util.PasswordUtil;

public class UserDao {

    public UserData findByLogin(String username, String password) {
        UserData user = null;

        try (Connection conn = DBManager.getConnection()) {

            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, username);

            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (!PasswordUtil.matches(password, storedPassword)) {
                    return null;
                }

                int id = rs.getInt("id");

                if (!PasswordUtil.isHashed(storedPassword)) {
                    storedPassword = PasswordUtil.hash(password);
                    rehashPassword(id, storedPassword);
                }

                user = new UserData();
                user.setId(id);
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(storedPassword);
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
                user.setBio(rs.getString("bio"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setFurigana(rs.getString("furigana"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setDeletedAt(rs.getTimestamp("deleted_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    private void rehashPassword(int id, String newHash) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newHash);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateUser(int id, String email, String password, String name) {
        return updateUser(id, email, password, name, null);
    }

    public boolean updateUser(int id, String email, String password, String name, byte[] imageBytes) {
        String sql = (imageBytes != null)
                ? "UPDATE users SET email = ?, password = ?, name = ?, profile_image = ? WHERE id = ?"
                : "UPDATE users SET email = ?, password = ?, name = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            int i = 1;
            pStmt.setString(i++, email);
            pStmt.setString(i++, PasswordUtil.hash(password));
            pStmt.setString(i++, name);
            if (imageBytes != null) {
                pStmt.setBytes(i++, imageBytes);
            }
            pStmt.setInt(i++, id);

            int result = pStmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateUser(int id, String email, String password, String name,
            String furigana, String gender, int age, String bio, byte[] imageBytes) {

        boolean changePassword = password != null && !password.isEmpty();

        StringBuilder sql = new StringBuilder(
                "UPDATE users SET email = ?, name = ?, furigana = ?, gender = ?, age = ?, bio = ?");
        if (changePassword) {
            sql.append(", password = ?");
        }
        if (imageBytes != null) {
            sql.append(", profile_image = ?");
        }
        sql.append(" WHERE id = ?");

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(sql.toString())) {

            int i = 1;
            pStmt.setString(i++, email);
            pStmt.setString(i++, name);
            pStmt.setString(i++, furigana);
            pStmt.setString(i++, gender);
            pStmt.setInt(i++, age);
            pStmt.setString(i++, bio);
            if (changePassword) {
                pStmt.setString(i++, PasswordUtil.hash(password));
            }
            if (imageBytes != null) {
                pStmt.setBytes(i++, imageBytes);
            }
            pStmt.setInt(i++, id);

            int result = pStmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateAccount(UserData user, String password, byte[] imageBytes) {
        boolean changePassword = password != null && !password.isEmpty();

        StringBuilder sql = new StringBuilder(
                "UPDATE users SET role = ?, status = ?, username = ?, email = ?, name = ?, "
                        + "furigana = ?, gender = ?, age = ?, bio = ?");
        if (changePassword) {
            sql.append(", password = ?");
        }
        if (imageBytes != null) {
            sql.append(", profile_image = ?");
        }
        sql.append(" WHERE id = ?");

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int i = 1;
            pstmt.setString(i++, user.getRole());
            pstmt.setString(i++, user.getStatus());
            pstmt.setString(i++, user.getUsername());
            pstmt.setString(i++, user.getEmail());
            pstmt.setString(i++, user.getName());
            pstmt.setString(i++, user.getFurigana());
            pstmt.setString(i++, user.getGender());
            pstmt.setInt(i++, user.getAge());
            pstmt.setString(i++, user.getBio());
            if (changePassword) {
                pstmt.setString(i++, PasswordUtil.hash(password));
            }
            if (imageBytes != null) {
                pstmt.setBytes(i++, imageBytes);
            }
            pstmt.setInt(i++, user.getId());

            int result = pstmt.executeUpdate();
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
                user.setBio(rs.getString("bio"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setFurigana(rs.getString("furigana"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setDeletedAt(rs.getTimestamp("deleted_at"));
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
        String sql = "UPDATE users SET status = 'deleted', deleted_at = CURRENT_TIMESTAMP WHERE id = ?";

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
                user.setBio(rs.getString("bio"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setFurigana(rs.getString("furigana"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setDeletedAt(rs.getTimestamp("deleted_at"));
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
                user.setBio(rs.getString("bio"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setFurigana(rs.getString("furigana"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setDeletedAt(rs.getTimestamp("deleted_at"));
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

    public boolean insertUser(UserData user, InputStream fileContent) {

        String sql = "INSERT INTO users "
                + "(username, email, password, name, role, status, profile_image, bio, age, gender, furigana, intro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, PasswordUtil.hash(user.getPassword()));
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getStatus());

            if (fileContent != null) {
                pstmt.setBlob(7, fileContent);
            } else {
                pstmt.setNull(7, java.sql.Types.BLOB);
            }

            pstmt.setString(8, user.getBio());
            pstmt.setInt(9, user.getAge());
            pstmt.setString(10, user.getGender());
            pstmt.setString(11, user.getFurigana());
            pstmt.setString(12, user.getIntro());


            int result = pstmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public byte[] getProfileImageBytes(int id) {
        String sql = "SELECT profile_image FROM users WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("profile_image");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
                user.setBio(rs.getString("bio"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setFurigana(rs.getString("furigana"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                user.setDeletedAt(rs.getTimestamp("deleted_at"));
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
        String sql = "UPDATE users SET status = 'active', deleted_at = NULL WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<UserData> getGeneralUserList() {

        List<UserData> list = new ArrayList<>();

        String sql = "SELECT u.id, u.username, u.name, u.email, u.furigana, u.gender, u.age, u.bio, "
                   + "(SELECT COUNT(*) FROM likes l WHERE l.target_user_id = u.id "
                   + "AND YEAR(l.created_at) = YEAR(CURDATE()) AND MONTH(l.created_at) = MONTH(CURDATE())) "
                   + "AS like_count "
                   + "FROM users u "
                   + "WHERE u.role = 'user' AND u.status != 'deleted' "
                   + "ORDER BY u.id";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UserData u = new UserData();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setFurigana(rs.getString("furigana"));
                u.setGender(rs.getString("gender"));
                u.setAge(rs.getInt("age"));
                u.setBio(rs.getString("bio"));
                u.setLikeCount(rs.getInt("like_count"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
