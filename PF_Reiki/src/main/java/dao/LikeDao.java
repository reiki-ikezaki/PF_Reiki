package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.LikeRanking;

public class LikeDao {

    // ▼ 今年のいいね数を取得
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
 // ▼ 今年のいいねランキングを取得
    public List<LikeRanking> getLikeRankingThisYear() {

        List<LikeRanking> list = new ArrayList<>();

        String sql = "SELECT u.name AS username, "
                   + "       COUNT(l.id) AS likeCount "
                   + "FROM users u "
                   + "LEFT JOIN likes l "
                   + "  ON u.id = l.user_id "
                   + " AND YEAR(l.created_at) = YEAR(CURDATE()) "
                   + "WHERE u.status != 'deleted' "
                   + "GROUP BY u.id, u.name "
                   + "ORDER BY likeCount DESC";

        try (Connection con = DBManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LikeRanking ranking = new LikeRanking();
                ranking.setUsername(rs.getString("username"));
                ranking.setLikeCount(rs.getInt("likeCount"));
                list.add(ranking);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    
}

