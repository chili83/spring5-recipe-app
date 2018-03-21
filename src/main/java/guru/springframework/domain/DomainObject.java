package guru.springframework.domain;

import javax.persistence.MappedSuperclass;

import guru.springframework.util.UUIDGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(exclude= {"UUID"})
public class DomainObject {

	private String UUID;
	
	public DomainObject () {
		this.UUID = UUIDGenerator.getUUID();
	}

}
