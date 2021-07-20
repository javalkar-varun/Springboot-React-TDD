package com.hoaxify.hoaxify;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hoaxify.hoaxify.error.ApiError;
import com.hoaxify.hoaxify.user.User;
import com.hoaxify.hoaxify.user.UserRepository;
import com.hoaxify.hoaxify.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {

	private static final String API_1_0_LOGIN = "/api/1.0/login";
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Before
	public void cleanup() {
		userRepository.deleteAll();
		testRestTemplate.getRestTemplate().getInterceptors().clear();
	}
	
	@Test
	public void postLogin_withoutUserCredentails_receiveUnauthorized() {
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void postLogin_withIncorrectCredentails_receiveUnauthorized() {
		authenticate();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void postLogin_withoutUserCredentials_receiveApiErrorWithoutValidationErrors() {
		ResponseEntity<String> response = login(String.class);
		assertThat(response.getBody().contains("validationErrors")).isFalse();
	}
	
	@Test
	public void postLogin_withIncorrectCredentials_receiveUnauthorizedWithoutWWWAuthenticationHeader() {
		authenticate();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getHeaders().containsKey("WWW-Authenticate")).isFalse();
	}
	
	@Test
	public void postLogin_withValidCredentials_receiveOk() {
		userService.save(TestUtil.createValidUser());
		authenticate();
		ResponseEntity<Object> response = login(Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void postLogin_withValidCredentials_receiveLoggedInUserId() {
		User inDB = userService.save(TestUtil.createValidUser());
		authenticate();
		ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {} );
		Map<String, Object> body = response.getBody();
		Integer id = (Integer) body.get("id");
		assertThat(id).isEqualTo(inDB.getId());
	}
	
	private <T> ResponseEntity<T> login(
			ParameterizedTypeReference<T> responseType) {
		return testRestTemplate.exchange(API_1_0_LOGIN, HttpMethod.POST, null, responseType);
	}

	@Test
	public void postLogin_withoutUserCredentails_receiveApiError() {
		authenticate();
		ResponseEntity<ApiError> response = login(ApiError.class);
		assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_LOGIN);
	}

	private void authenticate() {
		testRestTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("test-user",  "P4ssword"));
	}
	
	public <T> ResponseEntity<T> login(Class<T> responseType) {
		return testRestTemplate.postForEntity(API_1_0_LOGIN, null, responseType);
	}
}
