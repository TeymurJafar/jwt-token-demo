package com.example.jwttokendemo.repository;

import com.example.jwttokendemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByIdAndActive(Long id, Integer value);
}
