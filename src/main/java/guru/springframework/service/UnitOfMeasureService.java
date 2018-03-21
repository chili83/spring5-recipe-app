package guru.springframework.service;

import java.util.Set;

import guru.springframework.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

	public UnitOfMeasureCommand getUom(Long id);
	public Set<UnitOfMeasureCommand> getUoms();

}
