package guru.springframework.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator {

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
}
