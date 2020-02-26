package com.uphf.website;

import com.uphf.website.models.*;
import com.uphf.website.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class WebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsiteApplication.class, args);
	}

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, GroupRepository groupRepository, MusicRepository musicRepository) {

		return args -> {

			Role adminRole = roleRepository.findByRole("ADMIN");
			if (adminRole == null) {
				Role newAdminRole = new Role();
				newAdminRole.setId(0);
				newAdminRole.setRole("ADMIN");
				roleRepository.save(newAdminRole);
			}

			Role userRole = roleRepository.findByRole("USER");
			if (userRole == null) {
				Role newUserRole = new Role();
				newUserRole.setId(1);
				newUserRole.setRole("USER");
				roleRepository.save(newUserRole);
			}

			User user = userRepository.findByName("userTest");
			if(user == null) {
				User newUser = new User();
				newUser.setEmail("email@test.fr");
				newUser.setEnabled(true);
				newUser.setPassword(bCryptPasswordEncoder.encode("pass"));
				newUser.setName("userTest");
				Role defaultUserRole = roleRepository.findByRole("USER");
				newUser.setRoles(new HashSet<>(Arrays.asList(defaultUserRole)));
				userRepository.save(newUser);
			}

			Set<Music> musicList = musicRepository.findByTitle("Physical");
			if(musicList == null || musicList.isEmpty()) {
				Music newMusic = new Music();
				newMusic.setTitle("Physical");
				newMusic.setArtist("Dua Lipa");
				newMusic.setDate(new Date());
				newMusic.setLink("spotify:track:5px6upUHM3fhOP621Edp4V");
				newMusic.setUpvote(14);
				newMusic.setDownvote(4);
				User userTest = userRepository.findByName("userTest");
				newMusic.setUser(userTest);
				musicRepository.save(newMusic);
			}

			Group group = groupRepository.findByName("groupTest");
			if(group == null) {
				Group newGroup = new Group();
				newGroup.setName("groupTest");
				User userTest = userRepository.findByName("userTest");
				newGroup.addUser(userTest);
				groupRepository.save(newGroup);
			}
		};

	}
}
