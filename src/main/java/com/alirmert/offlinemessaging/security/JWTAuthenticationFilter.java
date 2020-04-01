package com.alirmert.offlinemessaging.security;

import com.alirmert.offlinemessaging.user.User;
import com.alirmert.offlinemessaging.userlog.UserLog;
import com.alirmert.offlinemessaging.userlog.UserLogRepository;
import com.auth0.jwt.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.alirmert.offlinemessaging.security.SecurityConstants.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserLogRepository userLogRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserLogRepository userLogRepository) {
        this.authenticationManager = authenticationManager;
        this.userLogRepository = userLogRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException|AuthenticationException e) {
            userLogRepository.save(new UserLog("Invalid login : " + e));
           try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(res.getWriter(),new UserLog("Error: Logging in failed!"));
            } catch (IOException ex) {
                userLogRepository.save(new UserLog("Error occurred sending a response to the client: " + ex));
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        userLogRepository.save(new UserLog("Successful login - Username: " + ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername()));
        try{
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(res.getWriter(),new UserLog("Logged in successfully!"));
        }
        catch(IOException e){
            userLogRepository.save(new UserLog("Error occurred sending a response to the client: " + e));
            throw new RuntimeException(e);
        }


    }



}
