package controllers

import javax.inject.Inject
import models.ReverseGeocode
import play.api.libs.json._
import play.api.mvc._
import services.TwitterClient
import scala.concurrent.ExecutionContext
import services.MapBox

class ApplicationController @Inject()(val twitterClient: TwitterClient, mapBox: MapBox)(implicit val executionContext: ExecutionContext) extends Controller {
  
  def index = Action { implicit request =>    
    Ok(views.html.index(mapBox))
  }

  def locations(lat: String, long: String) = Action.async {
    val locationWithTrends = twitterClient.closetLocations(ReverseGeocode(lat, long))
    locationWithTrends.map(locations => Ok(Json.toJson(locations)))
  }
  
  def trends(woeid: String) = Action.async {
    twitterClient.trendsFor(woeid) map { placeTrends => 
      Ok(Json.toJson(placeTrends))    
    }
  }
}
