package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

	private RecipeService recipeService;


	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}


	@GetMapping("")
	public String getRecipesPage(Model mdl){
		mdl.addAttribute("recipes", recipeService.getRecipes());
		return "recipes/recipes";
	}
	
	@GetMapping("{id}")
	public String getRecipePage(@PathVariable String id, Model mdl) {
		
		RecipeCommand recipe = recipeService.getRecipe(new Long(id));
		mdl.addAttribute("recipe",recipe);
		
		return "recipes/recipe";
	}
	
	@GetMapping("/new")
	public String newRecipe(Model mdl) {
		mdl.addAttribute("recipe", new RecipeCommand());
		return "recipes/recipeform";
	}
	@GetMapping("/{id}/edit")
	public String editRecipe(@PathVariable Long id, Model mdl) {
		mdl.addAttribute("recipe", recipeService.getRecipe(id));
		return "recipes/recipeform";
	}
	
	@DeleteMapping
	@RequestMapping("/{id}/delete")
	public String deleteRecipe(@PathVariable Long id) {
		recipeService.deleteRecipeById(id);
		return "redirect:/recipes";
	}
	
	
	@PostMapping(""/*name="", method=RequestMethod.POST*/)
	public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
		RecipeCommand result = recipeService.saveRecipe(command);
		return "redirect:/recipes/" + result.getId();
	}
	
	
	
	
}
