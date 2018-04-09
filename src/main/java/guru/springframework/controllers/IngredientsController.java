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
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;

@Controller
@RequestMapping(IngredientsController.BASE_URI)
public class IngredientsController {

	public static final String BASE_URI = RecipeController.BASE_URI + "/ingredients";

	IngredientService ingredientService;
	UnitOfMeasureService unitOfMeasureService;
	RecipeService recipeService;

	@Autowired
	public IngredientsController(IngredientService ingredientService, RecipeService recipeService,
			UnitOfMeasureService unitOfMeasureService) {
		this.ingredientService = ingredientService;
		this.recipeService = recipeService;
		this.unitOfMeasureService = unitOfMeasureService;
	}
	
	//Create
	@GetMapping("/create")
	public String create(@PathVariable Long recipeId, Model model) {
		RecipeCommand rc = recipeService.getRecipe(recipeId);
		model.addAttribute("recipe", rc);
		IngredientCommand ic = new IngredientCommand();
		ic.setRecipeId(recipeId);
		model.addAttribute("ingredient", ic);

		// Add empty UnitOfMeasure
		ic.setUom(new UnitOfMeasureCommand());

		model.addAttribute("uomList", unitOfMeasureService.getUoms());

		return "recipes/ingredients/ingredientform";
	}
	
	//Read
	@GetMapping
	public String showAll(@PathVariable String recipeId, Model model) {

		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		model.addAttribute("recipe", recipe);

		return "recipes/ingredients/ingredients";
	}

	//Update
	@PostMapping
	public String save(@PathVariable Long recipeId, @ModelAttribute IngredientCommand ingredientCommand) {
		IngredientCommand stored = ingredientService.saveIngredient(ingredientCommand);
		return String.format("redirect:/recipes/%d", recipeId);
	}



}
