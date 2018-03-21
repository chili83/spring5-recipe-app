package guru.springframework.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IngredientTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEquals() {
		Ingredient i1 = new Ingredient();
		i1.setId(1L);
		i1.setDescription("Test");
		
		Ingredient i2 = new Ingredient();
		i2.setId(1L);
		i2.setDescription("Test");
		
		assertEquals(i1, i2);
	}

}
