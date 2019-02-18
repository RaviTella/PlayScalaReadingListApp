package controllers
import java.util.UUID

import play.api.data._
import play.api.data.Forms._

case class RecommendationForm(isbn: String, title: String, author: String, description: String)

object RecommendationForm {
  val form: Form[RecommendationForm] = Form(
    mapping(
      "isbn" -> nonEmptyText,
      "title" -> nonEmptyText,
      "author" -> nonEmptyText,
      "description" -> nonEmptyText
    )(RecommendationForm.apply)(RecommendationForm.unapply)
  )
}
