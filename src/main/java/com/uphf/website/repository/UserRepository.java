package com.uphf.website.repository;

import com.uphf.website.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
    User findByName(String name);

}
