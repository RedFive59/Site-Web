package com.uphf.website.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Entité qui correspond au rôle de l'utilisateur
 */
@Document(collection = "role")
public class Role {

    @Id
    private String id;
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static Boolean doesRolesContainsRole(Set<Role> roles, Role role){
        for(Role roleTemp : roles){
            if(roleTemp.getId().equals(role.getId())) return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "Role ["
                + this.id + " "
                + this.role
                + "]";
    }
}
