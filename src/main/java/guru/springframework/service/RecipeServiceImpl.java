package guru.springframework.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService{

	RecipeRepository recipeRepo;
	RecipeToRecipeCommand recipeToRecipeCommand;
	RecipeCommandToRecipe recipeCommandToRecipe;

	public RecipeServiceImpl(RecipeRepository recipeRepo, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
		this.recipeRepo = recipeRepo;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
	}

	@Override
	public Set<RecipeCommand> getRecipes() {
		log.debug("Recipes called");
		Set<Recipe> recipeSet = new HashSet<>();
		recipeRepo.findAll().iterator().forEachRemaining(recipeSet::add);
		
		Set<RecipeCommand> recipeCommand =new HashSet<>();
		Iterator<Recipe> it = recipeSet.iterator();
		while (it.hasNext()) {
			Recipe recipe = it.next();
			recipeCommand.add(recipeToRecipeCommand.convert(recipe));
		}
		
		return recipeCommand;
	}

	@Override
	public RecipeCommand getRecipe(Long id) {
		Optional<Recipe> recipe = recipeRepo.findById(id);
		if (!recipe.isPresent()) {
			throw new EntityNotFoundException("Recipe not found!");
		}
		return recipeToRecipeCommand.convert(recipe.get());
	}

	@Override
	public RecipeCommand saveRecipe(RecipeCommand recipe) {
		if (recipe != null) {
			Recipe result = recipeRepo.save(recipeCommandToRecipe.convert(recipe));
			return recipeToRecipeCommand.convert(result);
		}
		return null;
	}

	@Override
	public void deleteRecipeById(Long id) {

		recipeRepo.deleteById(id);
		
	}
	

	
}
