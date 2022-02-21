package com.my.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping({"/", "/all"})
    public String home(Model model) {
        String message = "Kramp Hub Recruitment - Ben Malik";
        String github = "https://github.com/ben-malik";

        model.addAttribute("message", message);
        model.addAttribute("github", github);

        return "index";
    }
}
