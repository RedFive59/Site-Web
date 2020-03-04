package com.uphf.website.models;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "user")
public class User implements UserDetails {

    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String description;
    private String spotifyToken;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;
    @DBRef
    private Set<Role> roles;
    @DBRef
    private Set<Music> musics;

    public User(){
        this.email = "undefined";
        this.password = "undefined";
        this.name = "undefined";
        this.description = "undefined";
        this.spotifyToken = "undefined";
        this.enabled = false;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.authorities = null;
        this.roles = new HashSet<>();
        this.musics = new HashSet<>();
    }

    public User(String name, String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = "undefined";
        this.spotifyToken = "undefined";
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.roles = new HashSet<>(Arrays.asList(role));
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role.getRole()));
        this.authorities = new ArrayList<>(roles);
        this.musics = new HashSet<>();
    }

    public User(String name, String email, String password, Set<Role> userRoles){
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = "undefined";
        this.spotifyToken = "undefined";
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.roles = userRoles;
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });
        this.authorities = new ArrayList<>(roles);
        this.musics = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSpotifyToken() {
        return spotifyToken;
    }

    public void setSpotifyToken(String spotifyToken) {
        this.spotifyToken = spotifyToken;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public Set<Music> getMusics() {
        return musics;
    }

    public void setMusics(Set<Music> musics) {
        this.musics = musics;
    }

    public String toString(){
        return this.name;
    }
}
