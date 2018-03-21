package guru.springframework.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	UnitOfMeasureRepository uomRepository;
	UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
	
	@Autowired
	public UnitOfMeasureServiceImpl(UnitOfMeasureRepository uomRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		this.uomRepository = uomRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}
	
	@Override
	public UnitOfMeasureCommand getUom(Long id) {
		
		Optional<UnitOfMeasure> uom = uomRepository.findById(id);
		if (!uom.isPresent()) {
			throw new EntityNotFoundException();
		}
		return unitOfMeasureToUnitOfMeasureCommand.convert(uom.get());
		
	}

	@Override
	public Set<UnitOfMeasureCommand> getUoms() {
		Set<UnitOfMeasureCommand> uomsCmd = new HashSet<>();
		Iterator<UnitOfMeasure> uoms = uomRepository.findAll().iterator();
		while (uoms.hasNext()) {
			uomsCmd.add(unitOfMeasureToUnitOfMeasureCommand.convert(uoms.next()));
		}
		return uomsCmd;
	}


}
