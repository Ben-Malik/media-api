package com.my.media.controller;

import java.util.Arrays;
import java.util.List;

import com.my.media.constant.APIConstants;
import com.my.media.enums.MediaType;
import com.my.media.model.Media;
import com.my.media.service.MediaManager;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Test;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class MediaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MediaManager mediaManager;

    @InjectMocks
    private MediaController mediaController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(mediaController).build();
    }


    @Test
    public void contextLoads() {
    }

    @Test
    public void testFindAllBy() throws Exception {
        // Data statements
        List<Media> allMedia = Arrays.asList(
                new Media("kramp hub", Arrays.asList("Ben Malik"), MediaType.ALBUM),
                new Media("How to write clean code", Arrays.asList("Adnane Malik", "Abdel Kader"), MediaType.BOOK),
                new Media("seni seviyorum", Arrays.asList("Selen Yumlu"), MediaType.ALBUM));

        // When statement.
        Mockito.when(mediaManager.findAllBy(anyString())).thenReturn(allMedia);

        // Call statement & assertion statement.
        mockMvc.perform(get("/v1/allMedia/" + APIConstants.DEFAULT_SEARCH_INPUT)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].title", Matchers.is("kramp hub")))
                .andExpect(jsonPath("$[0].authors", Matchers.is(Arrays.asList("Ben Malik"))))
                .andExpect(jsonPath("$[0].type", Matchers.is(MediaType.ALBUM.toString())))
                .andExpect(jsonPath("$[1].title", Matchers.is("How to write clean code")))
                .andExpect(jsonPath("$[1].authors", Matchers.is(Arrays.asList("Adnane Malik", "Abdel Kader"))))
                .andExpect(jsonPath("$[1].type", Matchers.is(MediaType.BOOK.toString())))
                .andExpect(jsonPath("$[2].title", Matchers.is("seni seviyorum")))
                .andExpect(jsonPath("$[2].authors", Matchers.is(Arrays.asList("Selen Yumlu"))))
                .andExpect(jsonPath("$[2].type", Matchers.is(MediaType.ALBUM.toString())))
        ;
    }

    @Test
    public void ensureNotFoundStatusIsReturnedWhenTheGivenEndpointPathIsInvalid() throws Exception {
        mockMvc.perform(get("/v1/allMedia/")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
