package guru.springframework.commands;

import java.util.Set;

import guru.springframework.domain.Difficulty;
import guru.springframework.domain.DomainObject;
import lombok.Data;
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
