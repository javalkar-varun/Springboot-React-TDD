package com.hoaxify.hoaxify.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoaxify.hoaxify.shared.CurrentUser;

@RestController
public class LoginController {

	// Spring security allows us to inject use object to our method
	// Tell Spring to serialize Json in custom way
	@JsonView(Views.Base.class)
	@PostMapping("/api/1.0/login")
	User handleLogin(@CurrentUser User loggedInUser) {
		return loggedInUser;
	}
}
