package com.example.jwttokendemo.service;

import com.example.jwttokendemo.dto.reqDto.ProductReqDto;
import com.example.jwttokendemo.dto.respDto.ProductRespDto;
import com.example.jwttokendemo.entity.Product;
import com.example.jwttokendemo.enums.StatusEnum;
import com.example.jwttokendemo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductRespDto createProduct(ProductReqDto productReqDto) {
        Product product = new Product();
        product.setName(productReqDto.getName());
        product.setDescription(productReqDto.getDescription());
        product.setPrice(productReqDto.getPrice());
        product.setQuantity(productReqDto.getQuantity());

        Product savedProduct = productRepository.save(product);
        return mapToProductRespDto(savedProduct);
    }

    public ProductRespDto updateProduct(Long id, ProductReqDto reqDto) {
        Product product = productRepository.findByIdAndActive(id,StatusEnum.ACTIVE.value);
        if(product == null){
            throw  new RuntimeException("Product not found");
        }
        String name = reqDto.getName();
        String description = reqDto.getDescription();
        Double price = reqDto.getPrice();
        Integer quantity = reqDto.getQuantity();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

        Product updatedProduct = productRepository.save(product);
        return mapToProductRespDto(updatedProduct);
    }

    public ProductRespDto getProductById(Long id) {
        Product product = productRepository.findByIdAndActive(id,StatusEnum.ACTIVE.value);

        if (product == null){
            throw new RuntimeException("Product not found");
        }
        return mapToProductRespDto(product);
    }

    public List<ProductRespDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductRespDto)
                .collect(Collectors.toList());
    }

    public String deleteProduct(Long id) {
        Product product = productRepository.findByIdAndActive(id, StatusEnum.ACTIVE.value);
                if(product == null){
                   throw  new RuntimeException("Product not found");
                }
                product.setActive(StatusEnum.DEACTIVATE.value);
        productRepository.save(product);
        return "Successfully deleted";
    }

    private ProductRespDto mapToProductRespDto(Product product) {
        return ProductRespDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .active(product.getActive())
                .build();
    }
}
