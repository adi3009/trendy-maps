import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec

import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import testdata._

/**
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
class IntegrationSpec extends PlaySpec with OneAppPerSuite with IntegrationSuiteMixin {

  "Application" should {

    "get closest locations from twitter" in {
      val locationsUrl = s"/locations/$latitude/$longitude"
      val Some(result) = route(FakeRequest(GET, locationsUrl))

      status(result) must be(OK)
      contentType(result) must be(Some("application/json"))
      contentAsJson(result) must be(Json.toJson(closetLocationsHavingTrends))
      verifyBearerTokenCall()
    }
  }

  override implicit lazy val app: FakeApplication = FakeApplication(
    additionalConfiguration = Map(
      "twitter.api.url" -> s"http://$host:$wireMockPort",
      "twitter.consumer.key" -> twitterConsumerKey,
      "twitter.consumer.secret" -> twitterConsumerSecret))
}
