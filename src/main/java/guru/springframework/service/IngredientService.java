package guru.springframework.service;

import java.util.Set;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {

	public IngredientCommand getIngredient(Long recipeId, Long id);
	public IngredientCommand getIngredient(Long id);
	public Set<IngredientCommand> getIngredients(Long recipeId);
	public IngredientCommand saveIngredient(IngredientCommand ingredient);
	
}
