package services

import java.net.URLEncoder
import java.util.Base64
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton
import models.Location
import models.ReverseGeocode
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSRequest
import models.Trend

/**
 * @author Aditya Godara
 */
@Singleton
class TwitterClientImpl @Inject() (ws: WSClient, twitterAuth: TwitterAuth)(implicit val executionContext: ExecutionContext) extends TwitterClient {

  val baseUrl = twitterAuth.apiUrl.get

  def bearerTokenCredentials(): String = {
    val ckUrlEncoded = URLEncoder.encode(twitterAuth.consumerKey.get, "UTF-8")
    val csUrlEncoded = URLEncoder.encode(twitterAuth.consumerSecret.get, "UTF-8")

    Base64.getEncoder.encodeToString((ckUrlEncoded + ":" + csUrlEncoded).getBytes)
  }

  def bearerToken(): Future[String] = {
    val request = ws.url(s"$baseUrl/oauth2/token")
      .withHeaders(
        "Authorization" -> s"Basic ${bearerTokenCredentials()}",
        "Content-Type" -> "application/x-www-form-urlencoded;charset=UTF-8")

    request.post(Map("grant_type" -> Seq("client_credentials"))) map { response =>
      (response.json \ "access_token").validate[String].fold(invalid => "", valid => valid)
    }
  }

  private def requestWithBearerToken[T](url: String)(block: WSRequest => Future[T]): Future[T] = {
    bearerToken().flatMap { token =>
      block(ws.url(s"$baseUrl/1.1/$url").withHeaders("Authorization" -> s"Bearer $token"))
    }
  }

  /**
   * gets the locations that Twitter has trending topic information for,
   * closest to a specified latitude and longitude.
   */
  def closetLocations(reverseGeocode: ReverseGeocode): Future[List[Location]] = requestWithBearerToken("trends/closest.json") { request =>
    request.withQueryString("lat" -> reverseGeocode.latitude, "long" -> reverseGeocode.longitude)
      .get().map { response =>
        response.json.validate[List[Location]].fold(s => Nil, l => l)
      }
  }

  /**
   * Returns the top 50 trending topics for a specific WOEID, if trending information is available for it.
   */
  def trendsFor(woeid: String): Future[List[Trend]] = requestWithBearerToken("trends/place.json") { request =>
    request.withQueryString("id" -> woeid)
      .get().map { response =>
        (response.json \\ "trends")(0).validate[List[Trend]].fold(s => Nil, l => l)
      }
  }
}
