package guru.springframework.commands;

import guru.springframework.domain.DomainObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UnitOfMeasureCommand extends DomainObject{
	private Long id;
	private String description;
}
