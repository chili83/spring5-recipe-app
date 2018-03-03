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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;

public class RecipeControllerTest {

	@Mock
	RecipeService recipeService;
	
	@Mock
	Model model;
	
	RecipeController recipeController;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		recipeController = new RecipeController(recipeService);

	}
	
	public RecipeCommand getRecipe() {
		RecipeCommand testRecipe = new RecipeCommand();
		testRecipe.setId(1L);
		testRecipe.setDescription("Tiramisu");
		return testRecipe;
	}
	
	public Set<RecipeCommand> getRecipes(){
		Set<RecipeCommand> recipesData = new HashSet<>();
		recipesData.add(getRecipe());
		return recipesData;
		
	}
	private void performGetTest(String uri, String viewName, String modelName) throws Exception {
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipeController).build();
		mock.perform(MockMvcRequestBuilders.get(uri))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(viewName))
			.andExpect(MockMvcResultMatchers.model().attributeExists(modelName));
	}
	
	
	
	@Test
	public void getRecipePage() throws Exception {
		
		//Given
		RecipeCommand recipe = getRecipe();
		//When
		when(recipeService.getRecipe(Mockito.anyLong())).thenReturn(recipe);
		//Then
		performGetTest("/recipes/1", "recipes/recipe", "recipe");
		
		//Prüfe ob das Model das Rezept enthält
		/*String viewName = recipeController.getRecipePage(String.valueOf(recipe.getId()), model);
		ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
		verify(model, times(1)).addAttribute(Mockito.eq("recipe"), recipeCaptor.capture());
		assertEquals(recipe, recipeCaptor.getValue());
		*/
	}

	@Test
	public void getRecipesPage() throws Exception {
		
		//Given
		Set<RecipeCommand> recipesData = getRecipes();
		when(recipeService.getRecipes()).thenReturn(recipesData);
		
		//When
		//Then
		performGetTest("/recipes/", "recipes/recipes", "recipes");
		

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
	public void newRecipesPage() throws Exception {
		performGetTest("/recipes/new", "recipes/recipeform", "recipe");
	}

	@Test
	public void editRecipesPage() throws Exception {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		when (recipeService.getRecipe(Mockito.eq(1L))).thenReturn(recipe);
		
		//When/Then		
		performGetTest("/recipes/1/edit", "recipes/recipeform", "recipe");
	}
	@Test
	public void deleteRecipesPage() throws Exception {
		//Given
		Long recipeId = 1L;
		
		//When/Then		
		performDeleteTest("/recipes/"+recipeId+"/delete", "/recipes", false);
		
		//Then
		verify(recipeService, times(1)).deleteRecipeById(recipeId);
	}
	
	private void performDeleteTest(String uri, String viewName, boolean trueDelete) throws Exception {
		
		RequestBuilder request = trueDelete ? MockMvcRequestBuilders.delete(uri) : MockMvcRequestBuilders.get(uri);
		
		MockMvc mock = MockMvcBuilders.standaloneSetup(recipeController).build();
			    mock.perform(request)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl(viewName));
		
	}

	@Test
	public void saveRecipesPage() throws Exception {
		RecipeCommand testCommand = new RecipeCommand();
		testCommand.setDescription("Tiramisu");
		
		RecipeCommand storedCommand = new RecipeCommand();
		storedCommand.setId(1L);
		storedCommand.setDescription(testCommand.getDescription());
		
		when(recipeService.saveRecipe(Mockito.any())).thenReturn(storedCommand);
		when(recipeService.getRecipe(storedCommand.getId())).thenReturn(storedCommand);
		
		MockMvcBuilders.standaloneSetup(recipeController).build()
				.perform(MockMvcRequestBuilders.post("/recipes", testCommand))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/recipes/"+storedCommand.getId()));
		
		verify(recipeService, times(1)).saveRecipe(Mockito.any());
	}

	
}
