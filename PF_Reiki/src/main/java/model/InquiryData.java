package model;

public class InquiryData {

    private int id;
    private int categoryId;
    private String categoryName;
    private String content;
    private String email;
    private String status;
    private String createdAt;  
    private String updatedAt;  

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
