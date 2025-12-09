package com.coveros.training.flavorhub.service;

import com.coveros.training.flavorhub.model.Recipe;
import com.coveros.training.flavorhub.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecipeService
 */
@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    
    @Mock
    private RecipeRepository recipeRepository;
    
    @InjectMocks
    private RecipeService recipeService;
    
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    
    @BeforeEach
    void setUp() {
        recipe1 = new Recipe("Pasta", "Delicious pasta", 10, 15, 4, "Easy", "Italian");
        recipe2 = new Recipe("Pizza", "Tasty pizza", 20, 15, 2, "Medium", "Italian");
        recipe3 = new Recipe("Salad", "Fresh salad", 10, 0, 2, "Easy", "American");
    }
    
    @Test
    void testGetAllRecipes_WithPagination_ReturnsPageOfRecipes() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Pageable pageable = PageRequest.of(0, 12);
        Page<Recipe> expectedPage = new PageImpl<>(recipes, pageable, 2);
        
        when(recipeRepository.findAll(pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Recipe> result = recipeService.getAllRecipes(pageable);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
        verify(recipeRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void testGetAllRecipes_WithPagination_FirstPage() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Recipe> expectedPage = new PageImpl<>(recipes, pageable, 3);
        
        when(recipeRepository.findAll(pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Recipe> result = recipeService.getAllRecipes(pageable);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
        verify(recipeRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void testGetAllRecipes_WithPagination_SecondPage() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe3);
        Pageable pageable = PageRequest.of(1, 2);
        Page<Recipe> expectedPage = new PageImpl<>(recipes, pageable, 3);
        
        when(recipeRepository.findAll(pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Recipe> result = recipeService.getAllRecipes(pageable);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertFalse(result.isFirst());
        assertTrue(result.isLast());
        verify(recipeRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void testGetAllRecipes_WithoutPagination_ReturnsAllRecipes() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2, recipe3);
        when(recipeRepository.findAll()).thenReturn(recipes);
        
        // Act
        List<Recipe> result = recipeService.getAllRecipes();
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(recipeRepository, times(1)).findAll();
    }
}
