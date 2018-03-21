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
	public IngredientCommand saveIngredient(IngredientCommand ingredientToStore) {
		Long recipeId = ingredientToStore.getRecipeId();
		if (recipeId == null)
		{
			throw new RuntimeException("Ingredient not associated with a valid Recipe");
		}
		
		RecipeCommand recipe = recipeService.getRecipe(recipeId);
		//Check if IngredientCommand has an ID
		IngredientCommand ingredientFound = null;
		if (ingredientToStore.getId() != null) {
			//Check if Ingredient already exist....
			Optional<IngredientCommand> ingredientOptional = recipe.getIngredients().stream().filter(filter -> filter.getId().equals(ingredientToStore.getId())).findFirst();
			if (ingredientOptional.isPresent()) {
				ingredientFound = ingredientOptional.get();
				ingredientFound.setAmount(ingredientToStore.getAmount());
				ingredientFound.setDescription(ingredientToStore.getDescription());
				ingredientFound.setUom(ingredientToStore.getUom());
				ingredientFound.setUUID(ingredientToStore.getUUID());
			}
		}
		if (ingredientFound == null) {
			recipe.addIngredient(ingredientToStore);
		}
		
		
		RecipeCommand storedResult = recipeService.saveRecipe(recipe);
		Optional<IngredientCommand> ingredientOptional = storedResult.getIngredients().stream().filter(filter -> filter.getUUID().equals(ingredientToStore.getUUID())).findFirst();
		
		if (!ingredientOptional.isPresent()) {
			throw new RuntimeException("Persisted Ingredient not found or not persisted");
		}
		
		return ingredientOptional.get();
	}

	@Override
	public void deleteIngredient(Long recipeId, Long ingredientId) {
		
		boolean removed = false;
		RecipeCommand recipeCommand = recipeService.getRecipe(recipeId);
		if (recipeCommand == null) {
			throw new EntityNotFoundException();
		}
		
		
		Optional<IngredientCommand> opt = recipeCommand.getIngredients().stream().filter(filter -> filter.getId().equals(ingredientId)).findFirst();
		if (opt.isPresent()) {
			IngredientCommand cmd = opt.get();
			removed = recipeCommand.getIngredients().remove(cmd);
		}
		if (removed) {
			recipeService.saveRecipe(recipeCommand);
		}
		
	}

}
