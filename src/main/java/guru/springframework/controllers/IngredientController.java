package guru.springframework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;

@Controller
@RequestMapping(IngredientController.BASE_URI)
public class IngredientController {
	
	public static final String BASE_URI = IngredientsController.BASE_URI + "/{ingredientId}";
	
	IngredientService ingredientService;
	UnitOfMeasureService unitOfMeasureService;
	RecipeService recipeService;
	
	@Autowired
	public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {
		this.ingredientService = ingredientService;
		this.recipeService = recipeService;
		this.unitOfMeasureService = unitOfMeasureService;
	}
	

	@GetMapping
	public String showByID(@PathVariable Long recipeId, @PathVariable String ingredientId, Model model) {
		
		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		model.addAttribute("recipe", recipe);

		IngredientCommand ingredients = ingredientService.getIngredient(new Long(recipeId), new Long(ingredientId));
		model.addAttribute("ingredient", ingredients);
		
		return "recipes/ingredients/ingredient";
	}
	
	@GetMapping("/edit")
	public String edit(@PathVariable Long recipeId, @PathVariable String ingredientId, Model model) {
		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		model.addAttribute("recipe", recipe);

		IngredientCommand ingredients = ingredientService.getIngredient(new Long(recipeId), new Long(ingredientId));
		model.addAttribute("ingredient", ingredients);
		
		model.addAttribute("uomList", unitOfMeasureService.getUoms());
		
		return "recipes/ingredients/ingredientform";
	}

	@PostMapping
	public String update(@PathVariable Long recipeId, @ModelAttribute IngredientCommand ingredientCommand) {
		IngredientCommand stored = ingredientService.saveIngredient(ingredientCommand);
		return String.format("redirect:/recipes/%d/ingredients", recipeId);
	}

	@GetMapping("/delete")
	public String getDelete(@PathVariable Long recipeId, @PathVariable Long ingredientId) {
		return delete(recipeId, ingredientId);
	}

	@DeleteMapping
	public String delete(@PathVariable Long recipeId, @PathVariable Long ingredientId) {
		ingredientService.deleteIngredient(recipeId, ingredientId);
		return String.format("redirect:/recipes/%d/ingredients", recipeId);
	}
	
}
