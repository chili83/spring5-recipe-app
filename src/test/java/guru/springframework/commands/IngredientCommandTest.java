package guru.springframework.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IngredientCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRemoveEquals() {
		Set<IngredientCommand> testSet = new HashSet<IngredientCommand>();
		
		IngredientCommand i1 = new IngredientCommand();
		i1.setId(1L);
		i1.setDescription("Test");
		testSet.add(i1);
		
		IngredientCommand i2 = new IngredientCommand();
		i2.setId(1L);
		i2.setDescription("Test");
		testSet.add(i2);

		assertTrue(testSet.remove(i2));
		assertEquals(i1, i2);
	}
	
	@Test
	public void testEqualsAdvanced() {
		IngredientCommand i1 = new IngredientCommand();
		i1.setId(1L);
		i1.setDescription("Test");
		
		UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
		uom.setId(1L);
		uom.setDescription("Test");
		i1.setUom(uom);
		
		IngredientCommand i2 = new IngredientCommand();
		i2.setId(1L);
		i2.setDescription("Test");
		
		UnitOfMeasureCommand uom2 = new UnitOfMeasureCommand();
		uom2.setId(1L);
		uom2.setDescription("Test");
		uom2.setUUID(uom.getUUID());
		i2.setUom(uom2);
		
		int uomHash = uom.hashCode();
		int uomHash2 = uom2.hashCode();
		
		log.debug(String.valueOf(uomHash));
		log.debug(String.valueOf(uomHash2));
		
		assertEquals(uom, uom2);
		assertEquals(uomHash, uomHash2);
		assertEquals(i1, i2);
	}

}
