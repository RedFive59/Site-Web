package com.uphf.website.controller;

import com.uphf.website.models.Music;
import com.uphf.website.models.Role;
import com.uphf.website.models.User;
import com.uphf.website.repository.MusicRepository;
import com.uphf.website.repository.RoleRepository;
import com.uphf.website.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * Contrôleur qui gère qui toutes les opérations en lien avec les musiques
 *
 * Il permet de récupèrer l'objet de la BDD
 * d'être traité
 * et renvoyer sur la page Web demandée
 */
@Controller
public class MusicController {
    //Dépot des utilisateurs
    @Autowired
    private CustomUserDetailsService userService;

    //Dépot des musiques
    @Autowired
    private MusicRepository musicRepository;

    //Dépot des rôles
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Requête pour récupérer toutes les musiques de la BDD
     *
     * Les musiques sont récupérées page par page avec une taille de page de @size.
     * On ajoute également l'utilisateur user en tant que variable Web pour différentes vérifications sur la page elle-même
     */
    @RequestMapping(value = {"/","/home", "home", "/index", "/musics"}, method = RequestMethod.GET)
    public String musicsPage(HttpServletRequest request, Model model) {

        int page = 0;//Default page number
        int size = 2; //Default page size

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }

        // Ajout de l'attribut "musics" au modèle de la page qui a chargé cette méthode
        model.addAttribute("musics", musicRepository.findAll(PageRequest.of(page, size)));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User user = ((User) principal);
            model.addAttribute("user", user);
        }
        return "home";
    }

    /**
     * Vérifie si l'utilisateur connecté à posté la musique @music
     *
     * On peut remarquer que l'on doit regarder si l'id est le même
     * car l'objet connecté peut être différent que l'objet utilisateur recherché
     * alors qu'ils seront logiquement identique en tout autre point
     */
    private Boolean isUserThatPostedIt(Music music){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User user = ((User) principal);
            return music.getUser().getId().equals(user.getId());
        }
        return false;
    }

    /**
     * Requête qui permet d'afficher la page add-music
     */
    @GetMapping("/add-music")
    public String showAddMusicForm(Music music) {
        return "add-music";
    }

    /**
     * Requête Post addmusic qui permet d'ajouter la musique si elle n'existe pas déjà
     * dans la BDD
     * Elle permet aussi de donner des indications à l'utilisateur pour savoir ce qui n'irait pas
     *
     * Elle redirige en fin à la page /home
     */
    @PostMapping("/addmusic")
    public String addMusic(@Valid Music music, BindingResult bindingResult, Model model) {
        String link = checkLink(music.getLink());
        Music musicExists = musicRepository.findByLink(link);
        if (musicExists != null) {
            bindingResult
                    .rejectValue("link", "error.link",
                            "<nobr th:text=\"#{musics.alreadyexists}\">Link already used</nobr>");
            return "add-music";
        }
        if (link == null) {
            bindingResult
                    .rejectValue("link", "error.link",
                            "<nobr th:text=\"#{musics.linkerror}\">Link error</nobr>");
            return "add-music";
        } else {
            Date date = new Date();
            music.setPostingdate(date);
            music.setLink(link);

            music.getArtistAndTitleFromSpotify();

            // Get the user logged on
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User) {
                User user = ((User) principal);
                music.addUserToUpvote(user);
                music.setUser(user);
            } else {
                music.setUser(null);
            }
            musicRepository.save(music);
            return "redirect:/home";
        }
    }

    /**
     * Converti un Lien Spotify en ID Spotify
     *
     * @param link Lien Spotify en URI, URL ou Frame
     * @return ID Spotify
     * ref : https://developer.spotify.com/documentation/web-api/ - Partie Spotify URIs and IDs
     */
    private String checkLink(String link) {
        /*
        There is 3 types of link that are available :
            https://open.spotify.com/track/6yhvInrBQThvos9CNDJV8H?si=jBUY3pBMSue-J8vYapA-Ow
            <iframe src="https://open.spotify.com/embed/track/6yhvInrBQThvos9CNDJV8H" width="300" height="380" frameborder="0" allowtransparency="true" allow="encrypted-media"></iframe>
            spotify:track:6yhvInrBQThvos9CNDJV8H
        We can see that they all got the same code, so we will return this
        If the link doesn't contains of this type, it will return null
        */
        if(link.contains("https://open.spotify.com/")){
            if(link.startsWith("<iframe src=")){
                return link.substring(50, 72);
            }
            if(link.startsWith("https://open.spotify.com/track/")){
                return link.substring(31, 53);
            }
        }
        if(link.startsWith("spotify:track:")){
            return link.substring(14, 36);
        }
        if(link.length() == 22 && !link.contains("<") && !link.contains(">") && !link.contains(":") && !link.contains("/")&& !link.contains("."))
            return link;
        return null;
    }

    /**
     * Requête GET qui vérifie que l'édition de la musique est possible
     * @param id ID de la musique
     * @param model Modèle de la page
     * @return Page vers laquelle l'utilisateur sera redirigé
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null || !isUserThatPostedIt(music)){
            return "redirect:/home";
        }
        model.addAttribute("music", music);
        return "update-music";
    }

    /**
     * Requête POST pour la mise à jour de musique
     *
     * On va ici cloner la musique récupéré depuis le formulaire
     * On va changer la date du post, récupérer le titre et le nom d'artiste, définir l'utilisateur qui l'a posté
     * puis on sauvegarde dans la BDD et ainsi on écrase les données
     */
    @PostMapping("/update/{id}")
    public String updateMusic(@PathVariable("id") String id, Music music, Model model) {
        Optional<Music> optionalEntity = musicRepository.findById(id);
        Music musicCopy = optionalEntity.get();

        if(musicCopy == null || !isUserThatPostedIt(musicCopy)){
            return "redirect:/home";
        }

        Date date = new Date();

        music.clone(musicCopy);

        music.setPostingdate(date);

        music.getArtistAndTitleFromSpotify();

        // Get the user logged on
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User user = ((User) principal);
            music.addUserToUpvote(user);
            music.setUser(user);
        } else {
            music.setUser(null);
        }
        musicRepository.save(music);
        return "redirect:/home";
    }

    /**
     * Requête GET pour la suppression de musique
     *
     * On vérifie que la musique a bien été posté par l'utilisateur qui cherche à la supprimer
     * Si c'est bien le cas on supprime de la BDD
     */
    @GetMapping("/delete/{id}")
    public String deleteMusic(@PathVariable("id") String id, Model model) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null || !isUserThatPostedIt(music)){
            return "redirect:/home";
        }
        musicRepository.deleteById(id);
        model.addAttribute("musics", musicRepository.findAll());
        return "redirect:/home";
    }

    /**
     * Reqête GET pour le partage de musique
     *
     * Ici on récupère l'id de la musqiue et ajoute cette musique à la page show-music
     * pour que le navigateur reconnaise la musique
     */
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String showPage(@PathVariable("id") String id, HttpServletRequest request, Model model) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null){
            return "redirect:/home";
        }
        model.addAttribute("music", music);
        return "show-music";
    }

    /**
     * Requête GET pour afficher les musiques que l'utilisateur a posté
     * sur son tableau de bord
     */
    @RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
    public String musicsPosted(HttpServletRequest request, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            User user = ((User) principal);
            if(Role.doesRolesContainsRole(user.getRoles(), roleRepository.findByRole("ADMIN"))) return "redirect:/admin";
            else {
                int page = 0;//Default page number
                int size = 5; //Default page size
                if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
                    page = Integer.parseInt(request.getParameter("page")) - 1;
                }
                if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
                    size = Integer.parseInt(request.getParameter("size"));
                }
                Page<Music> actualPage = musicRepository.findByUser(user, PageRequest.of(page, size));
                if(!actualPage.isEmpty()) model.addAttribute("musics", actualPage);
            }

        }
        return "dashboard";
    }

    /**
     * Requête POST pour supprimer le vote d'un utilisateur sur une musique
     *
     * On redirige en fin à la page où l'utilisateur se trouvait
     */
    @RequestMapping(value = "/removevote", method = RequestMethod.POST)
    public String removevote(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String page = request.getParameter("page");

        Music music = musicRepository.findById(id).orElse(null);
        if(music == null){
            return "redirect:/home";
        }
        music.removeCurrentUserVote();
        musicRepository.save(music);
        if(page != "") return "redirect:/musics?page="+page;
        else return "redirect:/musics?page=1";
    }

    /**
     * Requête POST pour ajouter un vote positif d'un utilisateur sur une musique
     *
     * On redirige en fin à la page où l'utilisateur se trouvait
     */
    @RequestMapping(value = "/upvote", method = RequestMethod.POST)
    public String upvote(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String page = request.getParameter("page");

        Music music = musicRepository.findById(id).orElse(null);
        if(music == null){
            return "redirect:/home";
        }
        music.addUpvote();
        musicRepository.save(music);
        if(page != "") return "redirect:/musics?page="+page;
        else return "redirect:/musics?page=1";
    }

    /**
     * Requête POST pour ajouter un vote négatif d'un utilisateur sur une musique
     *
     * On redirige en fin à la page où l'utilisateur se trouvait
     */
    @RequestMapping(value = "/downvote", method = RequestMethod.POST)
    public String downvote(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String page = request.getParameter("page");

        Music music = musicRepository.findById(id).orElse(null);
        if(music == null){
            return "redirect:/home";
        }
        music.addDownvote();
        musicRepository.save(music);
        if(page != "") return "redirect:/musics?page="+page;
        else return "redirect:/musics?page=1";
    }
}
