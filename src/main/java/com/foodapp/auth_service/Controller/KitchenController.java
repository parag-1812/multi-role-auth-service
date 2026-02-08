package com.foodapp.auth_service.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchen")
public class KitchenController {

    @GetMapping("/hello")
    public String kitchenHello() {
        return "Hello KITCHEN";
    }
}
