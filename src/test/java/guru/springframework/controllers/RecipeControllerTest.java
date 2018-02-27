package guru.springframework.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

	@Test
	public void getRecipesPage() {
		
		//Given
		Set<Recipe> recipesData = new HashSet<>();
		Recipe testRecipe = new Recipe();
		testRecipe.setId(1L);
		testRecipe.setDescription("Tiramisu");
		recipesData.add(testRecipe);

		//When
		when(recipeService.getRecipes()).thenReturn(recipesData);
		
		//Then
		String recipePage = recipeController.getRecipesPage(model);
		assertNotNull(recipePage);
		assertTrue(recipePage.length() > 0);
		
		
		
		verify(recipeService, times(1)).getRecipes();
		//Stelle sicher das die addAttribute Methode nur einmal aufgerufen wird um ein Attribute für 'recipes' hinzuzufügen.
		verify(model, times(1)).addAttribute(Mockito.eq("recipes"), Mockito.anySet());

		//Stelle sicher das die addAttribute Methode nur einmal aufgerufen wird um ein Attribute für 'recipes' hinzuzufügen. Speichere dafür das Argument was vom Controller hinzugefügt wird.(Set<Recipes>)
		ArgumentCaptor<Set<Recipe>> recipesTypeCaptor = ArgumentCaptor.forClass(Set.class);
		verify(model, times(1)).addAttribute(Mockito.eq("recipes"), recipesTypeCaptor.capture());
		//Hole Argument mittels getValue
		Set<Recipe> recipesAddedByController = recipesTypeCaptor.getValue();
		//Stelle sicher das es das gleiche ist wie oben angelegt.
		assertEquals(recipesAddedByController, recipesData);
	
	}

}
