package com.walker.ds_catalog_java_spring_expert_devsuperior.controller;

import com.walker.ds_catalog_java_spring_expert_devsuperior.model.dto.ProductDTO;
import com.walker.ds_catalog_java_spring_expert_devsuperior.service.ProductService;
import com.walker.ds_catalog_java_spring_expert_devsuperior.util.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TESTE UNITÁRIO
@WebMvcTest(ProductController.class) //Carrega um contexto para o teste apenas da camada web(Controller[Simulando requisições HTTP])
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    public void setUp() {
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    @DisplayName("findAll deve retornar uma página")
    public void findAll_should_ReturnPage_WhenCalled() throws Exception {
        // Arrange
        when(productService.findAll(any())).thenReturn(page); //Instrui o mock: "quando findAll for chamado com qualquer argumento, retorne a página fake". Sem isso, o mock retornaria null por padrão.
        // Act & Assert
        mockMvc.perform(get("/api/products")). //Dispara uma requisição GET para o endpoint
                andExpect(status().isOk()); //Verifica que a resposta foi HTTP 200

        /*
            MockMvc
              └── GET /api/products
                    └── ProductController.findAll()
                          └── productService.findAll()  ← mock retorna a page fake
                                └── ResponseEntity.ok(page)
                                      └── HTTP 200 ✅
        */
    }
}