package com.my.media.enums;

/**
 * An enumeration indicating the type of a given {@linkplain Media}.
 * 
 * @author ben-maliktchamalam
 */
public enum MediaType {
    
    BOOK(0),
	ALBUM(1);

	@SuppressWarnings("unused")
	private int i;
	MediaType(int i) {
		this.i = i;
	}
    
}
