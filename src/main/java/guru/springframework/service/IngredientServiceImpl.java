package guru.springframework.service;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;

@Service
public class IngredientServiceImpl implements IngredientService {

	RecipeService recipeService;
	
	@Autowired
	public IngredientServiceImpl(RecipeService recipeService) {
		this.recipeService = recipeService;
	}
	
	@Override
	public IngredientCommand getIngredient(Long recipeId, Long ingredientId) {
		RecipeCommand recipe = recipeService.getRecipe(recipeId);
		Set<IngredientCommand> ingredients = recipe.getIngredients();
		Optional<IngredientCommand> ingredient = ingredients.stream()
			.filter(ingredientOptional -> ingredientOptional.getId().equals(ingredientId))
			.findFirst();
		
		if (ingredient.isPresent()) {
			return ingredient.get();
		}
		throw new EntityNotFoundException();
	}

	@Override
	public IngredientCommand getIngredient(Long id) {
		throw new RuntimeException("Not Yet Implemented");
	}

	@Override
	public Set<IngredientCommand> getIngredients(Long recipeId) {
		RecipeCommand recipe = recipeService.getRecipe(recipeId);
		return recipe.getIngredients();
	}

	@Override
	public IngredientCommand saveIngredient(IngredientCommand ingredient) {
		Long recipeId = ingredient.getRecipeId();
		if (recipeId == null)
		{
			throw new RuntimeException("Ingredient not associated with a valid Recipe");
		}
		
		RecipeCommand recipe = recipeService.getRecipe(recipeId);
		recipe.addIngredient(ingredient);
		
		RecipeCommand storedResult = recipeService.saveRecipe(recipe);
		Optional<IngredientCommand> ingredientOptional = storedResult.getIngredients().stream().filter(filter -> filter.getUUID().equals(ingredient.getUUID())).findFirst();
		
		if (!ingredientOptional.isPresent()) {
			throw new RuntimeException("Persisted Ingredient not found or not persisted");
		}
		
		return ingredientOptional.get();
	}

}
