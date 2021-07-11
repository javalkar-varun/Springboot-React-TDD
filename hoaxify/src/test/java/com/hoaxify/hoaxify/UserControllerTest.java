package com.hoaxify.hoaxify;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hoaxify.hoaxify.shared.GenericResponse;
import com.hoaxify.hoaxify.user.User;
import com.hoaxify.hoaxify.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

	private static final String API_1_0_USERS = "/api/1.0/users";
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	UserRepository userRepository;
	
	@Before
	public void cleanUp() {
		userRepository.deleteAll();
	}
	
	@Test
	public void postUser_whenUserIsValid_receiveOk() {
		User user = createValidUser();
		
		ResponseEntity<Object> response = postSignup(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void postUser_whenUserIsValid_receiveSuccessMessage() {
		User user = createValidUser();
		
		ResponseEntity<GenericResponse> response = postSignup(user, GenericResponse.class);
		assertThat(response.getBody().getMessage()).isNotNull();
	}
	
	@Test
	public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
		User user = createValidUser();
		postSignup(user, GenericResponse.class);
		List<User> users= userRepository.findAll();
		User inDB = users.get(0);
		assertThat(inDB.getPassword()).isNotEqualTo(user.getPassword());
	}
	
	@Test
	public void postUser_whenUserHasNullUsername_receiveBadRequest() {
		User user = createValidUser();
		user.setUsername(null);
		ResponseEntity<Object> response = postSignup(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserHasNullDisplayName_receiveBadRequest() {
		User user = createValidUser();
		user.setDisplayName(null);
		ResponseEntity<Object> response = postSignup(user, Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	public <T> ResponseEntity<T> postSignup(Object request, Class<T> response) {
		return testRestTemplate.postForEntity(API_1_0_USERS, request, response);
	}
	
	@Test
	public void postUser_whenUserIsValid_userSavedToDatabase() {
		User user = createValidUser();
		testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
		assertThat(userRepository.count()).isEqualTo(1);
	}
	
	private User createValidUser() {
		User user = new User();
		user.setUsername("test-user");
		user.setDisplayName("test-display");
		user.setPassword("P4ssword");
		return user;
	}
}
