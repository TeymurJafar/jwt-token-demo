package com.example.jwttokendemo.service;

import com.example.jwttokendemo.dto.reqDto.RoleReqDto;
import com.example.jwttokendemo.dto.respDto.RoleRespDto;
import com.example.jwttokendemo.entity.Role;
import com.example.jwttokendemo.enums.StatusEnum;
import com.example.jwttokendemo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleRespDto roleById(Long id) {
        Role role = roleRepository.findRoleByIdAndActive(id, StatusEnum.ACTIVE.value);

        if (role == null){
            throw new RuntimeException("The Role by given Id does not exist");
        }
        return mapRoleToDto(role);
    }
    public List<RoleRespDto> getAll() {
        List<Role> roles = roleRepository.findAllByActive(StatusEnum.ACTIVE.value);

        if (roles.isEmpty()){
            throw new RuntimeException("Roles table is empty");
        }
        return roles.stream()
                .map(this::mapRoleToDto)
                .toList();

    }

    private RoleRespDto mapRoleToDto(Role role) {
        return RoleRespDto.builder()
                .id(role.getId())
                .name(role.getName())
                .active(role.getActive())
                .build();
    }

    public RoleRespDto addRole(RoleReqDto roleReqDto) {

      String name  = roleReqDto.getName();
      if (!name.startsWith("ROLE_")){
          throw new RuntimeException("Role name has to be start with 'Role_'");
      }
      Role checkRole = roleRepository.findByName(name);
      if (checkRole != null){
          throw new RuntimeException("Given role exist in database");
      }

        Role role = roleRepository.save(new Role(name));
        return mapRoleToDto(role);
    }
}
