package guru.springframework.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.RecipeService;

public class RecipeImageControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	Model model;

	RecipeImageController recipeImageController;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		recipeImageController = new RecipeImageController(recipeService);

	}

	// Read

	// Edit
	@Test
	public void editTest() throws Exception {

		// Given
		RecipeCommand recipe = new RecipeCommand();
		recipe.setId(1L);
		recipe.setDescription("Tiramisu");

		String content = "test image";

		Byte[] testImage = new Byte[content.getBytes().length];
		int i = 0;
		for (byte b : content.getBytes()) {
			testImage[i++] = b;
		}
		recipe.setImage(testImage);

		when(recipeService.getRecipe(Mockito.eq(recipe.getId()))).thenReturn(recipe);

		// When
		MockMvcBuilders.standaloneSetup(recipeImageController).build()
				.perform(MockMvcRequestBuilders.get("/recipes/1/image/edit"))
				.andExpect(MockMvcResultMatchers.view().name("recipes/imageform"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));

	}

	// Update
	@Test
	public void updateTest() throws Exception {
		// Given
		Long recipeID = 1L;
		MockMultipartFile file = new MockMultipartFile("imagefile", "Test".getBytes());

		// When
		MockMvcBuilders.standaloneSetup(recipeImageController).build()
				.perform(MockMvcRequestBuilders.multipart(String.format("/recipes/%s/image", recipeID)).file(file))
				.andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/recipes/%s", recipeID)));

		// Then
		verify(recipeService, times(1)).addRecipeImage(Mockito.eq(recipeID), Mockito.eq(file));
		verify(recipeService, times(1)).addRecipeImage(Mockito.eq(recipeID), Mockito.eq(file));
	}

	// Delete

}
