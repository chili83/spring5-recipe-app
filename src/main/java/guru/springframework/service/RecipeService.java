package guru.springframework.service;

import java.util.Set;

import guru.springframework.domain.Recipe;

public interface RecipeService {

	Set<Recipe> getRecipes();
	
}
