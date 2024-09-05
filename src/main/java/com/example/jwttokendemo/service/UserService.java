package com.example.jwttokendemo.service;

import com.example.jwttokendemo.dto.RoleToUserDto;
import com.example.jwttokendemo.dto.reqDto.UserReqDto;
import com.example.jwttokendemo.entity.MyUser;
import com.example.jwttokendemo.entity.Role;
import com.example.jwttokendemo.dto.respDto.UserRespDto;
import com.example.jwttokendemo.enums.StatusEnum;
import com.example.jwttokendemo.repository.RoleRepository;
import com.example.jwttokendemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsernameAndActive(username,StatusEnum.ACTIVE.value).orElseThrow(() -> new UsernameNotFoundException("User not found for given username"));

        List<GrantedAuthority> roles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        ;
        return new User(user.getUsername(), user.getPassword(), roles);
    }
    public MyUser registerUser(UserReqDto userReqDto) {
        Role role = roleRepository.findByName("ROLE_USER");
        if (role.getName() == null) {
            role = new Role();
            role.setName("ROLE_USER");
        }
        MyUser user = new MyUser();
        user.getRoles().add(role);
        user.setUsername(userReqDto.getUsername());
        user.setPassword(userReqDto.getPassword());
        return userRepository.save(user);
    }
    public List<UserRespDto> findAllByActive() {
        List<MyUser> userList = userRepository.findAllByActive(StatusEnum.ACTIVE.value);
        if (userList.isEmpty()) {
            throw new RuntimeException("User List is empty");
        }

        return userList.stream().map(this::mapUserToDto).toList();
    }
    private UserRespDto mapUserToDto(MyUser user) {
        return UserRespDto.builder().id(user.getId()).username(user.getUsername()).roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())).build();

    }
    public UserRespDto findById(Long id) {

        MyUser user = userRepository.findByIdAndActive(id,StatusEnum.ACTIVE.value);

        return mapUserToDto(user);

    }

    public UserRespDto updateUser(UserReqDto reqDto, Long id) {

        UserRespDto respDto = findById(id);
        if (respDto == null){
            throw new RuntimeException("User does not exist");
        }
        respDto.setUsername(reqDto.getUsername());
        respDto.setPassword(reqDto.getPassword());

        MyUser myUser = MyUser.builder()
                .username(respDto.getUsername())
                .password(respDto.getPassword())
                .build();
        userRepository.save(myUser);
        return respDto;
    }


    public String deleteUser(Long id) {
        MyUser user = userRepository.findByIdAndActive(id,StatusEnum.ACTIVE.value);
        if (user == null){
            throw new RuntimeException("User does not exist");
        }
        user.setActive(StatusEnum.DEACTIVATE.value);
        userRepository.save(user);
        return "User Deleted Successfully";
    }

    public void addRoleToUser(RoleToUserDto form) {
        MyUser user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + form.getUserId()));
        Role role = roleRepository.findByName(form.getRoleName());
        if (role == null){
            throw new RuntimeException("Role not found");
        }
        Set<Role> roles = user.getRoles();
        if (roles.contains(role)) {
            throw new IllegalArgumentException("User already has the role: " + form.getRoleName());
        }
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
