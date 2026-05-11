package com.walker.ds_catalog_java_spring_expert_devsuperior.service;

import com.walker.ds_catalog_java_spring_expert_devsuperior.exception.DatabaseException;
import com.walker.ds_catalog_java_spring_expert_devsuperior.exception.ResourceNotFoundException;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Category;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.domain.Product;
import com.walker.ds_catalog_java_spring_expert_devsuperior.model.dto.ProductDTO;
import com.walker.ds_catalog_java_spring_expert_devsuperior.repository.CategoryRepository;
import com.walker.ds_catalog_java_spring_expert_devsuperior.repository.ProductRepository;
import com.walker.ds_catalog_java_spring_expert_devsuperior.util.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl<Product> page;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));
    }


    @Test
    @DisplayName("Deve deletar um produto quando o Id existe")
    public void delete_should_DeleteProduct_WhenIdExists() {
        // Arrange
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        // Act
        productService.delete(existingId);
        // Assert
        Mockito.verify(productRepository).deleteById(existingId);
        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));
    }

    @Test
    @DisplayName("delete deve lançar ResourceNotFoundException ao deletar um produto com Id inexistente")
    public void delete_should_ThrowResourceNotFoundException_WhenIdDoesNotExist() {
        // Arrange
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }

    @Test
    @DisplayName("delete deve lançar DatabaseException ao deletar um produto com Id dependente")
    public void delete_should_ThrowDatabaseException_WhenIdHasDependencies() {
        // Arrange
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
        // Act & Assert
        Assertions.assertThrows(DatabaseException.class, () -> productService.delete(dependentId));
    }

    @Test
    @DisplayName("findAll deve retornar uma página de ProductDTO")
    public void findAll_should_ReturnPageOfProductDTO_WhenCalled() {
        // Arrange
        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        // Act
        Page<ProductDTO> result = productService.findAll(PageRequest.of(0, 10));
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Mockito.verify(productRepository).findAll((Pageable) ArgumentMatchers.any());
    }

    @Test
    @DisplayName("findById deve retornar ProductDTO quando o Id existe")
    public void findById_should_ReturnProductDTO_WhenIdExists() {
        // Arrange
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        // Act
        ProductDTO result = productService.findById(existingId);
        // Assert
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando o Id não existe")
    public void findById_should_ThrowResourceNotFoundException_WhenIdDoesNotExist() {
        // Arrange
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingId));
    }

    @Test
    @DisplayName("insert deve retornar ProductDTO ao inserir um produto")
    public void insert_should_ReturnProductDTO_WhenValidProduct() {
        // Arrange
        Mockito.when(categoryRepository.getReferenceById(ArgumentMatchers.any())).thenReturn(new Category());
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        // Act
        ProductDTO result = productService.insert(productDTO);
        // Assert
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("update deve retornar ProductDTO quando o Id existe")
    public void update_should_ReturnProductDTO_WhenIdExists() {
        // Arrange
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(categoryRepository.getReferenceById(ArgumentMatchers.any())).thenReturn(new Category());
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        // Act
        ProductDTO result = productService.update(existingId, productDTO);
        // Assert
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("update deve lançar ResourceNotFoundException quando o Id não existe")
    public void update_should_ThrowResourceNotFoundException_WhenIdDoesNotExist() {
        // Arrange
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.update(nonExistingId, productDTO));
    }

}