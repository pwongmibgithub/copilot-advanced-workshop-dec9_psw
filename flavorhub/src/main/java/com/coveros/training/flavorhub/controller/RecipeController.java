package com.coveros.training.flavorhub.controller;

import com.coveros.training.flavorhub.model.Recipe;
import com.coveros.training.flavorhub.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing recipes
 */
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {
    
    private final RecipeService recipeService;
    
    /**
     * Get all recipes with optional pagination
     * @param page Page number (0-indexed, default 0)
     * @param size Number of items per page (default 12)
     * @return List of all recipes or paginated response
     */
    @GetMapping
    public ResponseEntity<?> getAllRecipes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        // If pagination parameters are not provided, return all recipes
        if (page == null || size == null) {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        }
        
        // Create pageable with default size of 12 and sort by name
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Recipe> recipePage = recipeService.getAllRecipes(pageable);
        return ResponseEntity.ok(recipePage);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Search recipes by name
     * NOTE: This endpoint is complete and working
     */
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String query) {
        return ResponseEntity.ok(recipeService.searchRecipes(query));
    }
    
    /**
     * Get recipes by difficulty level
     * NOTE: Workshop participants will implement this endpoint using Copilot
     */
    // TODO: Implement GET /api/recipes/difficulty/{level} endpoint
    
    /**
     * Get recipes by cuisine type
     * NOTE: Workshop participants will implement this endpoint using Copilot
     */
    // TODO: Implement GET /api/recipes/cuisine/{type} endpoint
    
    /**
     * Recommend recipes based on available pantry ingredients
     * NOTE: This is an advanced endpoint to be implemented during the workshop
     */
    // TODO: Implement GET /api/recipes/recommendations endpoint
    
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe saved = recipeService.saveRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id, 
            @Valid @RequestBody Recipe recipe) {
        return recipeService.getRecipeById(id)
                .map(existing -> {
                    recipe.setId(id);
                    return ResponseEntity.ok(recipeService.saveRecipe(recipe));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
