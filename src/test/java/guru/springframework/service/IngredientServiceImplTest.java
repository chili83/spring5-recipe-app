package guru.springframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;

public class IngredientServiceImplTest {

	@Mock
	RecipeService recipeService;
	IngredientService ingredientService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.ingredientService = new IngredientServiceImpl(recipeService);
	}
	
	@Test
	public void getIngredients() {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("TestRezept");
		
		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(1L);
		ingredient.setDescription("Test Ingredient 1");
		recipe.addIngredient(ingredient);

		ingredient = new IngredientCommand();
		ingredient.setId(2L);
		ingredient.setDescription("Test Ingredient 2");
		recipe.addIngredient(ingredient);
		
		ingredient = new IngredientCommand();
		ingredient.setId(3L);
		ingredient.setDescription("Test Ingredient 3");
		recipe.addIngredient(ingredient);
		
		when(recipeService.getRecipe(Mockito.eq(recipe.getId()))).thenReturn(recipe);
		
		//When
		Set<IngredientCommand> ingredients = ingredientService.getIngredients(recipe.getId()); 
		
		//Then
		verify(recipeService, times(1)).getRecipe(Mockito.eq(recipe.getId()));
		assertEquals(recipe.getIngredients().size(), ingredients.size());
	}
	
	@Test
	public void getIngredient() {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("TestRezept");
		
		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(2L);
		ingredient.setDescription("Test Ingredient 2");
		recipe.addIngredient(ingredient);
		
		UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
		uom.setId(232L);
		uom.setDescription("TestEinheit");
		ingredient.setUom(uom);
		
		when(recipeService.getRecipe(recipe.getId())).thenReturn(recipe);
		
		//When
		IngredientCommand ingredientResult = ingredientService.getIngredient(recipe.getId(), 2L);
		
		//Then
		verify(recipeService, times(1)).getRecipe(Mockito.eq(recipe.getId()));
		assertEquals(ingredientResult.getId(), new Long(2L));
		assertEquals(ingredientResult.getUom().getId(), uom.getId());
		
	}
	
	@Test
	public void saveIngredient() {
		//Given
		RecipeCommand testCommand = new RecipeCommand();
		testCommand.setId(1L);
		testCommand.setDescription("Tiramisu");
		
		IngredientCommand newIngredient = new IngredientCommand();
		newIngredient.setDescription("Test Ingredient");
		newIngredient.setRecipeId(testCommand.getId());
		
		UnitOfMeasureCommand newUom = new UnitOfMeasureCommand();
		newUom.setDescription("Test UOM");
		newIngredient.setUom(newUom);

		RecipeCommand storedRecipe = new RecipeCommand();
		storedRecipe.setId(testCommand.getId());
		storedRecipe.setDescription(testCommand.getDescription());
		
		IngredientCommand storedIngredient = new IngredientCommand();
		storedIngredient.setId(101L);
		storedIngredient.setDescription("Test Ingredient");
		storedIngredient.setRecipeId(testCommand.getId());
		storedIngredient.setUUID(newIngredient.getUUID());

		UnitOfMeasureCommand storedUom = new UnitOfMeasureCommand();
		storedUom.setId(202L);
		storedUom.setDescription(newUom.getDescription());
		storedIngredient.setUom(storedUom);
		
		storedRecipe.addIngredient(storedIngredient);
		
		when(recipeService.getRecipe(Mockito.eq(testCommand.getId()))).thenReturn(testCommand);
		when(recipeService.saveRecipe(Mockito.eq(testCommand))).thenReturn(storedRecipe);
		
		//When
		IngredientCommand result = ingredientService.saveIngredient(newIngredient);
		
		//Then
		verify(recipeService, times(1)).getRecipe(Mockito.eq(testCommand.getId()));
		verify(recipeService, times(1)).saveRecipe(Mockito.eq(testCommand));
		
		assertEquals(result, storedIngredient);
		assertEquals(newIngredient.getUUID(), storedIngredient.getUUID());
		assertEquals(storedIngredient.getUom(), storedUom);
		
	}
	
	@Test
	public void deleteIngredient() {
		//Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(2L);
		ingredient.setDescription("Test Ingredient");
		ingredient.setRecipeId(recipe.getId());
		
		UnitOfMeasureCommand newUom = new UnitOfMeasureCommand();
		newUom.setId(202L);
		newUom.setDescription("Test UOM");

		ingredient.setUom(newUom);
		recipe.addIngredient(ingredient);
		
		when(recipeService.getRecipe(Mockito.eq(ingredient.getRecipeId()))).thenReturn(recipe);
		
		//When
		ingredientService.deleteIngredient(recipe.getId(), ingredient.getId());
		RecipeCommand storedResult = recipeService.getRecipe(recipe.getId());
		
		//Then
		assertFalse(storedResult.getIngredients().size() > 0);
	}
}
