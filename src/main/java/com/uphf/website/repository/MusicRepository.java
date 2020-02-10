package com.uphf.website.repository;


import com.uphf.website.models.Music;
import com.uphf.website.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.Set;

public interface MusicRepository extends MongoRepository<Music, String> {

    Set<Music> findByTitle(String title);
    Set<Music> findByArtist(String name);
    Set<Music> findByDate(Date date);
    Set<Music> findByUser(User user);
}
