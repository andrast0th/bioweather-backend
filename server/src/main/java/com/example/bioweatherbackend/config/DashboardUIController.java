package com.example.bioweatherbackend.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardUIController {
    @RequestMapping(value = "/{path:^(?!api|actuator)[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}