package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.service.RecipeService;

@Controller
public class RecipeController {

	private RecipeService recipeService;


	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}


	@RequestMapping("/recipes")
	public String getRecipesPage(Model mdl){
		mdl.addAttribute("recipes", recipeService.getRecipes());
		return "recipes";
	}
	
}
