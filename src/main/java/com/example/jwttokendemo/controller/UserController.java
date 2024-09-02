package com.example.jwttokendemo.controller;

import com.example.jwttokendemo.entity.MyUser;
import com.example.jwttokendemo.entity.UserDto;
import com.example.jwttokendemo.repository.MyUserRepository;
import com.example.jwttokendemo.service.MyUserDetailService;
import com.example.jwttokendemo.token.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MyUserDetailService myUserDetailService;

    @PostMapping("/register")
    public MyUser createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String LoginUser(@RequestBody UserDto userDto) {

       Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(), userDto.getPassword()));

       if (authentication.isAuthenticated()){


           return jwtService.generateToken(
                   myUserDetailService.loadUserByUsername(userDto.getUsername()));
       }else{
           throw new UsernameNotFoundException("Invalid Credentials");
       }

    }

    @GetMapping("/user")
    public String getUser() {
        return "This is User";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "This is Admin";
    }

}