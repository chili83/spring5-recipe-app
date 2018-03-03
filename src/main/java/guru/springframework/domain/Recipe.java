package guru.springframework.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Recipe extends DomainObject{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	
	@Lob
	private String directions;
	
	@ManyToMany
	@JoinTable(name="recipe_category", joinColumns=@JoinColumn(name="recipe_id"), inverseJoinColumns=@JoinColumn(name="category_id"))
	private Set<Category> categories = new HashSet<>();
	
	@Enumerated(value=EnumType.STRING)
	private Difficulty difficulty;
	
	@Lob
	private Byte[] image;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	private Notes notes;
	
	@OneToMany(cascade= {CascadeType.ALL}, mappedBy="recipe", orphanRemoval=true)
	private Set<Ingredient> ingredients = new HashSet<>();
	
	public void setNotes(Notes notes) {
        if (notes != null) {
        	this.notes = notes;
        	notes.setRecipe(this);
        }
	}

	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients.clear();
		Iterator<Ingredient> it = ingredients.iterator();
		while(it.hasNext()) {
			addIngredient(it.next());
		}
	}
	
	public void addIngredient(Ingredient ingredient) {
		if (ingredient != null) ingredient.setRecipe(this);
		this.getIngredients().add(ingredient);
	}
	

}
