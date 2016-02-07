package models

import play.api.libs.json._

case class Trend(name: String, url: String)

object Trend {
  implicit val trendFormats: Format[Trend] = Json.format[Trend]
}