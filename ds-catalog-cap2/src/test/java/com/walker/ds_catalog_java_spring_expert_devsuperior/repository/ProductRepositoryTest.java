package com.walker.ds_catalog_java_spring_expert_devsuperior.repository;

import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Product;
import com.walker.ds_catalog_java_spring_expert_devsuperior.util.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//TESTE DE INTEGRAÇÃO
@DataJpaTest
public class ProductRepositoryTest {
    //Injeção de dependências
    @Autowired
    private ProductRepository productRepository;

    //Variáveis
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    //Usado para inicialização
    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
        countTotalProducts = 25L;
    }

    @Test
    @DisplayName("Deve deletar o objeto quando o ID existe")
    public void should_DeleteObject_When_IdExists() {
        //Act
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        //Assert
        assertFalse(result.isPresent()); //result.isPresent() == true -> quando o valor está presente. Caso contrário false.
    }

    @Test
    @DisplayName("Deve salvar o objeto quando o ID é nulo")
    public void should_SaveObject_When_IdIsNull() {
        //Arrange
        Product product = Factory.createProduct();
        product.setId(null); //O Spring Data JPA, ao ver que o ID é null, entende que é um INSERT (não um UPDATE). O banco então gera o próximo ID automaticamente via GenerationType.IDENTITY.
        //Act
        Product result = productRepository.save(product);
        //Assert
        assertNotNull(result.getId());
        assertEquals(countTotalProducts + 1, result.getId());
    }

    @Test
    @DisplayName("Deve retornar um Optional de Produto não vazio quando o ID for válido")
    public void should_ReturnOptionalProduct_When_IdIsValid() {
        //Act
        Optional<Product> result = productRepository.findById(existingId);
        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Deve retornar um Optional de Produto vazio quando o ID não for válido")
    public void should_ReturnOptionalProductEmpty_When_IdIsNotValid() {
        // Act
        Optional<Product> result = productRepository.findById(nonExistingId);
        // Assert
        assertTrue(result.isEmpty());
    }
}
