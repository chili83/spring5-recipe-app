package guru.springframework.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecipeCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIngredientSet() {
		RecipeCommand recipeCommand = new RecipeCommand();
		IngredientCommand ic = new IngredientCommand();
		ic.setId(1L);
		ic.setDescription("Test");
		recipeCommand.addIngredient(ic);
		
		assertTrue(recipeCommand.getIngredients().contains(ic));
		
		recipeCommand.getIngredients().remove(ic);
		assertTrue(!recipeCommand.getIngredients().contains(ic));
	}

}
