package com.hoaxify.hoaxify.user;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.hoaxify.shared.CurrentUser;

@RestController
public class LoginController {

	// Spring security allows us to inject use object to our method
	@PostMapping("/api/1.0/login")
	Map<String, Object> handleLogin(@CurrentUser User loggedInUser) {
		return Collections.singletonMap("id", loggedInUser.getId());
	}
}
