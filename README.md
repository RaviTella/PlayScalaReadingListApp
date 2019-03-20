![Reactive non-blocking Application with Play-Scala, Reactive Mongo-CosmosDB Mongo API](logo.png)

# Overview
A Sample application built with Play framework, integrated with Reactive Scala Driver for Mongo DB and Azure CosmosDB to demonstrate an end to end non-blocking application. The code base consists of two Scala projects a REST API called PlayScalaRecommendationService and a Web App which depends on the REST API called PlayScalaReadingListWebApp.

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
 * Create a Cosmos DB Mongo collection. use /reader as the "Shard Key"

## Then:
* Update the following properties in application.cong for PlayScalaReadingListWebApp 
  - mongoConfig.collection
  - mongodb.uri
* sbt "run 9001" - from PlayScalaRecommendationService the to start the REST API 
* sbt run - from PlayScalaReadingListWebApp to start the Web Application
* Access the WebApp at http://localhost:9000/

##### NOTE: You can also run the application against Mongo DB

# Highlights
In Play framework, action code must be non-blocking. So, the action must immediately return a promise of the result upon invocation. In Scala that would be an object of type Future.  The following action function demonstrates the use of for-comprehension to merge the results of two independent futures and then extract the resulting values in the view. The important point to note here is to create the futures prior to using them in the for-comprehension. This will allow for concurrent execution of the calls.
```
 def index() = Action.async { implicit request: Request[AnyContent] =>
    val f1 = getRecommendations()
    val f2 = getReadersBooks("tella")
    val futureResults = for {
      futureRecommendations <- f1
      futureBooks <- f2
    } yield (futureRecommendations, futureBooks)
    futureResults.map(results => Ok(views.html.readingList.render(results._1.toList, results._2))
    )
  }
```



