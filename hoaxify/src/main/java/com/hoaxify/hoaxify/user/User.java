package com.hoaxify.hoaxify.user;

import java.beans.Transient;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity // mapping object to database table
@Data
public class User implements UserDetails {

	// Spring expects each user to have roles. User can have multiple roles like admin, user etc.
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull(message = "{hoaxify.constraints.username.NotNull.message}")
	@Size(min=4, max=255)
	@UniqueUsername // Custom Annotation
	private String username;
	
	@NotNull
	@Size(min=4, max=255)
	private String displayName;
	
	@NotNull
	@Size(min=8, max=255)
	@Pattern(regexp= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message="{javax.validation.constraints.Pattern.message}") // one uppercase one lowercase one digit
	private String password;

	String image;
	
	@Override
	@Transient // not have these fields in database
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Role_USER");
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}

}
