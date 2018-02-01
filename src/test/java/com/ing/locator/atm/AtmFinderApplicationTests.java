package com.ing.locator.atm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ing.locator.atm.domain.Location;

/*
 * Test class to test Servlet Endpoints exposed by Camel
 * Starts a web container on a random port during these tests
 * 
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AtmFinderApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetAllAtmsLocationsSuccess() throws Exception {

		ResponseEntity<Collection<Location>> rateResponse = restTemplate.exchange(
				"http://localhost:" + port + "/atms/locations", HttpMethod.GET, null,
				new ParameterizedTypeReference<Collection<Location>>() {
				});
		Collection<Location> locations = rateResponse.getBody();
		assertThat(locations.size() > 0);
	}

	@Test
	public void testGetAtmLocationsByCitySuccess() throws Exception {
		final String validCity = "Amsterdam";
		ResponseEntity<Collection<Location>> rateResponse = restTemplate.exchange(
				"http://localhost:" + port + "/atms/locations/cities/?city=" + validCity + "", HttpMethod.GET, null,
				new ParameterizedTypeReference<Collection<Location>>() {
				});
		List<Location> locations = (List<Location>) rateResponse.getBody();
		String cityName = locations.get(1).getAddress().getCity();
		assertThat(validCity.equals(cityName));
	}

	@Test
	public void testGetAtmLocationsByCityFailed() throws Exception {
		final String invalidCity = "Atlanta";
		ResponseEntity<Collection<Location>> rateResponse = restTemplate.exchange(
				"http://localhost:" + port + "/atms/locations/cities/?city=" + invalidCity + "", HttpMethod.GET, null,
				new ParameterizedTypeReference<Collection<Location>>() {
				});

		Collection<Location> locations = rateResponse.getBody();
		assertThat(locations.size() == 0);
	}

}