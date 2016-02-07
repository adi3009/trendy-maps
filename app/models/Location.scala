package models

import play.api.libs.json._

/**
 * @author Aditya Godara
 */
case class Location(name: String, woeid: Long)

object Location {
  implicit val jsonFormats: Format[Location] = Json.format[Location]
}
