package com.hoaxify.hoaxify;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.hoaxify.hoaxify.user.User;
import com.hoaxify.hoaxify.user.UserService;
@SpringBootApplication
public class HoaxifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoaxifyApplication.class, args);
	}
	
	// Whenever we ask Spring for this Bean, it will insert user to database
	// This will create 15 users when application will be started
	@Bean
	@Profile("!test") // we don't want this bean to run when running tests
	CommandLineRunner run(UserService userService) {
		return (args) -> {
			IntStream.rangeClosed(1,15)
				.mapToObj(i -> {
					User user = new User();
					user.setUsername("user"+i);
					user.setDisplayName("display"+i);
					user.setPassword("P4ssword");
					return user;
				})
				.forEach(userService::save);

		};
	}

}