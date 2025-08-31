package com.smartbet;

public class UserInfoDTO {
    private String name;
    private String email;
    private String avatarUrl;
    private String userId;

    public UserInfoDTO() {}

    public UserInfoDTO(String name, String email, String avatarUrl, String userId) {
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}