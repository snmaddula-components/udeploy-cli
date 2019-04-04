# UDeploy CLI App

The automation agent is written in Java & Spring Boot that allows Udeploy authenticated users to perform the following actions:
-	Create new applications and application processes
-	Create new environments for applications and map existing agents to them, with the additional option to label the agents with data center tags
-	Create new components and component processes
-	Create new resources in the resource tree
-	Add/Edit properties on applications, environments, and components
-	Add existing teams to applications and their child resources 

### Tool Stack
-	Java 8
-	Spring Boot 2+
-	Apache POI 4+
-	Maven 3+

### Gist of the implementation
-	The program will take an excel workbook and few properties (credentials, environment, etc.) as the input feed.
-	Using Apache POI to parse the provided Excel feed and constructing a JSON like structure of the resources graph.
-	All the application related REST endpoints are maintained in the Manifest.
-	Leveraged Spring Framework provided RestTemplate to invoke REST endpoints on udeploy instance.
-	Implemented logging to report the progress.
-	A simple cli command to execute the application.
-	Easy to transform into a Web application.
