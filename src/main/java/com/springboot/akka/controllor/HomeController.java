package com.springboot.akka.controllor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "Akka in spring boot application.";
    }
}
