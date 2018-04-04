package guru.springframework.commands;

import guru.springframework.domain.DomainObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class CategoryCommand extends DomainObject{
	private Long id;
	private String description;
}
