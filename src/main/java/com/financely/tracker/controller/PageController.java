package com.financely.tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PageController {
    @GetMapping("/home")
    public String home(){
        return "home";
    }
    @GetMapping("/user/home")
    public String userHome(){
        return "user_home";
    }

    @GetMapping("/login")
    public String customLogin() {
        return "custom_login";
    }
}
