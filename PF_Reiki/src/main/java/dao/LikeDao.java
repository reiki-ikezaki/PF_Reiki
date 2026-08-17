package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.LikeRanking;

public class LikeDao {

    // ▼ いいねを登録
    public boolean insertLike(int userId, int targetUserId) {
        String sql = "INSERT INTO likes (user_id, target_user_id, created_at) VALUES (?, ?, NOW())";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, targetUserId);

            int result = pstmt.executeUpdate();
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ▼ 今月のいいね数
    public int countLikesThisMonth(int userId) {
        String sql = "SELECT COUNT(*) FROM likes "
                   + "WHERE user_id = ? "
                   + "AND YEAR(created_at) = YEAR(CURDATE()) "
                   + "AND MONTH(created_at) = MONTH(CURDATE())";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

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

    // ▼ 今年のいいね数
    public int countLikesThisYear(int userId) {
        String sql = "SELECT COUNT(*) FROM likes "
                   + "WHERE user_id = ? "
                   + "AND YEAR(created_at) = YEAR(CURDATE())";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

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

    // ▼ 今年のいいねランキングを取得
    public List<LikeRanking> getLikeRankingThisYear() {

        List<LikeRanking> list = new ArrayList<>();

        String sql = "SELECT u.id, u.name, COUNT(l.id) AS like_count "
                   + "FROM users u "
                   + "LEFT JOIN likes l ON u.id = l.user_id "
                   + "AND YEAR(l.created_at) = YEAR(CURDATE()) "
                   + "WHERE u.status != 'deleted' "
                   + "GROUP BY u.id, u.name "
                   + "ORDER BY like_count DESC";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
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
}
