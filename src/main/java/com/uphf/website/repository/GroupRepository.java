package com.uphf.website.repository;

import com.uphf.website.models.*;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {

    Group findByName(String name);
}
