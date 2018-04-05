package guru.springframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;

public class RecipeServiceImplTest {
	
	RecipeService recipeService;
	
	@Mock
	RecipeRepository recipeRepository;
	
	@Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

	@Mock
	RecipeCommandToRecipe recipeCommandToRecipe;

	@Before
	public void setUp() throws Exception {
		
		//Initialize Mocks...
		MockitoAnnotations.initMocks(this);
		
		recipeService = new RecipeServiceImpl(recipeRepository, recipeToRecipeCommand, recipeCommandToRecipe);
		
	}

	
	@Test
    public void getRecipe() throws Exception {
		
		//Given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeRepository.findById(Mockito.eq(recipe.getId()))).thenReturn(Optional.of(recipe));
        when(recipeToRecipeCommand.convert(Mockito.any())).thenReturn(recipeCommand);

        //When
        RecipeCommand result = recipeService.getRecipe(1L);
        
        //Then
        assertNotNull("Null recipe returned", result);
        verify(recipeRepository, times(1)).findById(Mockito.anyLong());
        verify(recipeRepository, never()).findAll();
		assertSame(recipeCommand, result);

    }
	
	
	@Test
	public void getRecipes() {
		
		//Given
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");
		
		//From Repository
		Set<Recipe> recipesData = new HashSet<>();
		recipesData.add(recipe);
		
		when(recipeRepository.findAll()).thenReturn(recipesData);

		//From Converter
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(recipe.getId());
		recipeCommand.setDescription(recipe.getDescription());
		
		when(recipeToRecipeCommand.convert(Mockito.any())).thenReturn(recipeCommand);
		
		//When
		Set<RecipeCommand> result = recipeService.getRecipes();

		//Then
		assertEquals(result.size(), recipesData.size());
		
	}
	
	@Test
	public void deleteRecipe() {
		//Given
		Long id = 1L;
		
		//When
		recipeService.deleteRecipeById(id);

		//Then
		verify(recipeRepository, times(1)).deleteById(Mockito.eq(1L));
	}
	
	
	
	@Test
	public void createRecipe() {
		
		//Given 
		//to Store
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setDescription("Tiramisu");
		
		Recipe recipe = new Recipe();
		recipe.setDescription(recipeCommand.getDescription());

		when(recipeCommandToRecipe.convert(Mockito.any())).thenReturn(recipe);

		//stored
		Recipe storedRecipe = new Recipe();
		storedRecipe.setId(1L);
		
		RecipeCommand storedRecipeCommand = new RecipeCommand();
		storedRecipeCommand.setId(storedRecipe.getId());
		storedRecipeCommand.setDescription(storedRecipe.getDescription());

		when(recipeRepository.save(Mockito.any())).thenReturn(storedRecipe);
		when(recipeToRecipeCommand.convert(Mockito.any())).thenReturn(storedRecipeCommand);
		
		//When
		RecipeCommand storedResult = recipeService.saveRecipe(recipeCommand);
		
		//Then
		verify(recipeRepository, times(1)).save(Mockito.eq(recipe));
		assertNotNull("No Recipe returned", storedResult);
		assertNotNull("Recipe has no ID", storedResult.getId());
		assertEquals(storedResult.getId(), storedRecipe.getId());
		
	}
	
	@Test
	public void addRecipeImage() throws IOException {
		
		 //given
        Long id = 1L;
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "Spring Framework Guru".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipe.getId());

        when(recipeRepository.findById(Mockito.anyLong())).thenReturn(recipeOptional);
        when(recipeToRecipeCommand.convert(Mockito.any())).thenReturn(recipeCommand);
        when(recipeCommandToRecipe.convert(Mockito.any())).thenReturn(recipe);
        when(recipeRepository.save(Mockito.any(Recipe.class))).then(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock arg0) throws Throwable {
				recipe.setImage(recipeCommand.getImage());
				return null;
			}
		});

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        RecipeCommand savedRecipeResult = recipeService.addRecipeImage(id, multipartFile);

        //then
        assertNotNull(recipeCommand.getImage());
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
	}
	
	@Test(expected=NotFoundException.class)
	public void getRecipeByIDNotFoundTest() {
		//Given
		Optional<Recipe> recipe = Optional.empty();
		when(recipeRepository.findById(Mockito.anyLong())).thenReturn(recipe);
		
		//When
		recipeService.getRecipe(1L);
	}
}
