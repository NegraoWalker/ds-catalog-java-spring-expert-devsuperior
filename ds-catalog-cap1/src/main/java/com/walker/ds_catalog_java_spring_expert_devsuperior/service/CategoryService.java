package com.walker.ds_catalog_java_spring_expert_devsuperior.service;

import com.walker.ds_catalog_java_spring_expert_devsuperior.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    //Injeção de dependências:
    private final CategoryRepository categoryRepository;

    //Constructors:
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


}
