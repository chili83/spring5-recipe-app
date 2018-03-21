package guru.springframework.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;

public class UnitOfMeasureServiceImplTest {
	
	@Mock
	UnitOfMeasureRepository uomRepository;
	
	@Mock
	UnitOfMeasureToUnitOfMeasureCommand toUomCommandConverter;
	
	UnitOfMeasureService uomService;

	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		this.uomService = new UnitOfMeasureServiceImpl(uomRepository, toUomCommandConverter);
		
	}
	
	@Test
	public void getUom() {
		//Given
		UnitOfMeasure uom = new UnitOfMeasure();
		uom.setId(291L);
		uom.setDescription("TestEinheit");
		
		UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
		uomCommand.setId(uom.getId());
		uomCommand.setDescription(uom.getDescription());
		
		when(uomRepository.findById(Mockito.eq(uom.getId()))).thenReturn(Optional.of(uom));
		when(toUomCommandConverter.convert(Mockito.eq(uom))).thenReturn(uomCommand);
		
		//When
		UnitOfMeasureCommand uomResult = uomService.getUom(uom.getId());
		
		//Then
		assertEquals(uomResult.getId(), uomCommand.getId());
		
	}
	
	@Test
	public void getUoms() {
		//Given
		Set<UnitOfMeasure> uoms = new HashSet<>();
		UnitOfMeasure uom1 = new UnitOfMeasure();
		uom1.setId(291L);
		uom1.setDescription("TestEinheit 1");
		uoms.add(uom1);
		
		UnitOfMeasureCommand cmd1 = new UnitOfMeasureCommand();
		cmd1.setId(uom1.getId());
		cmd1.setDescription(uom1.getDescription());
		cmd1.setUUID(uom1.getUUID());
		
		when(uomRepository.findAll()).thenReturn(uoms);
		when(toUomCommandConverter.convert(Mockito.eq(uom1))).thenReturn(cmd1);
		
		//When
		Set<UnitOfMeasureCommand> uomsResult = uomService.getUoms();
		
		//Then
		assertEquals(uoms.size(), uomsResult.size());
	}
	
}
