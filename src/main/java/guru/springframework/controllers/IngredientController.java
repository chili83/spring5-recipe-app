package guru.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;

@Controller
@RequestMapping("/recipes/{recipeId}/ingredients")
public class IngredientController {
	
	IngredientService ingredientService;
	RecipeService recipeService;
	
	@Autowired
	public IngredientController(IngredientService ingredientService, RecipeService recipeService) {
		this.ingredientService = ingredientService;
		this.recipeService = recipeService;
	}
	
	
	@GetMapping("")
	public String getIngredientsPage(@PathVariable String recipeId, Model model) {
		
		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		model.addAttribute("recipe", recipe);
		
		return "recipes/ingredients/ingredients";
	}

	@GetMapping("{id}")
	public String getIngredientPage(@PathVariable Long recipeId, @PathVariable String id, Model model) {
		
		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		model.addAttribute("recipe", recipe);

		IngredientCommand ingredients = ingredientService.getIngredient(new Long(recipeId), new Long(id));
		model.addAttribute("ingredient", ingredients);
		
		return "recipes/ingredients/ingredient";
	}
	
	@GetMapping("/new")
	public String newIngredientPage(@PathVariable Long recipeId, Model model) {
		model.addAttribute("recipe", recipeService.getRecipe(recipeId));
		model.addAttribute("ingredient", new IngredientCommand());
		return "recipes/ingredients/ingredientform";
	}

	@GetMapping("/{id}/edit")
	public String newIngredientPage(@PathVariable Long recipeId, @PathVariable String id, Model model) {
		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		model.addAttribute("recipe", recipe);

		IngredientCommand ingredients = ingredientService.getIngredient(new Long(recipeId), new Long(id));
		model.addAttribute("ingredient", ingredients);
		
		return "recipes/ingredients/ingredientform";
	}

	@PostMapping("")
	public String saveOrUpdate(@PathVariable Long recipeId, @ModelAttribute IngredientCommand ingredientCommand) {
		IngredientCommand stored = ingredientService.saveIngredient(ingredientCommand);
		
		return String.format("redirect:/recipes/%d/ingredients/%d", recipeId, stored.getId());
	}
	
}
