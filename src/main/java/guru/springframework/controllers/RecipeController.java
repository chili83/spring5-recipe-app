package guru.springframework.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
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
	
	@GetMapping("/{id}/image")
	public String editRecipeImage(@PathVariable Long id, Model mdl) {
		
		RecipeCommand recipe = recipeService.getRecipe(id);
		mdl.addAttribute("recipe",recipe);
		return "recipes/imageform";
		
	}
	
	@PostMapping("/{id}/image")
	public String saveRecipeImage(@PathVariable Long id, @RequestParam("imagefile") MultipartFile file) {
		
		try {
			recipeService.addRecipeImage(id, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/recipes/"+id;
	}
	
	
	@PostMapping(""/*name="", method=RequestMethod.POST*/)
	public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
		RecipeCommand result = recipeService.saveRecipe(command);
		return "redirect:/recipes/" + result.getId();
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(NotFoundException e) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception",e);
		mv.setViewName("404error");
		return mv;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NumberFormatException.class)
	public ModelAndView handleNotFound(Exception e) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception",e);
		mv.setViewName("400error");
		return mv;
	}
	
	
	
}
