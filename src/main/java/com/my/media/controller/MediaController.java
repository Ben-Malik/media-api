package com.my.media.controller;

import java.util.Arrays;
import java.util.List;

import com.my.media.model.Media;
import com.my.media.service.MediaManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MediaController {
    
    @Autowired
    private MediaManager mediaManager;

    @GetMapping("/api/album/search/{input}")
    public @ResponseBody List<Media> getAlbums(@PathVariable("input") String input, Model model) {
        return mediaManager.findAlbumsBy(input);
    }

    @GetMapping("/api/book/search/{input}")
    public @ResponseBody List<Media> getBooks(@PathVariable("input") String input, Model model) {
        return mediaManager.findBooksBy(input);
    }

    @GetMapping("/api/allMedia/search/{input}")
    public  @ResponseBody List<Media> searchMedia(@PathVariable("input") String input, Model model) {
        return mediaManager.findAllBy(input);
    }

    @GetMapping("/api/book/{input}")
    public String searchBook(@PathVariable("input") String input, Model model) {
        mediaManager.findBooksBy(input);
        return "findMedia";
    }

    @GetMapping("/api/album/{input}")
    public String search(@PathVariable("input") String input, Model model) {
        System.out.println(Arrays.toString(mediaManager.findAlbumsBy(input).toArray()));;
        return "findMedia";
    }

    @GetMapping("/api/allMedia/{input}")
    public String searchAllMedia(@PathVariable("input") String input, Model model) {
        System.out.println(Arrays.toString(mediaManager.findAllBy(input).toArray()));;
        return "findMedia";
    }

}
