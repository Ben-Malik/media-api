package com.my.media.service;

import com.my.media.model.User;
import com.my.media.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A service class managing the user security.
 * 
 * @author ben-maliktchamalam
 */
@Service
public class UserSecurityManager implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);		
		if (user == null) {
			throw new UsernameNotFoundException("Username not found");
		}	
		return user;
	}
	
	/**
	 * Authenticates the user having the given user name.
	 * @param username of the user to be authenticated.
	 * 
	 */
	public void authenticateUser(String username) {
		UserDetails userDetails = loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
}
