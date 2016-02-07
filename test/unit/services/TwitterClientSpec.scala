package unit.services

import scala.concurrent.Await
import scala.concurrent.duration._



import org.scalatestplus.play.PlaySpec

import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc._
import play.api.routing.sird._
import play.api.test._
import play.core.server.Server
import services.TwitterAuth
import services.TwitterClient
import services.TwitterClientImpl
import testdata._

/**
 * @author Aditya Godara
 */
class TwitterClientSpec extends PlaySpec {
  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val twitterAuth = new TwitterAuth(Configuration(
    "twitter.api.url" -> "",
    "twitter.consumer.key" -> twitterConsumerKey,
    "twitter.consumer.secret" -> twitterConsumerSecret))

  "TwitterClient" should {
    "encode consumer key and secret" in {
      withTwitterClient { client =>
        val result = client.bearerTokenCredentials()
        result must be(base64EncodedBearerTokenCredentials)
      }
    }

    "request bearer token" in {
      withTwitterClient { client =>
        val result = Await.result(client.bearerToken(), 5 seconds)
        result must be(bearerToken)
      }
    }

    "request closest locations" in {
      withTwitterClient { client =>
        val result = Await.result(client.closetLocations(reverseGeocode), 5 seconds)
        result must be(closetLocationsHavingTrends)
      }
    }
    
    "request trends" in {
      withTwitterClient { client =>
        val result = Await.result(client.trendsFor(woeid), 5 seconds)
        result must be(trendsList)
      }
    }
  }

  def withTwitterClient[T](block: TwitterClient => T): T = {
    Server.withRouter() {
      case POST(p"/oauth2/token") => Action {
        Results.Ok(Json.parse(bearerTokenResponse))
      }
      case GET(p"/1.1/trends/closest.json") => Action {
        Results.Ok(Json.parse(closetLocationsResponse))
      }
      case GET(p"/1.1/trends/place") => Action {
        Results.Ok(Json.parse(trendsResponse))
      }
    } { implicit port =>
      WsTestClient.withClient { client =>
        block(new TwitterClientImpl(client, twitterAuth))
      }
    }
  }
}
