package ru.nsu.skopintsev.turclub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        String errorMessage = null;
        if (throwable != null) {
            errorMessage = throwable.getMessage();
        } else {
            errorMessage = "Произошла неизвестная ошибка";
        }

        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }
}
