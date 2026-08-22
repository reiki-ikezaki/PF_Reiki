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
    private int age;
    private String bio;       
    private String intro;     
    private String profileImage;
    private int likeCount;
    


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getFurigana() {
        return furigana;
    }
    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
