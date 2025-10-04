package com.mtritran.e_commerce_order_service.service;

import com.mtritran.e_commerce_order_service.dto.request.ProductRequest;
import com.mtritran.e_commerce_order_service.dto.response.ProductResponse;
import com.mtritran.e_commerce_order_service.entity.Product;
import com.mtritran.e_commerce_order_service.exception.AppException;
import com.mtritran.e_commerce_order_service.exception.ErrorCode;
import com.mtritran.e_commerce_order_service.mapper.ProductMapper;
import com.mtritran.e_commerce_order_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductResponse create(ProductRequest request) {
        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        Product product = productMapper.toProduct(request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }

    public ProductResponse update(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }
}



