package guru.springframework.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import lombok.Synchronized;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {
        if (source == null) {
            return null;
        }

        IngredientCommand target = new IngredientCommand();
        target.setId(source.getId());
        if (source.getRecipe() != null) {
            target.setRecipeId(source.getRecipe().getId());
        }
        target.setAmount(source.getAmount());
        target.setDescription(source.getDescription());
        target.setUom(uomConverter.convert(source.getUom()));
        target.setUUID(source.getUUID());

        return target;
    }
}
