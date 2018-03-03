package guru.springframework.service;

import java.util.Set;

import guru.springframework.commands.RecipeCommand;

public interface RecipeService {

	Set<RecipeCommand> getRecipes();
	RecipeCommand getRecipe(Long id);
	RecipeCommand saveRecipe(RecipeCommand recipeCommand);
	void deleteRecipeById(Long id);
	
}
