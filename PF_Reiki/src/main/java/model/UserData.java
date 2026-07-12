package model;

public class UserData {

    private int id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String role;
    private String status;
    private String furigana;
    private String gender;
    private String age;
    private String bio;
    private String profileImage;

    public UserData() {}

    // ▼ id
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // ▼ username
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // ▼ email
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // ▼ password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // ▼ name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // ▼ role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // ▼ status
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ▼ furigana
    public String getFurigana() { return furigana; }
    public void setFurigana(String furigana) { this.furigana = furigana; }

    // ▼ gender
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // ▼ age
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    // ▼ bio
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    // ▼ profileImage
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
