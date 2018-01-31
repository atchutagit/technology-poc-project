# ATM Finder


Overview:
--------
	This application has been created using following technologies:
	1. Spring Boot
	2. Spring MVC/REST, Spring Security
	3. AngularJS for UI
	4. Camel to consume external REST API
	
Features Implemented:
---------------------
	1. A web application that provides REST api to fetch a list of ING ATMs in a given Dutch city
	2. Same web application provides a page to fetch and filter all the ATM locations
	3. Apache Camel is used to call external API (Via Camel Servlet)
	4. Spring Security is enabled for the Web Page   

Features not implemented:
------------------------
	1. Unit tests

Development Environment
------------------------
	1. STS 3.9.2 IDE (Spring Tools IDE)
	2. TOMCAT 7.0.84
	3. JDK 1.8
	
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
	1. Machine where the application is running should be connected to Internal to call external API
	2. Access Web Page using:
		- http://localhost:8080/atm-finder (Replace PORT value if TOMCAT is not running on 8080)
		- login using user/password
	3. Access REST API 
		- http://localhost:8080/atms/locations/cities?city=Amsterdam (Replace PORT if TOMCAT is not running on 8080)
	
