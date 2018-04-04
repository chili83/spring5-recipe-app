package guru.springframework.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import guru.springframework.commands.RecipeCommand;

public interface RecipeService {

	Set<RecipeCommand> getRecipes();
	RecipeCommand getRecipe(Long id);
	RecipeCommand saveRecipe(RecipeCommand recipeCommand);
	void deleteRecipeById(Long id);
	RecipeCommand addRecipeImage(Long id, MultipartFile file) throws IOException;
	
}
