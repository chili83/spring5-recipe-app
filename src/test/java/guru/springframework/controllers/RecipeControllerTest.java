package guru.springframework.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.domain.Recipe;
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
	
	public Recipe getRecipe() {
		Recipe testRecipe = new Recipe();
		testRecipe.setId(1L);
		testRecipe.setDescription("Tiramisu");
		return testRecipe;
	}
	
	public Set<Recipe> getRecipes(){
		Set<Recipe> recipesData = new HashSet<>();
		recipesData.add(getRecipe());
		return recipesData;
		
	}
	
	public void performGetTest(String uri, String viewName) throws Exception {
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipeController).build();
		mock.perform(MockMvcRequestBuilders.get(uri))
			.andExpect(MockMvcResultMatchers.view().name(viewName))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void getRecipePage() throws Exception {
		performGetTest("/recipes/1", "recipes/recipe");
		
		//Given
		Recipe recipe = getRecipe();
		//When
		when(recipeService.getRecipe(Mockito.anyLong())).thenReturn(recipe);
		//Then
		
		//Prüfe ob das Model das Rezept enthält
		String viewName = recipeController.getRecipePage(String.valueOf(recipe.getId()), model);
		ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
		verify(model, times(1)).addAttribute(Mockito.eq("recipe"), recipeCaptor.capture());
		assertEquals(recipe, recipeCaptor.getValue());
	}

	@Test
	public void getRecipesPage() throws Exception {
		performGetTest("/recipes/", "recipes/recipes");
		
		//Given
		Set<Recipe> recipesData = getRecipes();
		
		//When
		when(recipeService.getRecipes()).thenReturn(recipesData);
		
		//Then
		String recipePage = recipeController.getRecipesPage(model);
		/* Bereits über performGetTest geprüft
		 * assertNotNull(recipePage);
		 * assertTrue(recipePage.length() > 0);
		*/
		
		//Stelle sicher das getRecipes() nur einmal aufgerufen wurde
		/* verify(recipeService, times(1)).getRecipes(); */
		//Stelle sicher das die addAttribute Methode nur einmal aufgerufen wird um ein Attribute für 'recipes' hinzuzufügen.
		/* verify(model, times(1)).addAttribute(Mockito.eq("recipes"), Mockito.anySet());*/

		//Stelle sicher das die addAttribute Methode nur einmal aufgerufen wird um ein Attribute für 'recipes' hinzuzufügen. Speichere dafür das Argument was vom Controller hinzugefügt wird.(Set<Recipes>)
		ArgumentCaptor<Set<Recipe>> recipesTypeCaptor = ArgumentCaptor.forClass(Set.class);
		verify(model, times(1)).addAttribute(Mockito.eq("recipes"), recipesTypeCaptor.capture());
		//Hole Argument mittels getValue
		Set<Recipe> recipesAddedByController = recipesTypeCaptor.getValue();
		//Stelle sicher das es das gleiche ist wie oben angelegt.
		assertEquals(recipesAddedByController, recipesData);
	}

}
