package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.DBManager;

import model.InquiryData;

public class InquiryDao {

    // ★ お問い合わせ一覧（カテゴリ JOIN 版）
    public List<InquiryData> findAll() {
        List<InquiryData> list = new ArrayList<>();

        String sql =
            "SELECT i.*, c.name AS category_name " +
            "FROM inquiries i " +
            "LEFT JOIN categories c ON i.category_id = c.id " +
            "ORDER BY i.id DESC";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                InquiryData inq = new InquiryData();

                inq.setId(rs.getInt("id"));
                inq.setCategoryId(rs.getInt("category_id"));
                inq.setContent(rs.getString("content"));
                inq.setStatus(rs.getString("status"));

                // 本文10文字だけ shortContent に入れる
                String content = rs.getString("content");
                inq.setShortContent(content.length() > 10 ? content.substring(0, 10) : content);

                // ★ JOIN で取得したカテゴリ名
                inq.setCategoryName(rs.getString("category_name"));

                list.add(inq);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ★ お問い合わせ詳細（カテゴリ JOIN 版）
    public InquiryData findById(int id) {
        InquiryData data = null;

        String sql =
            "SELECT i.*, c.name AS category_name " +
            "FROM inquiries i " +
            "LEFT JOIN categories c ON i.category_id = c.id " +
            "WHERE i.id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                data = new InquiryData();

                data.setId(rs.getInt("id"));
                data.setCategoryId(rs.getInt("category_id"));
                data.setContent(rs.getString("content"));
                data.setStatus(rs.getString("status"));

                // ★ JOIN で取得したカテゴリ名
                data.setCategoryName(rs.getString("category_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    
    // ★ ステータス更新（未対応 → 対応中 → 対応済み）
    public void updateStatus(int id, String status) {
        String sql = "UPDATE inquiries SET status = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
