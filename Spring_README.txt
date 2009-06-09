Running Jhove2 with Spring configurations without Maven
=======================================================
In Project Properties -> 
  Java Build Path -> Source Tab
  Add jhove2/src/main/resources
  
  Java Build Path
  set Default Output Folder = jhove2/target/classes
 
  Java Build Path -> Libraries Tab
  Add  
   	org\springframework\spring-test\2.5.3\spring-test-2.5.3.jar
	org\springframework\spring-core\2.5.3\spring-core-2.5.3.jar
	org\springframework\spring-context\2.5.3\spring-context-2.5.3.jar
	org\springframework\spring-beans\2.5.3\spring-beans-2.5.3.jar
	commons-logging\commons-logging\1.1.1\commons-logging-1.1.1.jar
	commons-logging\commons-logging-api\1.1\commons-logging-api-1.1.jar
	log4j\log4j\1.2.14\log4j-1.2.14.jar