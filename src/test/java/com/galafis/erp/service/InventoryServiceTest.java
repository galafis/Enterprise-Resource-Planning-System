package com.galafis.erp.service;

import com.galafis.erp.dto.InventoryDto;
import com.galafis.erp.entity.Inventory;
import com.galafis.erp.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for InventoryService
 * 
 * This test class covers inventory management operations including:
 * - Product creation and management
 * - Stock level tracking and updates
 * - Low stock alerts and notifications
 * - Product search and filtering
 * - Inventory valuation calculations
 * - Best practices for service layer testing with proper mocking
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory testInventory;
    private InventoryDto testInventoryDto;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testInventory = Inventory.builder()
            .id(1L)
            .productName("Test Product")
            .productCode("TEST001")
            .description("Test product description")
            .category("Electronics")
            .quantity(100)
            .unitPrice(new BigDecimal("29.99"))
            .minimumStock(10)
            .supplier("Test Supplier")
            .location("Warehouse A")
            .lastUpdated(LocalDateTime.now())
            .active(true)
            .build();

        testInventoryDto = InventoryDto.builder()
            .productName("Test Product")
            .productCode("TEST001")
            .description("Test product description")
            .category("Electronics")
            .quantity(100)
            .unitPrice(new BigDecimal("29.99"))
            .minimumStock(10)
            .supplier("Test Supplier")
            .location("Warehouse A")
            .build();
    }

    @Test
    void shouldCreateProduct_whenValidInventoryDto() {
        // Given
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);
        when(inventoryRepository.existsByProductCode(anyString())).thenReturn(false);

        // When
        InventoryDto result = inventoryService.createProduct(testInventoryDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo(testInventoryDto.getProductName());
        assertThat(result.getProductCode()).isEqualTo(testInventoryDto.getProductCode());
        assertThat(result.getQuantity()).isEqualTo(testInventoryDto.getQuantity());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void shouldThrowException_whenProductCodeExists() {
        // Given
        when(inventoryRepository.existsByProductCode(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.createProduct(testInventoryDto);
        });

        verify(inventoryRepository).existsByProductCode(testInventoryDto.getProductCode());
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void shouldReturnProduct_whenFindByIdWithExistingId() {
        // Given
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(testInventory));

        // When
        InventoryDto result = inventoryService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testInventory.getId());
        assertThat(result.getProductName()).isEqualTo(testInventory.getProductName());
        verify(inventoryRepository).findById(1L);
    }

    @Test
    void shouldThrowException_whenFindByIdWithNonExistingId() {
        // Given
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            inventoryService.findById(999L);
        });

        verify(inventoryRepository).findById(999L);
    }

    @Test
    void shouldReturnAllProducts_whenFindAll() {
        // Given
        Inventory secondProduct = Inventory.builder()
            .id(2L)
            .productName("Second Product")
            .productCode("TEST002")
            .quantity(50)
            .build();
        
        List<Inventory> inventoryList = Arrays.asList(testInventory, secondProduct);
        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        // When
        List<InventoryDto> result = inventoryService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductName()).isEqualTo("Test Product");
        assertThat(result.get(1).getProductName()).isEqualTo("Second Product");
        verify(inventoryRepository).findAll();
    }

    @Test
    void shouldUpdateStock_whenValidQuantity() {
        // Given
        int newQuantity = 150;
        Inventory updatedInventory = testInventory.toBuilder()
            .quantity(newQuantity)
            .build();
        
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(testInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(updatedInventory);

        // When
        InventoryDto result = inventoryService.updateStock(1L, newQuantity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(newQuantity);
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void shouldThrowException_whenUpdateStockWithNegativeQuantity() {
        // Given
        int negativeQuantity = -10;
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(testInventory));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.updateStock(1L, negativeQuantity);
        });

        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void shouldReturnLowStockProducts_whenProductsBelowMinimum() {
        // Given
        Inventory lowStockProduct = testInventory.toBuilder()
            .quantity(5) // Below minimum of 10
            .build();
        
        List<Inventory> lowStockProducts = Arrays.asList(lowStockProduct);
        when(inventoryRepository.findLowStockProducts()).thenReturn(lowStockProducts);

        // When
        List<InventoryDto> result = inventoryService.findLowStockProducts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isLessThan(result.get(0).getMinimumStock());
        verify(inventoryRepository).findLowStockProducts();
    }

    @Test
    void shouldReturnProductsByCategory_whenCategoryExists() {
        // Given
        String category = "Electronics";
        List<Inventory> categoryProducts = Arrays.asList(testInventory);
        when(inventoryRepository.findByCategory(anyString())).thenReturn(categoryProducts);

        // When
        List<InventoryDto> result = inventoryService.findByCategory(category);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(category);
        verify(inventoryRepository).findByCategory(category);
    }

    @Test
    void shouldCalculateTotalInventoryValue_whenProductsExist() {
        // Given
        List<Inventory> inventoryList = Arrays.asList(testInventory);
        when(inventoryRepository.findAll()).thenReturn(inventoryList);
        
        // Expected value: quantity(100) * unitPrice(29.99) = 2999.00
        BigDecimal expectedValue = new BigDecimal("2999.00");

        // When
        BigDecimal result = inventoryService.calculateTotalInventoryValue();

        // Then
        assertThat(result).isEqualByComparingTo(expectedValue);
        verify(inventoryRepository).findAll();
    }

    @Test
    void shouldDeleteProduct_whenValidId() {
        // Given
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.of(testInventory));
        doNothing().when(inventoryRepository).deleteById(anyLong());

        // When
        inventoryService.deleteProduct(1L);

        // Then
        verify(inventoryRepository).findById(1L);
        verify(inventoryRepository).deleteById(1L);
    }

    @Test
    void shouldSearchProducts_whenSearchTermProvided() {
        // Given
        String searchTerm = "Test";
        List<Inventory> searchResults = Arrays.asList(testInventory);
        when(inventoryRepository.findByProductNameContainingIgnoreCase(anyString()))
            .thenReturn(searchResults);

        // When
        List<InventoryDto> result = inventoryService.searchProducts(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName()).containsIgnoringCase(searchTerm);
        verify(inventoryRepository).findByProductNameContainingIgnoreCase(searchTerm);
    }

    @Test
    void shouldReturnTrue_whenProductCodeExists() {
        // Given
        when(inventoryRepository.existsByProductCode(anyString())).thenReturn(true);

        // When
        boolean result = inventoryService.existsByProductCode("TEST001");

        // Then
        assertTrue(result);
        verify(inventoryRepository).existsByProductCode("TEST001");
    }

    @Test
    void shouldReturnFalse_whenProductCodeDoesNotExist() {
        // Given
        when(inventoryRepository.existsByProductCode(anyString())).thenReturn(false);

        // When
        boolean result = inventoryService.existsByProductCode("NONEXISTENT");

        // Then
        assertFalse(result);
        verify(inventoryRepository).existsByProductCode("NONEXISTENT");
    }
}
