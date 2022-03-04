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
     * @param sortOutput The value indicating whether to sort the out coming response.
     * @return a list of {@linkplain Media} having matched the given input and of type {@linkplain com.my.media.enums.MediaType}.
     */
    List<Media> findBooksBy(String input, boolean sortOutput);

    /**
     * Finds the albums matching the given string input.
     * @param input The input to be taken into account while searching for the albums.
     * @param sortOutput The value indicating whether to sort the out coming response.
     * @return a list of {@linkplain Media} having matched the given input and of type {@linkplain com.my.media.enums.MediaType}.ALBUM.
     */
    List<Media> findAlbumsBy(String input, boolean sortOutput);

    /**
     * Finds the media (both Albums and books) matching the given input
     * @param input The input to be taken into account while searching for the albums.
     * @return a list of {@linkplain Media} having matched the given input and of type {@linkplain com.my.media.enums.MediaType} of both types.
     */
    List<Media> findAllBy(String input);
}