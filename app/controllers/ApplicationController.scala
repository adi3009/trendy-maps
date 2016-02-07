package controllers

import javax.inject.Inject

import models.ReverseGeocode
import play.api.libs.json._
import play.api.mvc._
import services.TwitterClient

import scala.concurrent.ExecutionContext

class ApplicationController @Inject()(val twitterClient: TwitterClient)(implicit val executionContext: ExecutionContext) extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def locations(lat: String, long: String) = Action.async {
    val locationWithTrends = twitterClient.closetLocations(ReverseGeocode(lat, long))
    locationWithTrends.map(locations => Ok(Json.toJson(locations)))
  }
}
