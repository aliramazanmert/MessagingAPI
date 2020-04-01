package com.alirmert.offlinemessaging.user;

import com.alirmert.offlinemessaging.userlog.UserLog;
import com.alirmert.offlinemessaging.userlog.UserLogRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    private UserLogRepository userLogRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserLogRepository userLogRepository,UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userLogRepository = userLogRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public UserLog signUp(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()) == null){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            userLogRepository.save(new UserLog("Successful sign up - Username: " + user.getUsername()));
            return new UserLog("Signed up successfully!");
        }
        else{
            userLogRepository.save(new UserLog("Error occurred while signing up - Username already exists - Username: " + user.getUsername()));
            return new UserLog("Error: Username already exists!");
        }
    }
}
