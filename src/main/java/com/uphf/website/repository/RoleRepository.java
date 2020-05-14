package com.uphf.website.repository;

import com.uphf.website.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

/**
 * Répertoire pour l'opération liée à l'objet Role
 */
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
