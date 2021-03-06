package guru.springframework.commands;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import guru.springframework.domain.Difficulty;
import guru.springframework.domain.DomainObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class RecipeCommand extends DomainObject{
	private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(100)
    private Integer servings;
    private String source;

    @URL
    private String url;

    @NotBlank
    private String directions;

    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Byte[] image;
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
    
    public void addIngredient(IngredientCommand ingredient) {
    	this.ingredients.add(ingredient);
    }

    public String getImageBase64Encoded() {
    	if (image != null) {
    		byte[] bytes = new byte[image.length];
    		int i = 0;
    		for (byte b : image) {
    			bytes[i++] = b;
    		}
    		return new String(Base64.getEncoder().encodeToString(bytes));
    	}
    	return "";
    }

}
