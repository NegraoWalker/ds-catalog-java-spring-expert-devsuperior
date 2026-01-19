package com.walker.ds_catalog_java_spring_expert_devsuperior.repository;

import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
