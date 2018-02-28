package guru.springframework.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService{

	RecipeRepository recipeRepo;

	public RecipeServiceImpl(RecipeRepository recipeRepo) {
		this.recipeRepo = recipeRepo;
	}

	@Override
	public Set<Recipe> getRecipes() {
		log.debug("Recipes called");
		Set<Recipe> recipeSet = new HashSet<>();
		recipeRepo.findAll().iterator().forEachRemaining(recipeSet::add);
		return recipeSet;
	}

	@Override
	public Recipe getRecipe(Long id) {
		Optional<Recipe> recipe = recipeRepo.findById(id);
		if (!recipe.isPresent()) {
			throw new EntityNotFoundException("Recipe not found!");
		}
		return recipe.get();
	}
	
}
