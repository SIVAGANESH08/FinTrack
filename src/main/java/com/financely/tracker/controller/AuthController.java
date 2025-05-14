package com.financely.tracker.controller;

import com.financely.tracker.dto.LoginForm;
import com.financely.tracker.service.JwtService;
import com.financely.tracker.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public MyUserDetailService myUserDetailService;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(),loginForm.password()));

            if(authentication.isAuthenticated()) {
                return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
            }else{
                throw new UsernameNotFoundException("Invalid credentials");
            }
    }
}

