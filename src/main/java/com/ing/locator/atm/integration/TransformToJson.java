package com.ing.locator.atm.integration;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TransformToJson {
	public String transform(String body) {
		String locationsString = body.substring(body.indexOf("["));
		try {
			// Pretty JSON
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(locationsString, Object.class);
			locationsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		}catch(Exception e) {
			// Ignore parsing errors and return Unformatted JSON
		}
		return locationsString;
	}
}
