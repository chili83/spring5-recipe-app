package guru.springframework.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(RecipeController.BASE_URI)
public class RecipeController {
	
	public static final String BASE_URI = RecipesController.BASE_URI + "/{recipeId}";

	private RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}
	
	//Read
	@GetMapping
	public String showById(@PathVariable String recipeId, Model mdl) {
		
		RecipeCommand recipe = recipeService.getRecipe(new Long(recipeId));
		mdl.addAttribute("recipe",recipe);
		
		return "recipes/recipe";
	}
	
	//Edit
	@GetMapping("/edit")
	public String edit(@PathVariable String recipeId, Model mdl) {
		mdl.addAttribute("recipe", recipeService.getRecipe(Long.parseLong(recipeId)));
		return "recipes/recipeform";
	}
	
	//Update
	@PostMapping
	public String update(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			
			for (ObjectError err : bindingResult.getAllErrors()) {
				log.debug(err.toString());
			}
			
			return "recipes/recipeform";
		}
		
		RecipeCommand result = recipeService.saveRecipe(command);
		return "redirect:/recipes/" + result.getId();
	}
	
	
	//Delete
	@GetMapping("/delete")
	public String getDelete(@PathVariable String recipeId) {
		return delete(recipeId);
	}
	
	@DeleteMapping
	public String delete(@PathVariable String recipeId){
		recipeService.deleteRecipeById(Long.parseLong(recipeId));
		return "redirect:/recipes";
	}
	
}
