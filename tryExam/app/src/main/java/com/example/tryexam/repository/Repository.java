package com.example.tryexam.repository;

import com.example.tryexam.model.Recipe;

import java.util.List;

public interface Repository {
    List<Recipe> getAllRecipes();
    List<String> getAll();
    boolean addRecipe(Recipe recipe);
    boolean deleteRecipe(Recipe recipe);
    Recipe updateRecipe(int id, String name, String details, int time, String type, int rating);
}
