package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.UserData;

public class UserDao {

    // ▼ ログイン用
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
                user.setStatus(rs.getString("status"));
                user.setProfileImage(rs.getString("profile_image"));
                user.setBio(rs.getString("bio"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setFurigana(rs.getString("furigana"));
                user.setIntro(rs.getString("intro"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // ▼ プロフィール編集
    public boolean updateUser(int id, String email, String password, String name) {
        return updateUser(id, email, password, name, null);
    }

    // ▼ プロフィール編集（画像変更あり・BLOB保存）
    public boolean updateUser(int id, String email, String password, String name, byte[] imageBytes) {
        String sql = (imageBytes != null)
                ? "UPDATE users SET email = ?, password = ?, name = ?, profile_image = ? WHERE id = ?"
                : "UPDATE users SET email = ?, password = ?, name = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            int i = 1;
            pStmt.setString(i++, email);
            pStmt.setString(i++, password);
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

    // ▼ プロフィール編集（一般ユーザー自身用・フリガナ/性別/年齢/自己紹介も含めて更新）
    public boolean updateUser(int id, String email, String password, String name,
            String furigana, String gender, int age, String bio, byte[] imageBytes) {

        String sql = (imageBytes != null)
                ? "UPDATE users SET email = ?, password = ?, name = ?, furigana = ?, "
                        + "gender = ?, age = ?, bio = ?, profile_image = ? WHERE id = ?"
                : "UPDATE users SET email = ?, password = ?, name = ?, furigana = ?, "
                        + "gender = ?, age = ?, bio = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            int i = 1;
            pStmt.setString(i++, email);
            pStmt.setString(i++, password);
            pStmt.setString(i++, name);
            pStmt.setString(i++, furigana);
            pStmt.setString(i++, gender);
            pStmt.setInt(i++, age);
            pStmt.setString(i++, bio);
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

    // ▼ アカウント編集（管理者用・種別/ステータス/各項目まとめて更新）
    //    password は null または空文字なら「変更しない」として扱う
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
                pstmt.setString(i++, password);
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

    // ▼ 全ユーザー一覧
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
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ▼ ステータス切り替え
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

    // ▼ 論理削除
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

    // ▼ ID検索
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // ▼ ページング
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
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ▼ ユーザー数
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

 // ▼ アカウント追加（画像対応版）
    public boolean insertUser(UserData user, InputStream fileContent) {

        String sql = "INSERT INTO users "
                + "(username, email, password, name, role, status, profile_image, bio, age, gender, furigana, intro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword() != null ? user.getPassword() : "");
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getStatus());

            // ▼ 画像（BLOB）
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


    // ▼ プロフィール画像バイナリ取得（配信用）
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

    // ▼ 削除済みユーザー一覧
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
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ▼ 物理削除
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

    // ▼ 復活
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

    // ▼ 一般ユーザー一覧（公開画面）
    public List<UserData> getGeneralUserList() {

        List<UserData> list = new ArrayList<>();

        // ▼ profile_image(BLOB)は一覧に不要なのでSELECTしない（画像は /profileImage?id= から個別取得）。
        //    いいね数はGROUP BYではなく相関サブクエリで求める（BLOB列をGROUP BYに含めると
        //    MySQLのソートメモリを使い果たすことがあるため）。
        String sql = "SELECT u.id, u.username, u.name, u.email, u.furigana, u.gender, u.age, u.bio, "
                   + "(SELECT COUNT(*) FROM likes l WHERE l.target_user_id = u.id) AS like_count "
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
