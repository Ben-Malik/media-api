package com.my.media.repository;

import com.my.media.model.security.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The respository for the role.
 * 
 * @author ben-maliktchamalam
 */
@Repository("RoleRepository")
public interface RoleRepository extends CrudRepository<Role, Long>{

	/**
	 * Finds the role having the given name.
	 * 
	 * @param name The name of the role looked for.
	 * @return A {@link Role} if one found, and null otherwise.
	 */
	Role findByName(String name);

}
