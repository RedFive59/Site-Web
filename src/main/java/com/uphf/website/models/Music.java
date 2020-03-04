package com.uphf.website.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "music") // Permet de déclarer cette objet en tant que doc
public class Music {

    @Id
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String artist;
    private Date date;
    private String link;
    private Integer upvote;
    private Integer downvote;
    @DBRef
    private User user;
    @DBRef
    private List<User> userVote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getUpvote() {
        return upvote;
    }

    public void setUpvote(Integer upvote) {
        this.upvote = upvote;
    }

    public Integer getDownvote() {
        return downvote;
    }

    public void setDownvote(Integer downvote) {
        this.downvote = downvote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserVote() {
        return userVote;
    }

    public void setUserVote(List<User> userVote) {
        this.userVote = userVote;
    }

    public void addUserToUserVote(User user) {
        if(this.userVote == null){
            this.userVote = new ArrayList<User>();
        }
        this.userVote.add(user);
    }
}
