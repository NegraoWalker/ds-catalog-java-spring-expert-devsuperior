package com.walker.ds_catalog_java_spring_expert_devsuperior.util;

import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Category;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Product;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.dto.ProductDTO;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Iphone 20", 2000.0, "https://img.com/iphone.png", Instant.parse("2026-03-03T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletrônicos")); //Associa uma categoria cadastrada ao produto
        return product;
    }

//    public static ProductDTO createProductDTO() {
//        Product product = createProduct();
//        return new ProductDTO(product, product.getCategories());
//    }
}
