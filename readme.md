# Billing engine to generate bills #

This is a **Billing Engine** written in **Java 8** and using the **Spring-Boot** framework to develop the **REST API** that allows any service or client to connect to the engine and get bills information.

The build tool used is **Gradle**. In the **build.gradle** file, there are all the dependencies needed for the project.

**Mockito** has been used to help with the unit testing.

# Introduction #

The service REST API allows: bill creation.

For the simplicity of the solution, there is no database involved:
- the patient and medical service records are initialised in the repositories so that when the billing engine is being run, there is some data to possibly create bills for (also, in a production scenario, the billing engine should not manage patients or medical service records itself)
- the created data (bills) will not persist beyond the lifespan of the server

# Prerequisites #

Have your prefferred IDE installed.
Have JDK installed.

# Functionality #

Using basic **curl** syntax or a tool like **Postman** for example, the service supports following requests:

**POST** request to create a bill:

http://{hostname}:{port}/billing-engine/billing?patientId={patientId}

#### Request Body example
	{
		"DIAGNOSIS":2,
		"XRAY":1,
		"VACCINE":2
	}

The body represents a list of the medical services and corresponding amounts provided to the patient with {patientId}.

#### Response Body example
	{
		"id": "2b69334a-1608-4fd7-b0f5-165460d56c8f",
		"patientId": 1,
		"amount": 270
	}



## Running the service

Make sure you have an IDE installed.
Make sure you have JDK installed.
Clone this project.

You can import it into your IDE and then get the Gradle dependecies and then run the project. Spring Boot will start up Tomcat and will listen by default on the 8080 port. The resources folder contains an **application.properties** file that stores the default port, context path and application name.

The project can also be built as a jar file and then started from the command line using the following command:

	java -jar billing-engine-1.0.0.jar


## Tests

The tests can be run from the IDE or from the command line using the following command:

	gradle test
