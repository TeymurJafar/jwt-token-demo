package com.example.jwttokendemo.repository;

import com.example.jwttokendemo.dto.respDto.RoleRespDto;
import com.example.jwttokendemo.entity.Role;
import com.example.jwttokendemo.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);

   Role findRoleByIdAndActive(Long id, Integer active);

    List<Role> findAllByActive(Integer value);
}
