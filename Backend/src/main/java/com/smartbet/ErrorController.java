package com.smartbet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(@RequestParam(value = "error", required = false) String error) {
        // Log the error for debugging
        System.err.println("Authentication error occurred: " + error);

        // Redirect to login page with error parameter
        if (error != null) {
            return "redirect:http://localhost:3000/login?error=" + error;
        } else {
            return "redirect:http://localhost:3000/login?error=unknown";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        // This endpoint can be used if you want to serve a login page from Spring Boot
        // Otherwise, it will redirect to your React login page
        return "redirect:http://localhost:3000/login";
    }
}