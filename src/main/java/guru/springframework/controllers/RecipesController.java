package guru.springframework.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(RecipesController.BASE_URI)
public class RecipesController {
	
	public static final String BASE_URI = "/recipes";

	private RecipeService recipeService;

	public RecipesController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping("/create")
	public String create(Model mdl) {
		mdl.addAttribute("recipe", new RecipeCommand());
		return "recipes/recipeform";
	}

	@GetMapping
	public String showAll(Model mdl){
		mdl.addAttribute("recipes", recipeService.getRecipes());
		return "recipes/recipes";
	}
	
	@PostMapping
	public String save(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			for (ObjectError err : bindingResult.getAllErrors()) {
				log.debug(err.toString());
			}
			return "recipes/recipeform";
		}
		
		RecipeCommand result = recipeService.saveRecipe(command);
		return "redirect:/recipes/" + result.getId();
	}
	
}
