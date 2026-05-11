package com.walker.ds_catalog_java_spring_expert_devsuperior.service;

import com.walker.ds_catalog_java_spring_expert_devsuperior.exception.ResourceNotFoundException;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.dto.ProductDTO;
import com.walker.ds_catalog_java_spring_expert_devsuperior.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    @DisplayName("delete deve deletar o objeto quando o Id existe")
    public void delete_should_DeleteObject_WhenIdExists() {
        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        productService.delete(existingId);
        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    @DisplayName("delete deve lançar um ResourceNotFoundException quando o Id não existir")
    public void delete_should_ThrowResourceNotFoundException_WhenIdDoesNotExist() {
        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }

    @Test
    @DisplayName("findAll deve retornar uma página com produtos quando consultar a página 0 com 10 itens")
    public void findAll_should_ReturnPage_WhenPage0Size10() {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        PageRequest pageRequest = PageRequest.of(0, 10);
        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        Page<ProductDTO> result = productService.findAll(pageRequest);
        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        assertFalse(result.isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    @DisplayName("findAll deve retornar página vazia quando a página solicitada não existir")
    public void findAll_should_ReturnEmptyPage_WhenRequestedPageDoesNotExist() {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        PageRequest pageRequest = PageRequest.of(50, 10);
        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        Page<ProductDTO> result = productService.findAll(pageRequest);
        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll deve retornar produtos ordenados de forma ascendente pelo nome")
    public void findAll_should_ReturnOrderedPage_WhenSortedByNameAscending() {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name")); //ordem alfabética
        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        Page<ProductDTO> result = productService.findAll(pageRequest);
        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }


}


/*
//Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
//Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
//Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
*/
