package com.uphf.website.repository;

import com.uphf.website.models.*;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Répertoire pour l'opérations liée à l'objet Group
 *
 * Inutilisé
 */
public interface GroupRepository extends MongoRepository<Group, String> {

    Group findByName(String name);
}
