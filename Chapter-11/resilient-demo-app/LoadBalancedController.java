package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadBalancedController {

    @GetMapping("/serviceA")
    public String serviceA() {
        return "Service A Response";
    }

    @GetMapping("/serviceB")
    public String serviceB() {
        return "Service B Response";
    }
}
