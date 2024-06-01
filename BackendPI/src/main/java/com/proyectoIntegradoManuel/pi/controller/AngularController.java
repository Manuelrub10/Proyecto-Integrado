package com.proyectoIntegradoManuel.pi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AngularController {
    @RequestMapping(value = "/***")
    public String redirect() {
        return "forward:/static/index.html";
    }
}
