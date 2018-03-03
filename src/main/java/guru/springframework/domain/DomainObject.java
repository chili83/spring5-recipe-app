package guru.springframework.domain;

import javax.persistence.MappedSuperclass;

import guru.springframework.util.UUIDGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@MappedSuperclass
public class DomainObject {

	private String UUID;
	
	public DomainObject () {
		this.UUID = UUIDGenerator.getUUID();
	}

}
