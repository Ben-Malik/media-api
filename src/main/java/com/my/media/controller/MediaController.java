package com.my.media.controller;

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

    @GetMapping("/v1/album/{input}")
    public @ResponseBody List<Media> getAlbums(@PathVariable("input") String input) {
        return mediaManager.findAlbumsBy(input, true);
    }

    @GetMapping("/v1/book/{input}")
    public @ResponseBody List<Media> getBooks(@PathVariable("input") String input) {
        return mediaManager.findBooksBy(input, true);
    }

    @GetMapping("/v1/allMedia/{input}")
    public  @ResponseBody List<Media> getAllMedia(@PathVariable("input") String input) {
        return mediaManager.findAllBy(input);
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        String message = "Ben Malik";
        String github = "https://github.com/ben-malik";

        model.addAttribute("message", message);
        model.addAttribute("github", github);

        return "index";
    }

    @GetMapping("/doc")
    public String javadoc(Model model) {
        return "doc/index";
    }
}