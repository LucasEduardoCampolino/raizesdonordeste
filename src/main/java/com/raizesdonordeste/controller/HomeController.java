package com.raizesdonordeste.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "API Raízes do Nordeste funcionando!";
    }

    @GetMapping("/debug")
    public Object debug(Authentication auth) {

        if (auth == null) {
            return "SEM AUTENTICACAO";
        }

        return auth;
    }
}