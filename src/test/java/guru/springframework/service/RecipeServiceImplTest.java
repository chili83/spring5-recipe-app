package guru.springframework.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
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

	@Test
	public void getRecipes() {
		
		Set<Recipe> recipesData = new HashSet<>();
		recipesData.add(new Recipe());
		
		//Mockito: Wenn service.getRecipes() aufgerufen wird, returne stattdessen ein HashSet
		when(service.getRecipes()).thenReturn(recipesData);
		
		
		Set<Recipe> recipes = service.getRecipes();
		//JUnit: Stelle sicher, das es genau ein Rezept gibt.
		assertEquals(recipes.size(), 1);
		//Mockito: Stelle sicher das die findAll() methode nur einmal aufgerufen wurde
		verify(repository, Mockito.times(1)).findAll();
		
	}

}
