package controllers

import java.util.UUID

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import javax.inject._
import play.api.Configuration
import model.{Book, BookRepository, Recommendation}
import play.api._
import play.api.mvc._
import play.api.libs.ws._
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import reactivemongo.api.ReadPreference
import play.api.data._
import play.api.data.Forms._


// Reactive Mongo imports
import reactivemongo.api.Cursor

import play.modules.reactivemongo.{ // ReactiveMongo Play2 plugin
  MongoController,
  ReactiveMongoApi,
  ReactiveMongoComponents
}

// BSON-JSON conversions/collection
import reactivemongo.play.json._, collection._

@Singleton
class HomeController @Inject()(configuration: Configuration, ws: WSClient, components: ControllerComponents,
                               val reactiveMongoApi: ReactiveMongoApi
                              ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  /*
 * Get a JSONCollection (a Collection implementation that is designed to work
 * with JsObject, Reads and Writes.)
 */
  def collection: Future[JSONCollection] = database.map(
    _.collection[JSONCollection](configuration.get[String]("mongoConfig.collection")))

  import model._
  import model.JsonFormats._


  def index() = Action.async { implicit request: Request[AnyContent] =>
    val futureResults = for {
      futureRecommendations <- getRecommendations()
      futureBooks <- getReadersBooks("tella")
    } yield (futureRecommendations, futureBooks)
    futureResults.map(results => Ok(views.html.readingList.render(results._1.toList, results._2))
    )
  }


  def get(id: java.util.UUID) = Action.async { implicit request: Request[AnyContent] =>
    collection.flatMap(_.find(Json.obj("_id" -> id), Option.empty[JsObject])
        // perform the query and get a cursor of JsObject
        .cursor[Book](ReadPreference.primary)
        // Collect the results as a list
        .collect[List](Int.MaxValue, Cursor.FailOnError[List[Book]]())).map { books =>
      Ok(views.html.editReadingList.render(books.head))
    }
  }


  def delete(id: java.util.UUID) = Action.async { implicit request: Request[AnyContent] =>
    collection.flatMap(_.findAndRemove(Json.obj("_id" -> id), Option.empty[JsObject]).map(_ => Redirect(routes.HomeController.index())))
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    val recommendationForm: RecommendationForm = RecommendationForm.form.bindFromRequest.get
    val book = Book(reader = "tella", isbn = recommendationForm.isbn, title = recommendationForm.title, author = recommendationForm.author, description = recommendationForm.description)
    collection.flatMap(_.insert.one(book)).map(_ => Redirect(routes.HomeController.index())
    )
  }


  def update() = Action.async { implicit request: Request[AnyContent] => {
    val recommendationSelectionFormData: BookForm = BookForm.form.bindFromRequest.get
    val book = Book(UUID.fromString(recommendationSelectionFormData._id), recommendationSelectionFormData.reader, recommendationSelectionFormData.isbn, recommendationSelectionFormData.title, recommendationSelectionFormData.author, recommendationSelectionFormData.description)
    val selector = Json.obj("_id" -> recommendationSelectionFormData._id)
    collection.flatMap(c => c.update.one(selector, book)).map(_ => Redirect(routes.HomeController.index()))
  }
  }


  def cancel() = Action { implicit request: Request[AnyContent] => {
    Redirect(routes.HomeController.index())
  }
  }


  def convertToObject(jsonString: String) = {
    val objectMapper = new ObjectMapper with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.readValue(jsonString, classOf[Array[Recommendation]])
  }

  def getRecommendations() = {
    ws.url(configuration.get[String]("externalRestServices.recommendationService")).get().map { response =>
      (convertToObject(response.json.toString()))
    }
  }

  def getReadersBooks(reader: String) = {
    collection.flatMap(c => // find all
      c.find(Json.obj("reader" -> "tella"), Option.empty[JsObject])
        // perform the query and get a cursor of JsObject
        .cursor[Book](ReadPreference.primary)
        // Collect the results as a list
        .collect[List](Int.MaxValue, Cursor.FailOnError[List[Book]]())
    )
  }
}
