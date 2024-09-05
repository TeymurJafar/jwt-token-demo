package com.example.jwttokendemo.repository;

import com.example.jwttokendemo.entity.MyUser;
import com.example.jwttokendemo.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Long> {

    Optional<MyUser> findByUsernameAndActive(String username,Integer active);
    List<MyUser> findAllByActive(Integer active);

    MyUser findByIdAndActive(Long id, Integer active);

}
