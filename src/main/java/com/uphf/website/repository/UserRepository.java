package com.uphf.website.repository;

import com.uphf.website.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Répertoire pour les opérations liées à l'objet User
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
    User findByName(String name);
}
