package com.uphf.website.repository;


import com.uphf.website.models.Music;
import com.uphf.website.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.Set;

/**
 * Répertoire pour les opérations liées à l'objet Music
 *
 * Les méthodes se définissent toutes seuls grâce à Spring qui reconnaît la syntaxe de la méthode comme une requête en BDD
 */
public interface MusicRepository extends MongoRepository<Music, String>, PagingAndSortingRepository<Music, String> {

    Set<Music> findByTitle(String title);
    Set<Music> findByArtist(String name);
    Set<Music> findByPostingdate(Date date);
    Page<Music> findByUser(User user, PageRequest pageRequest);
    Music findByLink(String link);
}
