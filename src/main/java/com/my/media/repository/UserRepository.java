package com.my.media.repository;

import com.my.media.model.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository interface for the {@linkplain User} table.
 * 
 * @author ben-maliktchamalam
 */
@Repository("UserRepository")
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Finds the user having the given user name.
	 * @param username The user name of the user sought.
	 * @return The corresponding user if one found and null otherwise.
	 */
	@EntityGraph(value = "UserComplete", type=EntityGraphType.FETCH)
	User findByUsername(String username);
	
	/**
	 * Finds the user having the given email.
	 * @param email The email address of the user sought.
	 * @return The corresponding user if one found and null otherwise.
	 */
	User findByEmail(String email);
}
