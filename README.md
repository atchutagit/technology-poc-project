# ATM Finder 

A quick POC demonstrating Spring Boot, Spring Security, REST, Apache Camel, EHCache and Hystrix Circuit Breaker

Overview:
--------
	This application has been created using following technologies:
	1. Spring Boot
	2. Spring MVC/REST, Spring Security
	3. AngularJS for UI
	4. Camel to consume external REST API
	5. Camel-EHCache to cache data from external service
	6. Camel-Hystrix for implementing Circuit Breaker pattern
	7. JUnit for test cases
	
Features Implemented:
---------------------
	1. A REST API to fetch a list of ING ATMs in a given Dutch city, in pretty JSON format
	2. Same web page to display and filter all the ATM locations. This web page calls REST API mentioned above.
	3. Apache Camel is used to call external API (Via Camel Servlet)
	4. Spring Security is enabled for the Web Page   
	5. Test Cases for REST interface and Web Security
	6. EHCache is implemented to cache list of all ATM locations.
	7. Hystrix implemented for a sample scenario. (Fall back to cache if external service is slow to respond)

Development Environment
------------------------
	1. STS 3.9.2 IDE (Spring Tools IDE)
	2. TOMCAT 7.0.84
	3. JDK 1.8 
	4. Spring Boot - 1.5.9.Release
	5. Camel 2.20.1
	
How to build and deploy:
------------------------
	1. Import provided project into STS 
	2. Build and run as a Spring Boot Application inside IDE for testing
	3. Finally, run mvn install to get the WAR file for TOMCAT deployment. 
		 - WAR file will be generated in <project-root>/target directory
		 - Rename it to atm-finder.war before deploying it to TOMCAT
	Note: You can also deploy a pre-packaged war file (available under this repo) for quick testing
	
How to Access and Test:
----------------------
	1. Machine where the application is running should be connected to Internal for calling external API
	2. Access Web Page using:
		- http://localhost:8080/atm-finder (Replace PORT value if TOMCAT is not running on 8080)
		- login using user/password
		- Data shown on this page is served from cache after initial request. Cache expires after every 60 min.
	3. Access REST API 
		- http://localhost:8080/atms/locations/cities?city=Amsterdam (Replace PORT if TOMCAT is not running on 8080)
	4. Testing Circuit Breaker
		Step 1: Call Rest API (http://localhost:8080/atms/locations/cities?city=Amsterdam) and observe log. There will be a call to external REST API.
		Step 2: Disconnect network to simulate an issue with external REST API
		Step 3: Call the REST API again. This time response would be returned from cache. Circuit breaker will fall back to cache until the service dependency is resolved.
		Step 4: Connect to network again and call the REST API and observe logs to see if normal flow is established
	
