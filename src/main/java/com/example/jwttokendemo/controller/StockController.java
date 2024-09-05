package com.example.jwttokendemo.controller;

import com.example.jwttokendemo.dto.reqDto.ProductReqDto;
import com.example.jwttokendemo.dto.respDto.ProductRespDto;
import com.example.jwttokendemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class StockController {

    private final ProductService productService;
    @GetMapping("/{id}")
    public ProductRespDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductRespDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ProductRespDto createProduct(@RequestBody ProductReqDto productReqDto) {
        return productService.createProduct(productReqDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_MANAGER')")
    public ProductRespDto updateProduct(@PathVariable Long id, @RequestBody ProductReqDto productReqDto) {
        return productService.updateProduct(id, productReqDto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_MANAGER')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Successfully deleted";
    }
}
