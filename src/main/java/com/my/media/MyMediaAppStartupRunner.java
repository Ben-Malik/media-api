package com.my.media;

import java.util.Arrays;

import com.my.media.service.UserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A component class run to create an admin user if one doesn't exists yet.
 * 
 * @author ben-maliktchamalam
 */
@Component
public class MyMediaAppStartupRunner implements CommandLineRunner{
	
	@Autowired
	private UserManager userManager;
	
	@Override
	public void run(String... args) throws Exception {
		userManager.createUser("admin", "admin98!_", "admin@admin.com", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));	
	}

}
