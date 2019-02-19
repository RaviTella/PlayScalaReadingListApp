# (logo.png)

### A Sample application built with Play framework, integrated with Reactive Scala Driver for Mongo DB and Azure CosmosDB to demonstrate an end to end non-blocking application. The code base consists of two Scala projects a REST API called PlayScalaRecommendationService and a Web App which depends on the REST API called PlayScalaReadingListWebApp.

Specifically, the following capabilities are demonstrated:
* Scala Play 2.7 - Routing, Twirl templates, forms, Dependency Injection etc
* Akka HTTP server backend
* Calling REST APIs with Play WS
* ReactiveMongo plugin for Play Scala
* Using Cosmos DB Mongo API from Reactive Scala Driver 

# Getting started

## First:
 * Java 8
 * sbt
 * Create a Cosmos DB Mongo collection

## Then:
* Update the following properties in application.cong for PlayScalaReadingListWebApp 
  - mongoConfig.collection
  - mongodb.uri
* sbt "run 9001" - from PlayScalaRecommendationService the to start the REST API 
* sbt run - from PlayScalaReadingListWebApp to start the Web Application
* login at http://localhost:9000/

##### NOTE: You can also run the application against Mongo DB


