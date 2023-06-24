package com.example.instagramclone.Models;

public class User {
    String name, profession, email, password, coverPhoto, profilePhoto, About;
    String userId;
    int followerCount;

    int followingCount;


    public User(String name, String profession, String email, String password, String coverPhoto, String profilePhoto, String about, String userId, int followerCount, int followingCount) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhoto = coverPhoto;
        this.profilePhoto = profilePhoto;
        About = about;
        this.userId = userId;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public User(String name, String profession, String email, String password, String coverPhoto, String profilePhoto, String userId, int followerCount, int followingCount) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhoto = coverPhoto;
        this.profilePhoto = profilePhoto;
        this.userId = userId;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String profession, String email, String password) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
    }

    public User(String name, String profession, String email, String password, String coverPhoto) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhoto = coverPhoto;
    }

    public User(String name, String profession, String email, String password, String coverPhoto, String profilePhoto) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhoto = coverPhoto;
        this.profilePhoto = profilePhoto;
    }

    public User(String name, String profession, String email, String password, String coverPhoto, String profilePhoto, String userId, int followerCount) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhoto = coverPhoto;
        this.profilePhoto = profilePhoto;
        this.userId = userId;
        this.followerCount = followerCount;
    }

    public User() {
    }

    public User(String name, String profession, String email, String password, String coverPhoto, String profilePhoto, String userId) {
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.password = password;
        this.coverPhoto = coverPhoto;
        this.profilePhoto = profilePhoto;
        this.userId = userId;
    }


    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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
}
