package com.ing.locator.atm;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Main bootstraping class for spring boot applications
 *
 * @author Atchuta Penmatsa
 */

@SpringBootApplication
@EnableAutoConfiguration(exclude = { JacksonAutoConfiguration.class })
public class AtmFinderApplication {
	
	 private static final String CAMEL_URL_MAPPING = "/atms/*";
	 private static final String CAMEL_SERVLET_NAME = "CamelServlet";
	
	//This enables WebMvc
    @Bean
    WebMvcConfigurerAdapter mvcViewConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/atm-finder").setViewName("atm-search");
                registry.addViewController("/login").setViewName("login");
             }
        };
    }
    
    // Registers Camel Servlet for REST DSL
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), CAMEL_URL_MAPPING);
        registration.setName(CAMEL_SERVLET_NAME);
        return registration;
    }

	public static void main(String[] args) {
		SpringApplication.run(AtmFinderApplication.class, args);
	}
	
  

   
}
