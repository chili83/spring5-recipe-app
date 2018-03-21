package guru.springframework.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnitOfMeasureCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUnitOfMeasureEqualsAndHashCode() {
		UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
		uom.setId(1L);
		uom.setDescription("Test");
		
		UnitOfMeasureCommand uom2 = new UnitOfMeasureCommand();
		uom2.setId(1L);
		uom2.setDescription("Test");
		uom2.setUUID(uom.getUUID());
		
		int hash = uom.hashCode();
		int hash2 = uom2.hashCode();
		
		log.debug(String.valueOf(hash));
		log.debug(String.valueOf(hash2));
		
		assertEquals(hash, hash2);
		assertEquals(uom, uom2);
	}

}
