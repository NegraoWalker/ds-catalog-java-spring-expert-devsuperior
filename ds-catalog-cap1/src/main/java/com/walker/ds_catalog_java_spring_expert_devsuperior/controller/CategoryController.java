package com.walker.ds_catalog_java_spring_expert_devsuperior.controller;

import com.walker.ds_catalog_java_spring_expert_devsuperior.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {
    //Injeção de dependências:
    private final CategoryService categoryService;

    //Constructors:
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


}
