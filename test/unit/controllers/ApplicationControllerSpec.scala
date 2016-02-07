package unit.controllers

import scala.concurrent.Future

import org.mockito.Mockito.{ atLeastOnce, verify, when }
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

import com.google.inject.ImplementedBy

import controllers.ApplicationController
import play.api.libs.json.Json
import play.api.mvc.Results
import play.api.test._
import play.api.test.Helpers._
import services.{ TwitterClient, TwitterClientImpl }
import testdata._

/**
 * @author Aditya Godara
 */
class ApplicationControllerSpec extends PlaySpec with Results with MockitoSugar {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  "ApplicationController" should {
    "list closest trend locations" in new ControllerHelper {      
      when(mockTwitterClient.closetLocations(reverseGeocode)) thenReturn Future(closetLocationsHavingTrends)      
      
      val result = controller.locations(latitude, longitude).apply(FakeRequest())
      val resultAsJson = contentAsJson(result)

      verify(mockTwitterClient, atLeastOnce()).closetLocations(reverseGeocode)
      resultAsJson must be(Json.toJson(closetLocationsHavingTrends))
    }
    
    "list trends for a place(woeid)" in new ControllerHelper {
      when(mockTwitterClient.trendsFor(woeid)) thenReturn Future(trendsList)
      
      val result = controller.trends(woeid).apply(FakeRequest())
      val resultAsJson = contentAsJson(result)
      
      verify(mockTwitterClient, atLeastOnce()).trendsFor(woeid) 
      resultAsJson must be(Json.toJson(trendsList))
    }
  }
  
  abstract class ControllerHelper {
    val mockTwitterClient = mock[TwitterClient]
    val controller = new ApplicationController(mockTwitterClient)
  }
}
