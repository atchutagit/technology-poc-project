package com.ing.locator.atm.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ing.locator.atm.domain.Location;

/**
 * MVC & REST Controller for '/atms' and '/atms/locatedin/{city}' endpoints.
 *
 * @author Atchuta Penmatsa
 */

@RestController
@CrossOrigin
public class LocationController {

	private final ProducerTemplate producerTemplate;
	private static final String endpointBaseURI = "https4:www.ing.nl/api/locator/atms/";
	
	@Autowired
	public LocationController(ProducerTemplate producerTemplate) {
					this.producerTemplate = producerTemplate;
	}
    

	/**
	 * Get all ATM locations to be displayed on the web page
	 * @return JSON array of locations
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/atms/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Location> getAllLocations() throws IOException {
	    String atmAllLocationsJson = callRoute(endpointBaseURI);
		Type targetClassType = new TypeToken<ArrayList<Location>>() { }.getType();
    		Collection<Location> targetCollection = new Gson().fromJson(atmAllLocationsJson.substring(atmAllLocationsJson.indexOf("[")), targetClassType);
    		return targetCollection;
	}
	

	/**
	 * Get all ATM locations for the given city
	 * @param searchpath for filtering addresses 
	 * @param cityname to search for
	 * @return JSON Array all cities matching the given cityname
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/atms/{searchPath}/{cityName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Location> getLocationsByCity(@PathVariable("searchPath") String searchpath, @PathVariable("cityName") String cityname) throws IOException {
	  String atmLocationsJson = callRoute(endpointBaseURI+searchpath+"/"+cityname);
	  Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	  Type targetClassType = new TypeToken<ArrayList<Location>>(){ }.getType();
    	  Collection<Location> targetCollection = gson.fromJson(atmLocationsJson.substring(atmLocationsJson.indexOf("[")), targetClassType);
    	  return targetCollection;
	}
	
	private String callRoute(String endPointURI) {
		return producerTemplate.send(endpointBaseURI, new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
            }
	    }).getOut().getBody(String.class);
	}
	
}