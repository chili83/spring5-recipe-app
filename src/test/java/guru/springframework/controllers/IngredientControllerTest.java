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

public class IngredientControllerTest {

	IngredientController ingredientController;
	
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
		ingredientController = new IngredientController( ingredientService, recipeService, unitOfMeasureService );
	}
	
	@Test
	public void viewIngredientsPage() throws Exception {
		
		//Given
		RecipeCommand cmd = new RecipeCommand();
		cmd.setId(1L);
		when(recipeService.getRecipe(Mockito.eq(1L))).thenReturn(cmd);
		
		//When
		MockMvcBuilders.standaloneSetup(ingredientController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients", 1)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/ingredients/ingredients"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
		
		
	}
	
	@Test
	public void viewIngredientPage() throws Exception {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("TestRecipe");
		
		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(101L);
		ingredient.setDescription("Test Ingredient");
		ingredient.setRecipeId(recipe.getId());
		
		recipe.addIngredient(ingredient);
		
		when(	recipeService.getRecipe(Mockito.eq(recipe.getId()))	).thenReturn(recipe);
		when(	ingredientService.getIngredient(Mockito.eq(recipe.getId()),		Mockito.eq(ingredient.getId()) )	).thenReturn(ingredient);
		
		//When
		MockMvcBuilders.standaloneSetup(ingredientController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients/%d", recipe.getId(), ingredient.getId())))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/ingredients/ingredient"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe", "ingredient"));
		
		//Then
		verify(ingredientService, times(1)).getIngredient(Mockito.eq(recipe.getId()), Mockito.eq(ingredient.getId()));
		verify(recipeService, times(1)).getRecipe(Mockito.eq(recipe.getId()));
	}
	
	@Test
	public void newIngredientPage() throws Exception {
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
		MockMvcBuilders.standaloneSetup(ingredientController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients/new", recipe.getId())))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/ingredients/ingredientform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe", "ingredient", "uomList"))
			.andExpect(MockMvcResultMatchers.model().attribute("ingredient", Matchers.hasProperty("recipeId", Matchers.equalTo(recipe.getId()))))
			.andExpect(MockMvcResultMatchers.model().attribute("uomList", Matchers.iterableWithSize(1)));
		
		//Then
		verify(recipeService, times(1)).getRecipe(Mockito.eq(recipe.getId()));
		
	}
	
	@Test
	public void editIngredientPage() throws Exception {
		
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		
		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(101L);
		ingredient.setDescription("Test Ingredient");
		ingredient.setRecipeId(recipe.getId());
		
		UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
		uom.setId(212L);
		uom.setDescription("TestEinheit");
		ingredient.setUom(uom);
		
		recipe.addIngredient(ingredient);
		
		when(  recipeService.getRecipe(Mockito.eq(recipe.getId()))).thenReturn(recipe);
		when(	ingredientService.getIngredient(Mockito.eq(recipe.getId()),		Mockito.eq(ingredient.getId()) )	).thenReturn(ingredient);

		//When
		MockMvcBuilders.standaloneSetup(ingredientController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients/%d/edit", recipe.getId(), ingredient.getId())))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("recipes/ingredients/ingredientform"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recipe", "ingredient", "uomList"))
			.andExpect(MockMvcResultMatchers.model().attribute("ingredient", Matchers.hasProperty("uom", Matchers.notNullValue())));
		
		//Then
		verify(ingredientService, times(1)).getIngredient(Mockito.eq(recipe.getId()), Mockito.eq(ingredient.getId()));
	}

	@Test
	public void saveIngredient() throws Exception {
		RecipeCommand testCommand = new RecipeCommand();
		testCommand.setId(1L);
		testCommand.setDescription("Tiramisu");
		
		IngredientCommand newIngredient = new IngredientCommand();
		newIngredient.setDescription("Test Ingredient");
		newIngredient.setRecipeId(testCommand.getId());

		IngredientCommand storedIngredient = new IngredientCommand();
		storedIngredient.setId(101L);
		storedIngredient.setDescription(newIngredient.getDescription());
		storedIngredient.setRecipeId(newIngredient.getRecipeId());
		
		when(ingredientService.saveIngredient(Mockito.any())).thenReturn(storedIngredient);
		
		MockMvcBuilders.standaloneSetup(ingredientController).build()
			.perform(MockMvcRequestBuilders.post(String.format("/recipes/%d/ingredients/", testCommand.getId() ), newIngredient))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/recipes/%d/ingredients",testCommand.getId())));
		
		verify(ingredientService, times(1)).saveIngredient(Mockito.any());
	}
	
	@Test
	public void deleteIngredient() throws Exception {
		//Given
		RecipeCommand testCommand = new RecipeCommand();
		testCommand.setId(1L);
		testCommand.setDescription("Tiramisu");
		
		IngredientCommand newIngredient = new IngredientCommand();
		newIngredient.setId(1L);
		newIngredient.setDescription("Test Ingredient");
		newIngredient.setRecipeId(testCommand.getId());
		testCommand.addIngredient(newIngredient);
		
		//When
		MockMvcBuilders.standaloneSetup(ingredientController).build()
			.perform(MockMvcRequestBuilders.get(String.format("/recipes/%d/ingredients/%d/delete", testCommand.getId(), newIngredient.getId())))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/recipes/%d/ingredients", testCommand.getId())))
			;
		
	}

}
