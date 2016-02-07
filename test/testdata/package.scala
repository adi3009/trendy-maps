import models.Location
import models.ReverseGeocode
import play.api.libs.json._
import play.api.mvc.Results._
import play.api.routing.sird._

/**
 * @author Aditya Godara
 */
package object testdata {
  val latitude = "123"
  val longitude = "456"
  val reverseGeocode = ReverseGeocode(latitude, longitude)  
  val closetLocationsResponse = """[
                                                  {
                                                    "name": "Jaipur",
                                                    "placeType": {
                                                      "code": 7,
                                                      "name": "Town"
                                                    },
                                                    "url": "http://where.yahooapis.com/v1/place/2295401",
                                                    "parentid": 23424848,
                                                    "country": "India",
                                                    "woeid": 2295401,
                                                    "countryCode": "IN"
                                                  }
                                                ]"""
  val closetLocationsHavingTrendsJson = Json.parse(closetLocationsResponse)

  val closetLocationsHavingTrends: List[Location] = closetLocationsHavingTrendsJson.validate[List[Location]].get
  
  val host = "localhost"

  val twitterClosetLocationsUrl = "/1.1/trends/closest.json"
  val twitterConsumerKey = "xvz1evFS4wEEPTGEFPHBog"
  val twitterConsumerSecret = "L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg"
  val bearerTokenUrl = "/oauth2/token"
  val bearerToken = "AAAAAAAAAAAA"
  val bearerTokenResponse = s"""{"token_type":"bearer", "access_token":"$bearerToken"}"""
  val base64EncodedBearerTokenCredentials = "eHZ6MWV2RlM0d0VFUFRHRUZQSEJvZzpMOHFxOVBaeVJnNmllS0dFS2hab2xHQzB2SldMdzhpRUo4OERSZHlPZw=="
  val basicAuthorizationHeader = s"Basic $base64EncodedBearerTokenCredentials"
  val formUrlEncodedHeader = "application/x-www-form-urlencoded;charset=UTF-8"
  val queryString = Seq("lat" -> latitude, "long" -> longitude, "granularity" -> "city")    

}
