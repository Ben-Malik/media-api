package com.my.media.service;

import java.util.List;

import com.my.media.model.User;

/**
 * An interface to the user service.
 * 
 * @author ben-maliktchamalam		
 */
public interface UserManager {

	/**
	 * Grabs the user with the given id.
	 * @param id The id of the user looked for.
	 * @return The corresponding user if one found and null otherwise.
	 */
	User findById(Long id);
	
	/**
	 * Grabs the user having the given user name.
	 * @param username of the user looked for.
	 * @return The corresponding user if one found and null otherwise.
	 */
	User findByUsername(String username);
	
	/**
	 * Grabs the user having the given email address.
	 * @param email whose corresponding user is looked for.
	 * @return The corresponding user if one found and null otherwise.
	 */
	User findByEmail(String email);
		
	/**
	 * Saves the given user to the database.
	 * @param user The user to be saved.
	 */
	void save(User user);
	
	/**
	 * Creates a user with the given parameters.
	 * @param username of the user.
	 * @param password of the user.
	 * @param email of the user.
	 * @param roles of the user.
	 * @return
	 */
	User createUser(String username, String password, String email, List<String> roles);

	/**
	 * Grabs all users from the database.
	 * @return a list of users.
	 */
	List<User> getAllUsers();
}
