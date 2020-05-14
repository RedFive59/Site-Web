package com.uphf.website.services;

import java.util.*;

import com.uphf.website.models.*;
import com.uphf.website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service permettant de gérer les transactions de donner liées à l'utilisateur
 *
 * On gère ici la sauvegarde et le chargement d'utilisateur
 *
 * L'objet hérite de UserDetailsService car c'est un objet de SpringSecurity
 * et nous avons besoin de définir nos propres méthodes de transactions de données
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Récupère l'utilisateur de la BDD grâce à son mail
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Sauvegarde d'un nouvel utilisateur
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    // Pour gérer le système d'authentification, vérifie ou compare le nom d'utilisateur avec les utilisateurs de la BDD MongoDB
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = findUserByEmail(email);
        if(user != null) {
            return buildUserForAuthentication(user);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    // Créer un second objet Utilisateur qui a les privileges correspondant à ses rôles
    private User buildUserForAuthentication(User user) {
        return new User(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRoles());
    }
}
