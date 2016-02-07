package services

import com.google.inject.ImplementedBy
import models.{ Location, ReverseGeocode }

import scala.concurrent.Future

/**
 * @author Aditya Godara
 */
@ImplementedBy(classOf[TwitterClientImpl])
trait TwitterClient {
  def bearerTokenCredentials(): String
  def bearerToken(): Future[String]
  def closetLocations(geocode: ReverseGeocode): Future[List[Location]]
}
