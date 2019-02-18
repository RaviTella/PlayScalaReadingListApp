package controllers

import javax.inject._
import model.Recommendation
import play.api._
import play.api.mvc._
import play.api.libs.json._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  implicit val writes = Json.writes[Recommendation]
  def getAll() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(getRecommendations))
  }

  //build recommendations 
  def getRecommendations = {
    List(Recommendation("1", "01234", "Getting Started with kubernetes", "Jonathan Baier", "Learn Kubernetes the right way", "https://mtchouimages.blob.core.windows.net/books/Kubernetes.jpg"),
      Recommendation("2", "95201", "Learning Docker Networking", "Rajdeep Das", "Docker networking deep dive", "https://mtchouimages.blob.core.windows.net/books/DockerNetworking.jpg"),
      Recommendation("6", "95298", "Spring Microservices", "Rajesh RV", "Build scalable microservices with Spring and Docker", "https://mtchouimages.blob.core.windows.net/books/SpringMicroServices.jpg"),
      Recommendation("5", "01264", "Learning Concurrent Programming in Scala", "Aleksandar Prokopec", "Learn the art of building concurrent applications", "https://mtchouimages.blob.core.windows.net/books/Scala.jpg"),
      Recommendation("3", "23123", "Modern Authentication with AzureAD ", "Vittorio Bertocci", "Azure active directory capabilities", "https://mtchouimages.blob.core.windows.net/books/AzureAD.jpg"),
      Recommendation("4", "11201", "Microsoft Azure SQL", "Leonard G.Lobel", "Setp by step guide for developers", "https://mtchouimages.blob.core.windows.net/books/AzureSQL.jpg"),
      Recommendation("7", "28526", "Developing Azure and Web Services", "Rajdeep Das", "Exam Ref 70-487", "https://mtchouimages.blob.core.windows.net/books/AzureCert.jpg"),
      Recommendation("8", "95298", "Programming Microsoft Azure Service fabric", "Haishi Bai", "Service fabric for developers", "https://mtchouimages.blob.core.windows.net/books/ServiceFabric.jpg")
    )
  }
}
