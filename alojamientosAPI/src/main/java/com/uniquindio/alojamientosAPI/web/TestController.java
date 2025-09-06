package com.uniquindio.alojamientosAPI.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hola 👋, este endpoint es PÚBLICO.";
    }

    @GetMapping("/private/hello")
    public String privateHello() {
        return "Hola 👤, este endpoint es PRIVADO (ROLE_USER).";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hola 👑, este endpoint es solo para ADMIN.";
    }
}