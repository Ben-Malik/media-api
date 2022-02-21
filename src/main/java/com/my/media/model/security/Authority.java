package com.my.media.model.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * An authority class that implements the {@link GrantedAuthority} class.
 * 
 * @author ben-maliktchamalam
 */
@SuppressWarnings("serial")
public class Authority implements GrantedAuthority {

	private final String authority;
	
	public Authority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}

}
