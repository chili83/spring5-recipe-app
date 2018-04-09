package guru.springframework.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	
	//Read
	@Test
	public void showByIdTest() throws Exception {
		
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		//Given
		when(recipeService.getRecipe(Mockito.anyLong())).thenReturn(recipe);
		//When
		//Then
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipeController).build();
		mock.perform(MockMvcRequestBuilders.get("/recipes/1"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/recipe"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
		
		//Prüfe ob das Model das Rezept enthält
		/*String viewName = recipeController.getRecipePage(String.valueOf(recipe.getId()), model);
		ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
		verify(model, times(1)).addAttribute(Mockito.eq("recipe"), recipeCaptor.capture());
		assertEquals(recipe, recipeCaptor.getValue());
		*/
	}
	
	//Edit
	@Test
	public void editTest() throws Exception {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		when (recipeService.getRecipe(Mockito.eq(1L))).thenReturn(recipe);
		
		//When/Then		
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipeController).build();
		mock.perform(MockMvcRequestBuilders.get("/recipes/1/edit"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/recipeform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));}
	
	//Update
	@Test
	public void updateTest() throws Exception {
		RecipeCommand testCommand = new RecipeCommand();
		testCommand.setId(1L);
		testCommand.setDescription("Tiramisu");
		testCommand.setDirections("Test Direction");
		
		RecipeCommand storedCommand = new RecipeCommand();
		storedCommand.setId(1L);
		storedCommand.setDescription("New Description");
		storedCommand.setDirections("New Direction");
		
		when(recipeService.getRecipe(testCommand.getId())).thenReturn(testCommand);
		when(recipeService.saveRecipe(Mockito.any())).thenReturn(storedCommand);
		
		MockMvcBuilders.standaloneSetup(recipeController).build()
				.perform(MockMvcRequestBuilders.post("/recipes/1")
					.param("description", storedCommand.getDescription())
					.param("directions", storedCommand.getDirections())
				)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/recipes/"+storedCommand.getId()));
		
		verify(recipeService, times(1)).saveRecipe(Mockito.any());
	}
	
	//Delete
	@Test
	public void deleteTest() throws Exception {
		//Given
		Long recipeId = 1L;
		
		//When/Then		
		RequestBuilder request = MockMvcRequestBuilders.get("/recipes/"+recipeId+"/delete");
		
		MockMvc mock = MockMvcBuilders.standaloneSetup(recipeController).build();
			    mock.perform(request)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/recipes"));
		//Then
		verify(recipeService, times(1)).deleteRecipeById(recipeId);
	}
	
	
	//UNHAPPY PATH
	
	@Test
	public void showByIDBadRequestTest() throws Exception {
		//Given
		when(recipeService.getRecipe(Mockito.anyLong())).thenThrow(NotFoundException.class);
		
		//When
		MockMvcBuilders.standaloneSetup(recipeController).setControllerAdvice(new ControllerExceptionHandler()).build()
		.perform(MockMvcRequestBuilders.get("/recipes/ASD"))
		.andExpect(status().isBadRequest())
		.andExpect(view().name("400error"));
	}
	
	@Test
	public void showByIDNotFoundTest() throws Exception {
		//Given
		when(recipeService.getRecipe(Mockito.anyLong())).thenThrow(NotFoundException.class);
		
		//When
		MockMvcBuilders.standaloneSetup(recipeController).setControllerAdvice(new ControllerExceptionHandler()).build()
			.perform(MockMvcRequestBuilders.get("/recipes/1"))
			.andExpect(status().isNotFound())
			.andExpect(view().name("404error"));
		
	}
	

}
