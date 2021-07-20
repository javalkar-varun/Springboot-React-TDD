package com.hoaxify.hoaxify.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AuthUserService authUserService;
	
	// this method gives spring security objects
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// disable spring security token
		http.csrf().disable();
		
		// define authorization logic - if we don't define spring will reject with 403 forbidden
		http.httpBasic().authenticationEntryPoint(new BasicAuthenticationEntryPoint());
		
		// Add Spring security authorization checks for only for login and for others, all request are allowed to go through
		// ordering is important
		
		// Define which endpoint will be secured and which won't be
		// allowed Spring to check if request is authorized before allowing the matching url to continue
		http
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/1.0/login").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();
		
		// Rest server is expected to be stateless that means your current request is not affected by previous request. We are focused on stateless backend.
		// So configure Spring security Stateless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// user password encoder to compare incoming credentials with one stored in database, it has to do same encoding because it compares hash of 2 data 
		auth.userDetailsService(authUserService).passwordEncoder(passwordEncoder());
	}
	
	@Bean // to inject same instance in multiple places
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
