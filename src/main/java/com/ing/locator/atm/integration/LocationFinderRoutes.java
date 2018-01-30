package com.ing.locator.atm.integration;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LocationFinderRoutes extends RouteBuilder {
	
 @Override
 public void configure() throws Exception {
  from("direct:atmRoute")
  .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
  .to("https4:www.ing.nl/api/locator/atms/locatedin/Lelystad").log("$body");
 }
}