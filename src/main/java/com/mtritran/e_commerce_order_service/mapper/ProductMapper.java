package com.mtritran.e_commerce_order_service.mapper;

import com.mtritran.e_commerce_order_service.dto.request.ProductRequest;
import com.mtritran.e_commerce_order_service.dto.response.ProductResponse;
import com.mtritran.e_commerce_order_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    Product toProduct(ProductRequest request);
    ProductResponse toProductResponse(Product product);
}

