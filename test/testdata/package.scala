import models.Location
import models.ReverseGeocode
import play.api.libs.json._
import play.api.mvc.Results._
import play.api.routing.sird._
import scala.io.Source
import models.Trend

/**
 * @author Aditya Godara
 */
package object testdata {
  val latitude = "123"
  val longitude = "456"
  val reverseGeocode = ReverseGeocode(latitude, longitude)  
  val woeid = "2295401"
  
  val twitterClosetLocationsUrl = "/1.1/trends/closest.json"
  val closetLocationsResponse = readJson("/twitter/trend-locations.json")
  val closetLocationsHavingTrendsJson = Json.parse(closetLocationsResponse)
  val closetLocationsHavingTrends: List[Location] = closetLocationsHavingTrendsJson.validate[List[Location]].get
  
  val twitterTrendsUrl = "/1.1/trends/place"
  val aTrend = readJson("/twitter/a-trend.json")
  val aTrendModel = Trend("#GanaPuntosSi", "http://twitter.com/search/?q=%23GanaPuntosSi")
  val trendsResponse = readJson("/twitter/trends.json")
  val trendsList: List[Trend] = (Json.parse(trendsResponse) \\ "trends")(0).validate[List[Trend]].get
  
  val host = "localhost"
  
  val twitterConsumerKey = "xvz1evFS4wEEPTGEFPHBog"
  val twitterConsumerSecret = "L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg"
  val bearerTokenUrl = "/oauth2/token"
  val bearerToken = "AAAAAAAAAAAA"
  val bearerTokenResponse = s"""{"token_type":"bearer", "access_token":"$bearerToken"}"""
  val base64EncodedBearerTokenCredentials = "eHZ6MWV2RlM0d0VFUFRHRUZQSEJvZzpMOHFxOVBaeVJnNmllS0dFS2hab2xHQzB2SldMdzhpRUo4OERSZHlPZw=="
  val basicAuthorizationHeader = s"Basic $base64EncodedBearerTokenCredentials"
  val formUrlEncodedHeader = "application/x-www-form-urlencoded;charset=UTF-8"
  val queryString = Seq("lat" -> latitude, "long" -> longitude, "granularity" -> "city")    

  private def readJson(filePath: String): String = {
    val resourceURL = getClass.getResource(filePath)
    Source.fromURL(resourceURL).mkString
  }
}
