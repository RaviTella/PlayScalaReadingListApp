package controllers

import java.util.UUID

import play.api.data._
import play.api.data.Forms._

case class BookForm(_id: String, reader: String, isbn: String, title: String, author: String, description: String)

object BookForm {
  val form: Form[BookForm] = Form(
    mapping(
      "_id" -> nonEmptyText,
      "reader" -> nonEmptyText,
      "isbn" -> nonEmptyText,
      "title" -> nonEmptyText,
      "author" -> nonEmptyText,
      "description" -> nonEmptyText
       )(BookForm.apply)(BookForm.unapply)
  )
}
