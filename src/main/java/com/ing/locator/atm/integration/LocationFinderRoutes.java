package com.ing.locator.atm.integration;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class LocationFinderRoutes extends RouteBuilder {
	
	 @Override
	 public void configure() throws Exception {
		 
		 from("servlet:///locations") 
		    .setHeader("Content-Type", constant("application/json"))
	        .setHeader("Accept", constant("application/json"))
	        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("https4:www.ing.nl/api/locator/atms/?bridgeEndpoint=true")
            .convertBodyTo(String.class)
            .to("bean:transformToJson?method=transform(${body})")
            .end();
            
		 from("servlet:///locations/cities") 
		    .setHeader("Content-Type", constant("application/json"))
	        .setHeader("Accept", constant("application/json"))
	        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
	        .recipientList(simple("https4:www.ing.nl/api/locator/atms/locatedin/${header.city}?bridgeEndpoint=true"))
	        .convertBodyTo(String.class)
	        .to("bean:transformToJson?method=transform(${body})")
	        .end();	

	 }
   
}

