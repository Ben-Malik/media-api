package com.my.media.service;

import com.my.media.constant.APIConstants;
import com.my.media.enums.MediaType;
import com.my.media.model.Media;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

/**
 * An implementation of unit tests for the interface {@linkplain MediaManager}.
 *
 * @author ben-maliktchamalam
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MediaManagerTest {

    @Spy // mock it partially
    @InjectMocks
    private MediaManager mediaManager = new MediaManagerImpl();

    @Test
    public void testFindAllBy() {
        // Data statements
        List<Media> books = generateMedia(MediaType.BOOK);
        List<Media> albums = generateMedia(MediaType.ALBUM);

        // When statement.
        Mockito.when(mediaManager.findBooksBy(APIConstants.DEFAULT_SEARCH_INPUT, false)).thenReturn(books);
        Mockito.when(mediaManager.findAlbumsBy(APIConstants.DEFAULT_SEARCH_INPUT, false)).thenReturn(albums);

        // Call statement
        List<Media> allMedia = mediaManager.findAllBy(APIConstants.DEFAULT_SEARCH_INPUT);

        // Assertion statement
        Assert.assertEquals(allMedia.size(), 6);
        verify(mediaManager, times(1)).findBooksBy(APIConstants.DEFAULT_SEARCH_INPUT, false);
        verify(mediaManager, times(1)).findAlbumsBy(APIConstants.DEFAULT_SEARCH_INPUT, false);
    }

    /**
     * A helper method to generate a list of media given the {@linkplain MediaType}.
     * @param mediaType The type of media to be created.
     * @return a list of {@linkplain MediaType}s.
     */
    private List<Media> generateMedia(MediaType mediaType) {
       return Arrays.asList(
               new Media("kramp hub", Arrays.asList("Ben Malik"), mediaType),
               new Media("How to write clean code", Arrays.asList("Adnane Malik", "Abdel Kader"), mediaType),
               new Media("seni seviyorum", Arrays.asList("Selen Yumlu"), mediaType));
    }

}
