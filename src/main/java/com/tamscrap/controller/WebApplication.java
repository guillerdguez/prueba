package com.tamscrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = {"http://localhost:4200", "https://tamscrapt.up.railway.app"})

public class WebApplication {
 	
  

    @GetMapping("/")
    String home() {
        return "index";
    }

   	

 
}
