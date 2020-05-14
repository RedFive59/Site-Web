package com.uphf.website.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Entité Group
 *
 * Pas utilisé finalement
 */
@Document(collection = "group")
public class Group {

    @Id
    private String id;
    private String name;
    private String description;
    @DBRef
    private Set<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if(this.users == null)
            this.users = new HashSet<User>();
        this.users.add(user);
    }

    public void removeUser(User user) { if(this.users != null && this.users.contains(user)) this.users.remove(user); }
}
