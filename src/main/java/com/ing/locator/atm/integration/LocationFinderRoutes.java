package com.ing.locator.atm.integration;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ehcache.EhcacheConstants;
import org.springframework.stereotype.Component;

@Component
public class LocationFinderRoutes extends RouteBuilder {
	
	 private static final String CITY = "city";
	 private static final String ALL_ATM_LOCATIONS = "atmlocations";
	 private static final String CONTENT_TYPE_HEADER = "Content-Type";
	 private static final String ACCEPT_HEADER = "Accept";
	 private static final String CONTENT_TYPE_JSON = "application/json";
	

	
	 @Override
	 public void configure() throws Exception {
		 
		 // Route to get all ATM Locations
		 // Check and serve from Cache first. If not found, call External service and also cache the response in EHCache for future requests.
		 from("servlet:///locations") 
		    .setProperty(ALL_ATM_LOCATIONS, constant(ALL_ATM_LOCATIONS))
		    .setHeader(CONTENT_TYPE_HEADER, constant(CONTENT_TYPE_JSON))
	        .setHeader(ACCEPT_HEADER, constant(CONTENT_TYPE_JSON))
		 	.setHeader(EhcacheConstants.ACTION, constant(EhcacheConstants.ACTION_GET))
			.setHeader(EhcacheConstants.KEY, exchangeProperty(ALL_ATM_LOCATIONS))
			.to("ehcache://atmsLocationsCache?configUri=classpath:ehcache.xml&keyType=java.lang.String&valueType=java.util.List")
			.choice()
			.when(header(EhcacheConstants.ACTION_HAS_RESULT))
				.log("Serving from Cache....")
		        .to("bean:transformToJson?method=transform(${body})")
			.otherwise()
			    .log("Calling ING service....")
			    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
		        .recipientList(simple("https4:www.ing.nl/api/locator/atms/?bridgeEndpoint=true"))
		        .convertBodyTo(String.class)
		        .to("bean:transformToJson?method=transform(${body})")
		        .to("direct:saveAllLocationsInCache")	    
		    .endChoice()
			.end(); 
         
		 
		 // Circuit Breaker - Hystrix Implementation
		 // Normal Scenario --> Call external REST service and get the locations, save it in cache
		 // Failure Scenario --> Fall back to cached data, if the External service is slow to respond (takes more than 5 sec)
		 from("servlet:///locations/cities") 
			 .hystrix()
				 .hystrixConfiguration()
	             	.executionTimeoutInMilliseconds(5000)
	             	.circuitBreakerSleepWindowInMilliseconds(10000)
	             .end()
	         	 .setHeader(CONTENT_TYPE_HEADER, constant(CONTENT_TYPE_JSON))
			 	 .setHeader(ACCEPT_HEADER, constant(CONTENT_TYPE_JSON))
			 	 .setHeader(Exchange.HTTP_METHOD, constant("GET"))
		         .toD("https4:www.ing.nl/api/locator/atms/locatedin/${header.city}?bridgeEndpoint=true")
		         .convertBodyTo(String.class)
		         .to("bean:transformToJson?method=transform(${body})")
		         .to("direct:saveLocationsInCache")
		     .onFallback()
	        		.log("Issue calling external service. Falling back to Cached data ....")
	       		.setProperty(CITY, header(CITY))
	       		.setHeader(CONTENT_TYPE_HEADER, constant(CONTENT_TYPE_JSON))
	       		.setHeader(ACCEPT_HEADER, constant(CONTENT_TYPE_JSON))
	       		.setHeader(EhcacheConstants.ACTION, constant(EhcacheConstants.ACTION_GET))
	       		.setHeader(EhcacheConstants.KEY, exchangeProperty(CITY))
	       		.to("ehcache://atmsLocationsCache?configUri=classpath:ehcache.xml&keyType=java.lang.String&valueType=java.util.List")
	       		.to("bean:transformToJson?method=transform(${body})") 
	       	    .end()
			.end();
		 
		 // Save ATM Locations into Cache. City Name is the key
		 from("direct:saveLocationsInCache").routeId("save-locations-in-cache")
		 	.setProperty(CITY, header(CITY))
			.setHeader(EhcacheConstants.ACTION, constant(EhcacheConstants.ACTION_PUT))
			.setHeader(EhcacheConstants.KEY, exchangeProperty(CITY))
			.to("ehcache://atmsLocationsCache?configUri=classpath:ehcache.xml&keyType=java.lang.String&valueType=java.util.List")
			.end();
	
		 // Save All ATM Locations into Cache. 
		 from("direct:saveAllLocationsInCache").routeId("save-all-locations-in-cache")
		 	.setProperty(ALL_ATM_LOCATIONS, header(ALL_ATM_LOCATIONS))
			.setHeader(EhcacheConstants.ACTION, constant(EhcacheConstants.ACTION_PUT))
			.setHeader(EhcacheConstants.KEY, exchangeProperty(ALL_ATM_LOCATIONS))
			.to("ehcache://atmsLocationsCache?configUri=classpath:ehcache.xml&keyType=java.lang.String&valueType=java.util.List")
			.end();

	}
   
}

