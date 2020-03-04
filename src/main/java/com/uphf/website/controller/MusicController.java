package com.uphf.website.controller;

import com.uphf.website.models.Music;
import com.uphf.website.models.User;
import com.uphf.website.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class MusicController {

    @Autowired
    private MusicRepository musicRepository;

    // Modèle et vue pour charger la page d'accueil
    @RequestMapping(value = {"/","/home", "home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");;
        List musics = musicRepository.findAll();
        modelAndView.addObject("musics", musics);
        return modelAndView;
    }

    @GetMapping("/add-music")
    public String showAddMusicForm(Music music) {
        return "add-music";
    }

    @PostMapping("/addmusic")
    public String addMusic(@Valid Music music, BindingResult bindingResult, Model model) {
        Music musicExists = musicRepository.findByLink(music.getLink());
        if (musicExists != null) {
            bindingResult
                    .rejectValue("link", "error.link",
                            "Il existe déjà ce lien sur le site");
        }
        String link = checkLink(music.getLink());
        if (bindingResult.hasErrors() || link == null) {
            return "add-music";
        } else {
            Date date = new Date();
            music.setDate(date);
            music.setLink(link);
            music.setDownvote(0);
            // Get the user logged on
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User) {
                User user = ((User) principal);
                music.addUserToUserVote(user);
                music.setUser(user);
                music.setUpvote(1);
            } else {
                music.addUserToUserVote(null);
                music.setUser(null);
                music.setUpvote(0);
            }
            musicRepository.save(music);
            model.addAttribute("musics", musicRepository.findAll());
            return "home";
        }
    }

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
        return null;
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null){
            //throw new IllegalArgumentException("Invalid music ID :" + id);
            model.addAttribute("musics", musicRepository.findAll());
            return "home";
        }
        model.addAttribute("music", music);
        return "update-music";
    }

    @PostMapping("/update/{id}")
    public String updateMusic(@PathVariable("id") String id, @Valid Music music,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            music.setId(id);
            return "update-music";
        }

        musicRepository.save(music);
        model.addAttribute("musics", musicRepository.findAll());
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String deleteMusic(@PathVariable("id") String id, Model model) {
        Music music = musicRepository.findById(id).orElse(null);
        if(music == null){
            //throw new IllegalArgumentException("Invalid music ID :" + id);
            model.addAttribute("musics", musicRepository.findAll());
            return "home";
        }
        musicRepository.deleteById(id);
        model.addAttribute("musics", musicRepository.findAll());
        return "home";
    }
}
