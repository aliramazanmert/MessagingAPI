package com.alirmert.offlinemessaging.userlog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class UserLogController {
    private UserLogRepository userLogRepository;

    public UserLogController(UserLogRepository userLogRepository){
        this.userLogRepository = userLogRepository;
    }

    @GetMapping
    public List<UserLog> getLogs(){
        return userLogRepository.findAll();
    }
}
