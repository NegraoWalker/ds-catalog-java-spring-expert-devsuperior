package com.walker.ds_catalog_java_spring_expert_devsuperior.repository;

import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class ProductRepositoryTest {
    //Injeção de dependências
    @Autowired
    private ProductRepository productRepository;

    //Variáveis
    private Long existingId;

    @BeforeEach //Usado para inicialização
    void setUp() {
        existingId = 1L;
    }

    @Test
    @DisplayName("Deve deletar o objeto quando o ID existe")
    void should_DeleteObject_When_IdExists() {
        //Act
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        //Assert
        assertFalse(result.isPresent());
    }
}
