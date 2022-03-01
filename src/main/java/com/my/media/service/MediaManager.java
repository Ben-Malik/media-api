package com.my.media.service;

import java.util.List;

import com.my.media.model.Media;

/**
 * An interface to the media manager.
 * 
 * @author ben-maliktchamalam
 */
public interface MediaManager {
    
    /**
     * Finds the books matching the given string input.
     * @param input The input to be taken into account while searching for the books.
     * @return a list of {@linkplain Media} having matched the given input and of type {@linkplain MediaType.BOOK}. 
     */
    List<Media> findBooksBy(String input);

    /**
     * Finds the albums matching the given string input.
     * @param input The input to be taken into account while searching for the albums.
     * @return a list of {@linkplain Media} having matched the given input and of type {@linkplain MediaType.ALBUM}. 
     */
    List<Media> findAlbumsBy(String input);

    /**
     * Finds the media (both Albums and books) matching the given input
     * @param input The input to be taken into account while searching for the albums.
     * @return a list of {@linkplain Media} having matched the given input and of type {@linkplain MediaType.ALBUM} and {@linkplain MediaType.BOOK}. 
     */
    List<Media> findAllBy(String input);
}
