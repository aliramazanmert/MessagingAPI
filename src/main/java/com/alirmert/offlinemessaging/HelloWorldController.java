package com.alirmert.offlinemessaging;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String sayHello(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello " + name;
    }
}
