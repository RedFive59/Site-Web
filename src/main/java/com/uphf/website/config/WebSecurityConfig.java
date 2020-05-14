package com.uphf.website.config;

import com.uphf.website.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Référence à l'encodeur de mot de passe
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Référence au CustomAuthenticationSuccessHandler (Redirection selon les rôles)

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new CustomUserDetailsService();
    }

    // Gère le mécanisme d'authentification qui utilise UserDetailsService et l'encryption BCrypt
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    // Sécurisation des requêtes HTTP + ajout des redirections du système de login
    // https://www.baeldung.com/java-config-spring-security#HTTP=
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/musics").permitAll()
                .antMatchers("/show/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/forgotpassword").permitAll()
                .antMatchers("/dashboard/**").authenticated()
                .antMatchers("/admin/").hasAuthority("ADMIN")
                .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                .and()
                    .exceptionHandling()
                .and()
                    .csrf()
                .and()
                    .sessionManagement().maximumSessions(1).expiredUrl("/login");
    }

    // Exclusion des ressources statiques de l'app web
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/static/**", "/css/**", "/img/**", "/js/**", "/sprites/**", "/webfonts/**", "/error");
    }
}
