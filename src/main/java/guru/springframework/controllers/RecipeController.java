package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.domain.Recipe;
import guru.springframework.service.RecipeService;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

	private RecipeService recipeService;


	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}


	@RequestMapping("")
	public String getRecipesPage(Model mdl){
		mdl.addAttribute("recipes", recipeService.getRecipes());
		return "recipes/recipes";
	}
	
	@RequestMapping("{id}")
	public String getRecipePage(@PathVariable String id, Model mdl) {
		
		Recipe recipe = recipeService.getRecipe(new Long(id));
		mdl.addAttribute("recipe",recipe);
		
		return "recipes/recipe";
	}
}
