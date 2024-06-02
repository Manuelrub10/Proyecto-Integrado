package com.proyectoIntegradoManuel.pi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
Esta clase se ha implementado para que te redireccion al index.html cuando inicies en el despliegue.
 */
@Controller
public class AngularController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
