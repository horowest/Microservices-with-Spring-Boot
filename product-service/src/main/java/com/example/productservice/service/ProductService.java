package com.example.productservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        // build product from ProductRequest
        Product product = Product.builder()
                .name(productRequest.getName())
                .desciption(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // save productr to db
        productRepository.save(product);
        log.info("Product {} is saved.", product.getName());
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(e -> mapProductToProductResponse(e))
                .collect(Collectors.toList());
    }

    private ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .description(product.getDesciption())
                .price(product.getPrice())
                .build();

        return productResponse;
    }
    
}
