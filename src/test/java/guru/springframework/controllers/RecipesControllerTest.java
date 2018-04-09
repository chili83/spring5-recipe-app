package guru.springframework.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;

public class RecipesControllerTest {
	
	@Mock
	RecipeService recipeService;
	
	@Mock
	Model model;
	
	RecipesController recipesController;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		recipesController = new RecipesController(recipeService);

	}
	
	//Create
	@Test
	public void create() throws Exception {
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipesController).build();
		mock.perform(MockMvcRequestBuilders.get("/recipes/create"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/recipeform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
	}
	
	//Read
	@Test
	public void showAllTest() throws Exception {
		
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		Set<RecipeCommand> recipesData = new HashSet<>();
		recipesData.add(recipe);
		
		when(recipeService.getRecipes()).thenReturn(recipesData);
		
		//When
		//Then
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipesController).build();
		mock.perform(MockMvcRequestBuilders.get("/recipes/"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/recipes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipes"));
		
		//String recipePage = recipeController.getRecipesPage(model);
		/* Bereits über performGetTest geprüft
		 * assertNotNull(recipePage);
		 * assertTrue(recipePage.length() > 0);
		*/
		
		//Stelle sicher das getRecipes() nur einmal aufgerufen wurde
		/* verify(recipeService, times(1)).getRecipes(); */
		//Stelle sicher das die addAttribute Methode nur einmal aufgerufen wird um ein Attribute für 'recipes' hinzuzufügen.
		/* verify(model, times(1)).addAttribute(Mockito.eq("recipes"), Mockito.anySet());*/
		/*
		//Stelle sicher das die addAttribute Methode nur einmal aufgerufen wird um ein Attribute für 'recipes' hinzuzufügen. Speichere dafür das Argument was vom Controller hinzugefügt wird.(Set<Recipes>)
		ArgumentCaptor<Set<Recipe>> recipesTypeCaptor = ArgumentCaptor.forClass(Set.class);
		verify(model, times(1)).addAttribute(Mockito.eq("recipes"), recipesTypeCaptor.capture());
		//Hole Argument mittels getValue
		Set<Recipe> recipesAddedByController = recipesTypeCaptor.getValue();
		//Stelle sicher das es das gleiche ist wie oben angelegt.
		assertEquals(recipesAddedByController, recipesData);
		*/
	}
	
	@Test
	public void saveTest() throws Exception {
		RecipeCommand newRecipe = new RecipeCommand();
		newRecipe.setId(1L);
		newRecipe.setDescription("Tiramisu");
		newRecipe.setDirections("Test Direction");
		
		when(recipeService.saveRecipe(Mockito.any())).thenReturn(newRecipe);
		
		MockMvcBuilders.standaloneSetup(recipesController).build()
				.perform(MockMvcRequestBuilders.post("/recipes")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		                .param("id", "")
		                .param("description", "some string")
		                .param("directions", "some directions")
				)
						
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/recipes/"+newRecipe.getId()));
		
		verify(recipeService, times(1)).saveRecipe(Mockito.any());
	}
	

	
	
	
	@Test
	public void postNewRecipeFormValidationFail() throws Exception {
		//Given
		RecipeCommand command = new RecipeCommand();
		command.setId(1L);
		
		when(recipeService.saveRecipe(Mockito.any())).thenReturn(command);
		
		//When
		MockMvcBuilders.standaloneSetup(recipesController).build().perform(
				MockMvcRequestBuilders.post("/recipes").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "")
			)
		.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"))
		.andExpect(MockMvcResultMatchers.view().name("recipes/recipeform"));
		
	}	
	

	
}
