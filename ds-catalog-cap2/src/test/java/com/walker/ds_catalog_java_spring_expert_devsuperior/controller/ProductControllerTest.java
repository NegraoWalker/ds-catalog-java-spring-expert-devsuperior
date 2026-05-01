package com.walker.ds_catalog_java_spring_expert_devsuperior.controller;

import com.walker.ds_catalog_java_spring_expert_devsuperior.exception.ResourceNotFoundException;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.dto.ProductDTO;
import com.walker.ds_catalog_java_spring_expert_devsuperior.service.ProductService;
import com.walker.ds_catalog_java_spring_expert_devsuperior.util.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
//Carrega um contexto para o teste apenas da camada web(Controller[Simulando requisições HTTP])
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private Long existingId;
    private Long nonExistingId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private String jsonBody;


    @BeforeEach
    public void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        jsonBody = objectMapper.writeValueAsString(productDTO);
    }

    @Test
    @DisplayName("findAll deve retornar uma página")
    public void findAll_should_ReturnPage_WhenCalled() throws Exception {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        when(productService.findAll(any())).thenReturn(page); //Instrui o mock: "quando findAll for chamado com qualquer argumento, retorne a página fake". Sem isso, o mock retornaria null por padrão

        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/products")
                                .accept(MediaType.APPLICATION_JSON)
                ); //Dispara uma requisição GET para o endpoint

        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        resultActions.andExpect(status().isOk()); //Verifica que a resposta foi HTTP 200

        /*
            MockMvc
              └── GET /api/products
                    └── ProductController.findAll()
                          └── productService.findAll()  ← mock retorna a page fake
                                └── ResponseEntity.ok(page)
                                      └── HTTP 200 ✅
        */
    }

    @Test
    @DisplayName("findById deve retornar um ProductDTO quando o Id existir")
    public void findById_should_ReturnProductDTO_WhenIdExists() throws Exception {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        when(productService.findById(existingId)).thenReturn(productDTO);

        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/products/{id}", existingId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists()); //Verifica se o field id está no retorno no formato JSON do productDTO
        resultActions.andExpect(jsonPath("$.name").exists()); //Verifica se o field name está no retorno no formato JSON do productDTO
        resultActions.andExpect(jsonPath("$.description").exists()); //Verifica se o field description está no retorno no formato JSON do productDTO

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("JSON COMPLETO: " + jsonResponse);
    }

    @Test
    @DisplayName("findById deve lançar um ResourceNotFoundException quando o Id não existir")
    public void findById_should_ThrowResourceNotFoundException_WhenIdDoesNotExist() throws Exception {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/products/{id}", nonExistingId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update deve retornar um ProductDTO quando o Id existir")
    public void update_should_ReturnProductDTO_WhenIdExists() throws Exception {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        when(productService.update(eq(existingId), any())).thenReturn(productDTO);

        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        ResultActions resultActions = mockMvc
                .perform(
                        put("/api/products/{id}", existingId)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    @DisplayName("update deve lançar um ResourceNotFoundException quando o Id não existir")
    public void update_should_ThrowResourceNotFoundException_WhenIdDoesNotExist() throws Exception {
        //Arrange(Preparar) - Preparar o cenário de teste: instanciar e configurar objetos, mocks, dados de entrada, comportamentos esperados de dependências
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        //Act(Executar) - Executar a ação que está sendo testada: chamar, executar e capturar o resultado do método a ser testado
        ResultActions resultActions = mockMvc
                .perform(
                        put("/api/products/{id}", nonExistingId)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

        //Assert(Verificar) - Verificar se o(s) resultado(s) foram os esperados
        resultActions.andExpect(status().isNotFound());
    }

//    @Test
//    @DisplayName("delete deve deletar um produto quando o Id existir)
//
//    @Test
//    @DisplayName("delete deve lançar um ResourceNotFoundException quando o Id não existir")
//
//    @Test
//    @DisplayName("delete deve lançar um DatabaseException")

}