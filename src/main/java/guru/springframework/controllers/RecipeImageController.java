package guru.springframework.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;

@Controller
@RequestMapping(RecipeImageController.BASE_URI)
public class RecipeImageController {
	
	public static final String BASE_URI = RecipeController.BASE_URI+"/image";
	
	RecipeService recipeService;
	
	@Autowired
	public RecipeImageController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}
	
	
	
	//Read
	
	//Edit
	@GetMapping("/edit")
	public String edit(@PathVariable Long recipeId, Model mdl) {
		
		RecipeCommand recipe = recipeService.getRecipe(recipeId);
		mdl.addAttribute("recipe",recipe);
		return "recipes/imageform";
		
	}
	
	//Update
	
	@PostMapping
	public String update(@PathVariable Long recipeId, @RequestParam("imagefile") MultipartFile file) {
		
		try {
			recipeService.addRecipeImage(recipeId, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/recipes/"+recipeId;
	}
	
	
	//Delete
}
