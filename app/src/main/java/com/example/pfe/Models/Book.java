package com.example.pfe.Models;
import com.google.firebase.database.ServerValue;
public class Book {


    private String bookKey;
    private String Author;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private String userPhoto;
    private String Theme;
    private String Spetialite;
    private float Score;
    private Object timeStamp ;

    public float getScore() {
        return Score;
    }

    public void setScore(float score) {
        this.Score = score;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public Book(String title, String description, String picture, String userId) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }
    public Book(String title,String Auteur, String description, String picture, String userId,String Sp,String th) {
        this.title = title;
        this.Author=Auteur;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.Spetialite=Sp;
        this.Theme=th;
        this.Score=0;
    }

    // make sure to have an empty constructor inside ur model class
    public Book() {
    }

    public String getBookKey() {
        return bookKey;
    }

    public void setBookKey(String bookKey) {
        this.bookKey = bookKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public String getSpetialite() {
        return Spetialite;
    }

    public void setSpetialite(String spetialite) {
        Spetialite = spetialite;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}

