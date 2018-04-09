package guru.springframework.controllers;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;

public class IngredientsControllerTest {

	IngredientsController ingredientsController;
	
	@Mock
	RecipeService recipeService;
	
	@Mock
	IngredientService ingredientService;
	
	@Mock
	UnitOfMeasureService unitOfMeasureService;
	
	@Mock
	Model model;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		//ingredientService = new IngredientServiceImpl(recipeService);
		ingredientsController = new IngredientsController( ingredientService, recipeService, unitOfMeasureService );
	}
	
	//Create
	@Test
	public void createTest() throws Exception {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);

		Set<UnitOfMeasureCommand> uoms = new HashSet<>();
		UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
		uom.setId(212L);
		uom.setDescription("TestEinheit");
		uoms.add(uom);
		
		when(  unitOfMeasureService.getUoms()).thenReturn(uoms);
		when(  recipeService.getRecipe(Mockito.eq(recipe.getId()))).thenReturn(recipe);
		
		//When
		MockMvcBuilders.standaloneSetup(ingredientsController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients/create", recipe.getId())))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/ingredients/ingredientform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe", "ingredient", "uomList"))
			.andExpect(MockMvcResultMatchers.model().attribute("ingredient", Matchers.hasProperty("recipeId", Matchers.equalTo(recipe.getId()))))
			.andExpect(MockMvcResultMatchers.model().attribute("uomList", Matchers.iterableWithSize(1)));
		
		//Then
		verify(recipeService, times(1)).getRecipe(Mockito.eq(recipe.getId()));
		
	}
	
	//Read
	@Test
	public void showAllTest() throws Exception {
		
		//Given
		RecipeCommand cmd = new RecipeCommand();
		cmd.setId(1L);
		when(recipeService.getRecipe(Mockito.eq(1L))).thenReturn(cmd);
		
		//When
		MockMvcBuilders.standaloneSetup(ingredientsController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients", 1)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/ingredients/ingredients"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
		
		
	}
	
	//Update
	@Test
	public void saveIngredient() throws Exception {
		RecipeCommand storedRecipe = new RecipeCommand();
		storedRecipe.setId(1L);
		storedRecipe.setDescription("Tiramisu");
		
		IngredientCommand newIngredient = new IngredientCommand();
		newIngredient.setId(101L);
		newIngredient.setDescription("Test Ingredient");
		newIngredient.setRecipeId(storedRecipe.getId());
		
		when(recipeService.getRecipe(Mockito.eq(1L))).thenReturn(storedRecipe);
		when(ingredientService.saveIngredient(Mockito.any())).thenReturn(newIngredient);
		
		MockMvcBuilders.standaloneSetup(ingredientsController).build()
			.perform(MockMvcRequestBuilders.post(String.format("/recipes/%d/ingredients", storedRecipe.getId() ))
					.param("id", ""+newIngredient.getId())
					.param("description", ""+newIngredient.getDescription())
					.param("recipeId", ""+newIngredient.getRecipeId())
					)
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/recipes/%d/ingredients",storedRecipe.getId())));
		
		verify(ingredientService, times(1)).saveIngredient(Mockito.any());
	}
	

	
	
	
	
	
	

	
}
