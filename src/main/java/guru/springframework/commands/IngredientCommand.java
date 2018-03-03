package guru.springframework.commands;

import java.math.BigDecimal;

import guru.springframework.domain.DomainObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IngredientCommand extends DomainObject{
	private Long id;
	private Long recipeId;
	private String description;
	private BigDecimal amount;
	private UnitOfMeasureCommand uom;

}
