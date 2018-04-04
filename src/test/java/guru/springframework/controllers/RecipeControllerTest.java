package guru.springframework.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.commands.RecipeCommand;
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
	
	
	
	
	@Test
	public void getRecipePage() throws Exception {
		
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
	
	@Test
	public void getRecipesPage() throws Exception {
		
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		Set<RecipeCommand> recipesData = new HashSet<>();
		recipesData.add(recipe);
		
		when(recipeService.getRecipes()).thenReturn(recipesData);
		
		//When
		//Then
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipeController).build();
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
	public void newRecipesPage() throws Exception {
		MockMvc mock = new MockMvcBuilders().standaloneSetup(recipeController).build();
		mock.perform(MockMvcRequestBuilders.get("/recipes/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/recipeform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
	}

	@Test
	public void editRecipesPage() throws Exception {
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

	@Test
	public void getDeleteRecipesPage() throws Exception {
		//Given
		Long recipeId = 1L;
		
		//When/Then		
		RequestBuilder request = false ? MockMvcRequestBuilders.delete("/recipes/"+recipeId+"/delete") : MockMvcRequestBuilders.get("/recipes/"+recipeId+"/delete");
		
		MockMvc mock = MockMvcBuilders.standaloneSetup(recipeController).build();
			    mock.perform(request)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/recipes"));
		//Then
		verify(recipeService, times(1)).deleteRecipeById(recipeId);
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

	@Test
	public void getRecipeImagePage() throws Exception {
		
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		String content = "test image";

		Byte[] testImage = new Byte[content.getBytes().length];
		int i = 0;
		for(byte b : content.getBytes()) {
			testImage[i++] = b;
		}
		recipe.setImage(testImage);
		
		when(recipeService.getRecipe(Mockito.eq(recipe.getId()))).thenReturn(recipe);
		
		//When
		MockMvcBuilders.standaloneSetup(recipeController).build()
			.perform(MockMvcRequestBuilders.get("/recipes/1/image"))
			.andExpect(MockMvcResultMatchers.view().name("recipes/imageform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
		
		
	}

	@Test
	public void saveRecipeImage() throws Exception {
		//Given
		Long recipeID = 1L;
		MockMultipartFile file = new MockMultipartFile("imagefile", "Test".getBytes());
		
		
		//When
		MockMvcBuilders.standaloneSetup(recipeController).build()
			.perform(MockMvcRequestBuilders.multipart(String.format("/recipes/%s/image", recipeID)).file(file))
			.andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/recipes/%s", recipeID)));
		
		//Then
		verify(recipeService, times(1)).addRecipeImage(Mockito.eq(recipeID), Mockito.eq(file));
		verify(recipeService, times(1)).addRecipeImage(Mockito.eq(recipeID), Mockito.eq(file));
	}

}
