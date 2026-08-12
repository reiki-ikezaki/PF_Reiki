package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.InquiryData;

public class InquiryDao {

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
                inq.setCategoryName(rs.getString("category_name"));
                inq.setEmail(rs.getString("email"));
                inq.setContent(rs.getString("content"));
                inq.setStatus(rs.getString("status"));
                inq.setCreatedAt(rs.getString("created_at"));
                inq.setUpdatedAt(rs.getString("updated_at"));

                String content = rs.getString("content");
                String shortContent = (content.length() > 10)
                        ? content.substring(0, 10)
                        : content;
                inq.setShortContent(shortContent);

                list.add(inq);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

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
                data.setCategoryName(rs.getString("category_name"));
                data.setEmail(rs.getString("email"));
                data.setContent(rs.getString("content"));
                data.setStatus(rs.getString("status"));
                data.setCreatedAt(rs.getString("created_at"));
                data.setUpdatedAt(rs.getString("updated_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

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

    public void insertInquiry(int categoryId, String content, String email) {
        String sql =
            "INSERT INTO inquiries (category_id, content, email, status) " +
            "VALUES (?, ?, ?, '未対応')";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            pstmt.setString(2, content);
            pstmt.setString(3, email);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
