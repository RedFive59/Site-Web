package com.uphf.website.controller;

import com.uphf.website.models.Music;
import com.uphf.website.models.Role;
import com.uphf.website.models.User;
import com.uphf.website.repository.MusicRepository;
import com.uphf.website.repository.RoleRepository;
import com.uphf.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Controller
public class AdminController {
    //Dépot des utilisateurs
    @Autowired
    private UserRepository userRepository;

    //Dépot des musiques
    @Autowired
    private MusicRepository musicRepository;

    //Dépot des rôles
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Requête GET pour afficher le tableau de bord administrateur
     */
    @RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public String musicsPosted(HttpServletRequest request, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            User user = ((User) principal);
            //Vérification que l'opération est demandé par un admin
            if(Role.doesRolesContainsRole(user.getRoles(), roleRepository.findByRole("ADMIN"))) {
                /** Ajout de toutes les musiques du site */
                int musicpage = 0;//Default page number
                int size = 2; //Default page size
                if (request.getParameter("musicpage") != null && !request.getParameter("musicpage").isEmpty()) {
                    musicpage = Integer.parseInt(request.getParameter("musicpage")) - 1;
                }
                if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
                    size = Integer.parseInt(request.getParameter("size"));
                }
                Page<Music> actualMusicPage = musicRepository.findAll(PageRequest.of(musicpage, size));
                if(!actualMusicPage.isEmpty()) model.addAttribute("musics", actualMusicPage);

                /** Ajout de tous les utilisateurs du site */
                int userpage = 0;//Default page number
                if (request.getParameter("userpage") != null && !request.getParameter("userpage").isEmpty()) {
                    userpage = Integer.parseInt(request.getParameter("userpage")) - 1;
                }
                Page<User> actualUserPage = userRepository.findAll(PageRequest.of(userpage, size));
                if(!actualUserPage.isEmpty()) model.addAttribute("users", actualUserPage);

                //Redirection implicite vers la page admin
                return "admin";
            }
        }
        return "redirect:/home";
    }

    /**
     * Requête POST pour supprimer un utilisateur
     */
    @RequestMapping(value = {"/deleteuser/{id}"}, method = RequestMethod.GET)
    public String userDelete(@PathVariable("id") String id, HttpServletRequest request, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User){
            User user = ((User) principal);
            //Vérification que l'opération est demandé par un admin
            if(Role.doesRolesContainsRole(user.getRoles(), roleRepository.findByRole("ADMIN"))) {
                Optional<User> optionalEntity = userRepository.findById(id);
                User userToDelete = optionalEntity.get();

                if(userToDelete != null){
                    //Suppresion de toutes les musiques qu'il a posté
                    for(Music music : user.getMusics()){
                        musicRepository.delete(music);
                    }
                    //Suppression de l'utilisateur
                    userRepository.delete(userToDelete);
                }
                return "admin";
            }
        }
        return "redirect:/home";
    }
}
