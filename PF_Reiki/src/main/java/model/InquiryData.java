package model;

public class InquiryData {

    private int id;
    private String categoryName;
    private String email;
    private String content;
    private String status;
    private String createdAt;
    private String updatedAt;

    public InquiryData() {}

    public InquiryData(int id, String categoryName, String email, String content,
                       String status, String createdAt, String updatedAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.email = email;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getEmail() {
        return email;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
