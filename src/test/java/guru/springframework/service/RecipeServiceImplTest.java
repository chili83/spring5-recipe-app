package guru.springframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;

public class RecipeServiceImplTest {
	
	RecipeService service;
	
	@Mock
	RecipeRepository repository;

	@Before
	public void setUp() throws Exception {
		
		//Initialize Mocks...
		MockitoAnnotations.initMocks(this);
		
		service = new RecipeServiceImpl(repository);
		
	}

	public Recipe newTestRecipe(Long id, String description) {
		Recipe recipe = new Recipe();
		recipe.setId(id);
		recipe.setDescription(description);
		return recipe;
	}
	
	@Test
	public void getRecipe() {
		
		Recipe recipe = newTestRecipe(1L, "Tiramisu");
		//When
		when(repository.findById(Mockito.eq(recipe.getId()))).thenReturn(Optional.of(recipe));
		
		Recipe result = service.getRecipe(recipe.getId());
		assertSame(recipe, result);
		
	}
	
	
	@Test
	public void getRecipes() {
		
		//Given
		Set<Recipe> recipesData = new HashSet<>();
		recipesData.add(newTestRecipe(2L, "Tiramisu"));
		
		//When
		//Mockito: Wenn repository.findAll() aufgerufen wird, returne stattdessen ein HashSet
		when(repository.findAll()).thenReturn(recipesData);

		
		//Then
		Set<Recipe> result = service.getRecipes();
		//recipesData.add(newTestRecipe(3L, "ErdbeerTorte"));
		assertEquals(recipesData, result);
		assertNotSame(recipesData, result);
		
	}

}
