package com.uphf.website.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.uphf.website.models.*;
import com.uphf.website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Get user from mail
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Sauvegarde d'un nouvel utilisateur
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    // Pour gérer le système d'authentification, vérifie ou compare le nom d'utilisateur avec les utilisateurs de la BDD MongoDB
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            //return buildUserForAuthentication(user, authorities);
            return user;
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    /*
    // Converts user to spring.springframework.security.core.userdetails.User
    private User buildUserForAuthentication(User user) {
        return new User(user.getName(), user.getEmail(), user.getPassword(), user.getRoles());
    }
    */

    // Converts role to GrantedAuthority
    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }
}
