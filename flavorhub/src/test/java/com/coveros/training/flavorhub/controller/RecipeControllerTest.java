package com.coveros.training.flavorhub.controller;

import com.coveros.training.flavorhub.model.Recipe;
import com.coveros.training.flavorhub.service.RecipeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecipeController
 */
@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {
    
    @Mock
    private RecipeService recipeService;
    
    @InjectMocks
    private RecipeController recipeController;
    
    private Recipe recipe1;
    private Recipe recipe2;
    
    @BeforeEach
    void setUp() {
        recipe1 = new Recipe("Pasta", "Delicious pasta", 10, 15, 4, "Easy", "Italian");
        recipe2 = new Recipe("Pizza", "Tasty pizza", 20, 15, 2, "Medium", "Italian");
    }
    
    @Test
    void testGetAllRecipes_WithPaginationParameters_ReturnsPageOfRecipes() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Page<Recipe> expectedPage = new PageImpl<>(recipes, PageRequest.of(0, 12), 2);
        
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(expectedPage);
        
        // Act
        ResponseEntity<?> response = recipeController.getAllRecipes(0, 12);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Page);
        
        Page<?> resultPage = (Page<?>) response.getBody();
        assertEquals(2, resultPage.getContent().size());
        assertEquals(2, resultPage.getTotalElements());
        assertEquals(1, resultPage.getTotalPages());
        
        verify(recipeService, times(1)).getAllRecipes(any(Pageable.class));
    }
    
    @Test
    void testGetAllRecipes_WithoutPaginationParameters_ReturnsListOfRecipes() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        when(recipeService.getAllRecipes()).thenReturn(recipes);
        
        // Act
        ResponseEntity<?> response = recipeController.getAllRecipes(null, null);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        
        List<?> resultList = (List<?>) response.getBody();
        assertEquals(2, resultList.size());
        
        verify(recipeService, times(1)).getAllRecipes();
        verify(recipeService, never()).getAllRecipes(any(Pageable.class));
    }
    
    @Test
    void testGetAllRecipes_WithPageParameter_UsesCorrectPageSize() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1);
        Page<Recipe> expectedPage = new PageImpl<>(recipes, PageRequest.of(1, 12), 14);
        
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(expectedPage);
        
        // Act
        ResponseEntity<?> response = recipeController.getAllRecipes(1, 12);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getNumber());
        assertEquals(12, resultPage.getSize());
        
        verify(recipeService, times(1)).getAllRecipes(any(Pageable.class));
    }
    
    @Test
    void testGetAllRecipes_WithCustomPageSize_ReturnsCorrectPageSize() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Page<Recipe> expectedPage = new PageImpl<>(recipes, PageRequest.of(0, 5), 2);
        
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(expectedPage);
        
        // Act
        ResponseEntity<?> response = recipeController.getAllRecipes(0, 5);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertNotNull(resultPage);
        assertEquals(5, resultPage.getSize());
        
        verify(recipeService, times(1)).getAllRecipes(any(Pageable.class));
    }
}
