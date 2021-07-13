package com.hoaxify.hoaxify.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity // mapping object to database tble
@Data
public class User {

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull(message = "{hoaxify.constraints.username.NotNull.message}")
	@Size(min=4, max=255)
	private String username;
	
	@NotNull
	@Size(min=4, max=255)
	private String displayName;
	
	@NotNull
	@Size(min=8, max=255)
	@Pattern(regexp= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message="{javax.validation.constraints.Pattern.message}") // one uppercase one lowercase one digit
	private String password;

}
