package com.uphf.website.controller;

import javax.validation.Valid;

import com.uphf.website.models.User;
import com.uphf.website.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private CustomUserDetailsService userService;

    // Modèle et vue pour le login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        // Vérification que l'utilisateur n'est pas déjà connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("forward:/home");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    // Modèle et vue pour le register
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        // Vérification que l'utilisateur n'est pas déjà connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("forward:/home");
        }

        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    // Modèle et vue pour enregistrer un utilisateur après le remplissage du formulaire
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "Il existe déjà un compte utilisant cette email");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "L'utilisateur a bien été enregistré");
            modelAndView.addObject("user", user);
            modelAndView.setViewName("login");

        }
        return modelAndView;
    }
}
