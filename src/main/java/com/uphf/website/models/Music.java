package com.uphf.website.models;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import org.apache.http.ParseException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entité Music qui représente une musique
 *
 * @id Id de la musique
 * @title Titre de la musique
 * @artist Nom de ou des artistes
 * @description Description de la musique donnée par l'utilisateur (peut être vide)
 * @postingdate Date du post
 * @link Lien de la musique (sous forme d'ID Spotify)
 * @upvote Nombre de votes positifs de la musique
 * @downvote Nombre de votes négatifs de la musique
 * @user Objet utilisateur de celui qui l'a postée
 * @userUpvote Liste des utilisateurs qui ont voté positivement la musique
 * @userDownvote Liste des utilisateurs qui ont voté négativement la musique
 */
@Document(collection = "music") // Permet de déclarer cette objet en tant que doc
public class Music {
    @Id
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String artist;
    private String description;
    private Date postingdate;
    private String link;
    private Integer upvote = 0;
    private Integer downvote = 0;
    @DBRef
    private User user;
    @DBRef
    private List<User> userUpvote;

    @DBRef
    private List<User> userDownvote;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Date sous la forme "dd/MM/yyyy HH:mm:ss"
    public String getPostingdate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(postingdate);
    }

    public void setPostingdate(Date date) {
        this.postingdate = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getUpvote() {
        return upvote;
    }

    public void setUpvote(Integer upvote) {
        this.upvote = upvote;
    }

    public Integer getDownvote() {
        return downvote;
    }

    public void setDownvote(Integer downvote) {
        this.downvote = downvote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserUpvote() {
        return userUpvote;
    }

    public void setUserUpvote(List<User> userUpvote) {
        this.userUpvote = userUpvote;
        this.upvote = userUpvote.size();
    }

    public void addUserToUpvote(User user) {
        if(user != null){
            if(this.userUpvote == null){
                this.userUpvote = new ArrayList<User>();
            }
            if(this.userDownvote == null){
                this.userDownvote = new ArrayList<User>();
            }
            if(!this.userUpvote.contains(user) && !this.userDownvote.contains(user)){
                this.upvote++;
                this.userUpvote.add(user);
            }
        }
    }

    public void addUpvote() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null){
            if(this.userUpvote == null){
                this.userUpvote = new ArrayList<User>();
            }
            if(this.userDownvote == null){
                this.userDownvote = new ArrayList<User>();
            }
            if(!this.userUpvote.contains(user) && !this.userDownvote.contains(user)){
                this.upvote++;
                this.userUpvote.add(user);
            }
        }
    }
    
    public List<User> getUserDownvote() {
        return userDownvote;
    }

    public void setUserDownvote(List<User> userDownvote) {
        this.userDownvote = userDownvote;
        this.downvote = userDownvote.size();
    }

    public void addDownvote() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null){
            if(this.userUpvote == null){
                this.userUpvote = new ArrayList<User>();
            }
            if(this.userDownvote == null){
                this.userDownvote = new ArrayList<User>();
            }
            if(!this.userUpvote.contains(user) && !this.userDownvote.contains(user)){
                this.downvote++;
                this.userDownvote.add(user);
            }
        }
    }

    public void removeUserVote(User user) {
        if(user != null){
            if(this.userUpvote != null){
                for(User uservote : this.userUpvote){
                    if(uservote.getId().equals(user.getId())){
                        this.upvote--;
                        this.userUpvote.remove(uservote);
                        return;
                    }
                }
            }
            if(this.userDownvote != null){
                for(User uservote : this.userDownvote){
                    if(uservote.getId().equals(user.getId())){
                        this.downvote--;
                        this.userDownvote.remove(uservote);
                        return;
                    }
                }
            }
        }
    }

    public void removeCurrentUserVote() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        removeUserVote(user);
    }

    public void clone(Music music){
        this.artist = music.getArtist();
        this.link = music.getLink();
        this.title = music.getTitle();
    }

    /**
     * Converti un Lien Spotify (en URI, URL ou Frame) en ID Spotify
     *
     * @return ID Spotify
     * ref : https://developer.spotify.com/documentation/web-api/ - Partie Spotify URIs and IDs
     */
    private String getIdFromLink() {
        if(this.getLink().contains("https://open.spotify.com/")){
            if(this.getLink().startsWith("<iframe src=")){
                return this.getLink().substring(50, 72);
            }
            if(this.getLink().startsWith("https://open.spotify.com/track/")){
                return this.getLink().substring(31, 53);
            }
        }
        if(this.getLink().startsWith("spotify:track:")){
            return this.getLink().substring(14, 36);
        }
        if(this.getLink().length() == 22 && !this.getLink().contains("<") && !this.getLink().contains(">") && !this.getLink().contains(":") && !this.getLink().contains("/")&& !this.getLink().contains("."))
            return this.getLink();
        return null;
    }

    /**
     * Récupère le nom de ou des artistes ainsi que le titre de la musique
     *
     * On utilise ici l'API Spotify pour réaliser l'opération
     * On va d'abord construire l'API, créer des identifiants de requête (ClientCredentialsRequest).
     * Ces identifiants nous permettent de créer un AccessToken qui va nous permettre de faire notre recherche Spotify.
     * On fait alors une requête de musique (GetTrackRequest).
     * En fin de méthode, on envoie les informations dans notre musique.
     */
    public void getArtistAndTitleFromSpotify() {
        String clientId = "d01934a0c32c4402bc693582ba9855b0";
        String clientSecret = "ee29be0402294864bcc34412a676540c";
        String artists = "";

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(getLink())
                .build();

        try {
            final Track track = getTrackRequest.execute();

            ArtistSimplified artistSimplified[] = track.getArtists();

            for (int i = 0; i < artistSimplified.length; i++) {
                artists += artistSimplified[i].getName();
                if(i != artistSimplified.length-1) artists += ",";
            }

            setArtist(artists);
            setTitle(track.getName());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Méthode qui vérifie si @user a déjà voté pour cette musique ou pas
     * @param user Utilisateur qui a voté ou non
     * @return Oui si l'utilisateur est inconnu ou si il a bien voté sinon Non
     */
    public Boolean hasVoted(User user){
        Boolean bool = false;
        if(user == null){
            bool = true;
        } else {
            for(User uservote : this.getUserUpvote()){
                if(user.getId().equals(uservote.getId())) bool = true;
            }
            for(User uservote : this.getUserDownvote()){
                if(user.getId().equals(uservote.getId())) bool = true;
            }
        }
        //System.out.println(this + " - User [" + user + "] hasVoted? " + bool);
        return bool;
    }

    @Override
    public String toString(){
        return "Music [" + this.getId() + "; "
                + this.getTitle() + "; "
                + this.getArtist() + "; "
                + this.getLink() + "; "
                + this.getUser() + "; "
                + this.getPostingdate() + "; "
                + this.getUpvote() + "; "
                + this.getDownvote()
                + "]";
    }
}
