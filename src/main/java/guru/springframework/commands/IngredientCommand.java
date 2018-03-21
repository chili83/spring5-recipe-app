package guru.springframework.commands;

import java.math.BigDecimal;

import guru.springframework.domain.DomainObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
public class IngredientCommand extends DomainObject{
	private Long id;
	private Long recipeId;
	private String description;
	private BigDecimal amount;
	private UnitOfMeasureCommand uom;

}
