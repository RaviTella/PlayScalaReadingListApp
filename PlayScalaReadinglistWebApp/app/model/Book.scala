package model
import reactivemongo.bson.Macros.Annotations.Key
import java.util.UUID
import reactivemongo.bson.BSONObjectID


case class Book(_id: UUID=UUID.randomUUID(), reader: String, isbn: String, title: String, author: String, description: String)

object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val bookFormat = Json.format[Book]

}
