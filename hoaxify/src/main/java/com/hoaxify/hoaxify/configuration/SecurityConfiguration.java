package com.hoaxify.hoaxify.configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	// this method gives spring security objects
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// disable spring security token
		http.csrf().disable();
		
		// define authorization logic - if we don't define spring will reject with 403 forbidden
		http.httpBasic();
		
		// Add Spring security authorization checks for only for login and for others, all request are allowed to go through
		// ordering is important
		
		// Define which endpoint will be secured and which won't be
		// allowed Spring to check if request is authorized before allowing the matching url to continue
		http
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/1.0/login").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();
	}
}
