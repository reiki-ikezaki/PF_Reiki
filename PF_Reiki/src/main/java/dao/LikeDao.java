package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.LikeRanking;
import model.UserData;

public class LikeDao {

    public void addLike(int userId) {

        String sql = "INSERT INTO likes (user_id, created_at) VALUES (?, NOW())";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LikeRanking> getLikeRankingThisYear() {

        List<LikeRanking> list = new ArrayList<>();

        String sql = "SELECT u.id AS id, u.name AS name, "
                   + "COUNT(l.id) AS like_count "
                   + "FROM users u "
                   + "LEFT JOIN likes l "
                   + "ON u.id = l.target_user_id "
                   + "AND YEAR(l.created_at) = YEAR(CURDATE()) "
                   + "WHERE u.status != 'deleted' "
                   + "GROUP BY u.id, u.name "
                   + "ORDER BY like_count DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LikeRanking r = new LikeRanking();
                r.setUserId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setLikeCount(rs.getInt("like_count"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<LikeRanking> getLikeRankingThisMonth() {

        List<LikeRanking> list = new ArrayList<>();

        String sql = "SELECT u.id AS id, u.name AS name, "
                   + "COUNT(l.id) AS like_count "
                   + "FROM users u "
                   + "LEFT JOIN likes l "
                   + "ON u.id = l.target_user_id "
                   + "AND YEAR(l.created_at) = YEAR(CURDATE()) "
                   + "AND MONTH(l.created_at) = MONTH(CURDATE()) "
                   + "WHERE u.status != 'deleted' "
                   + "GROUP BY u.id, u.name "
                   + "ORDER BY like_count DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LikeRanking r = new LikeRanking();
                r.setUserId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setLikeCount(rs.getInt("like_count"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<UserData> getRanking() {

        List<UserData> list = new ArrayList<>();

        String sql = "SELECT u.id, u.name, "
                   + "COUNT(l.id) AS like_count "
                   + "FROM users u "
                   + "LEFT JOIN likes l ON u.id = l.target_user_id "
                   + "WHERE u.status != 'deleted' "
                   + "GROUP BY u.id, u.name "
                   + "ORDER BY like_count DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UserData u = new UserData();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setLikeCount(rs.getInt("like_count"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public int countLikesThisMonth(int userId) {
        String sql = "SELECT COUNT(*) FROM likes "
                   + "WHERE target_user_id = ? "
                   + "AND YEAR(created_at) = YEAR(CURDATE()) "
                   + "AND MONTH(created_at) = MONTH(CURDATE())";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countLikesThisYear(int userId) {
        String sql = "SELECT COUNT(*) FROM likes "
                   + "WHERE target_user_id = ? "
                   + "AND YEAR(created_at) = YEAR(CURDATE())";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int countLikesTotal(int targetUserId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE target_user_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, targetUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean insertLike(int userId, int targetUserId) {

        String sql = "INSERT INTO likes (user_id, target_user_id, created_at) VALUES (?, ?, NOW())";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, targetUserId);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
