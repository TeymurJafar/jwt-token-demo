package com.example.jwttokendemo.controller;

import com.example.jwttokendemo.dto.RoleToUserDto;
import com.example.jwttokendemo.dto.reqDto.RoleReqDto;
import com.example.jwttokendemo.dto.reqDto.UserReqDto;
import com.example.jwttokendemo.dto.respDto.RoleRespDto;
import com.example.jwttokendemo.entity.MyUser;
import com.example.jwttokendemo.dto.respDto.UserRespDto;
import com.example.jwttokendemo.service.RoleService;
import com.example.jwttokendemo.service.UserService;
import com.example.jwttokendemo.token.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/user/register")
    public MyUser createUser(@RequestBody UserReqDto userReqDto) {
        userReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
        MyUser savedUser = userService.registerUser(userReqDto);
        System.out.println(savedUser);
        return savedUser;
    }

    @PostMapping("/user/login")
    public String LoginUser(@RequestBody UserReqDto userReqDto, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userReqDto.getUsername(), userReqDto.getPassword()));

        if (authentication.isAuthenticated()){
            String jwt = jwtService.generateToken(
                    userService.loadUserByUsername(userReqDto.getUsername()));
            response.setHeader("Authorization", "Bearer "+jwt);
            return jwt;

        }else{
            throw new UsernameNotFoundException("Invalid Credentials");
        }
    }
    @GetMapping(value = "/roles/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoleRespDto getRoleById(@PathVariable("id") Long id){
        return roleService.roleById(id);
    }
    @GetMapping(value = "/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<RoleRespDto> getAll(){
        return roleService.getAll();
    }
    @PostMapping(value = "/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoleRespDto addRole(@RequestBody RoleReqDto roleReqDto)
    {
        return roleService.addRole(roleReqDto);
    }
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_CASHIER')")
    public List<UserRespDto> getUser() {
        return userService.findAllByActive();
    }
    @GetMapping("users/{id}")
    public UserRespDto getUserById( @PathVariable(value = "id") Long id){
        return userService.findById(id);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public UserRespDto updateUser(@RequestBody UserReqDto reqDto,@PathVariable Long id){
       return userService.updateUser(reqDto,id);
    }

    @PostMapping("/addRoleToUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRoleToUser(@RequestBody RoleToUserDto form) {
        try {
            userService.addRoleToUser(form);
            return "Role " + form.getRoleName() + " added to user with ID " + form.getUserId();
        } catch (Exception e) {
            return "Failed to add role: " + e.getMessage();
        }
    }
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
