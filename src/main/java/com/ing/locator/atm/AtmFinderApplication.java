package com.ing.locator.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
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

	public static void main(String[] args) {
		SpringApplication.run(AtmFinderApplication.class, args);
	}
}
