package com.walker.ds_catalog_java_spring_expert_devsuperior.service;

import com.walker.ds_catalog_java_spring_expert_devsuperior.exception.ResourceNotFoundException;
import com.walker.ds_catalog_java_spring_expert_devsuperior.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


//TESTE DE UNIDADE/UNITÁRIO
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    /*
      @Mock → cria um objeto simulado do repository, sem buscar no banco
      @InjectMocks → cria o ProductService real e injeta automaticamente o mock do repository dentro dele
    */

    //Injeção de dependências
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    //Variáveis
    private Long existingId;
    private Long nonExistingId;

    //Usado para inicialização
    @BeforeEach
    public void setup() {
        existingId = 1L;
        nonExistingId = 1000L;
    }

    /*
       OBJETIVO -> Realizar testes unitarios/unidade para os metodos da classe ProductService.
       USO DO MOCKITO -> Criar mocks(objetos simulados) para simular as dependencias da classe ProductService.
       TESTE 1 -> Metodo delete
    */

    @Test
    @DisplayName("Deve deletar um produto quando o Id existe")
    public void should_DeleteProduct_When_IdExists() {
        //Arrange
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        //Act
        productService.delete(existingId);
        //Assert
        Mockito.verify(productRepository).deleteById(existingId);
        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar um produto com Id inexistente")
    void should_ThrowResourceNotFoundException_When_DeleteWithNonExistingId() {
        //Arrange
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        //Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }
}
