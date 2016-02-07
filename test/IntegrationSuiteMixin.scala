import java.net.ServerSocket

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Suite

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._

import testdata._

/**
 * @author Aditya Godara
 */
trait IntegrationSuiteMixin extends BeforeAndAfterAll {
  this: Suite =>

  val wireMockPort = getPort()

  val server = new WireMockServer(wireMockConfig().port(wireMockPort))

  override protected def beforeAll(): Unit = {
    server.start()
    WireMock.configureFor(host, wireMockPort)
    bearerTokenStub()
    locationsStub()
    super.beforeAll()
  }

  override protected def afterAll(): Unit = {
    try super.afterAll()
    finally server.stop()
  }

  def bearerTokenStub(): Unit = {
    stubFor(post(urlEqualTo(bearerTokenUrl))
        .withHeader("Authorization", containing(basicAuthorizationHeader))
        .withHeader("Content-Type", containing(formUrlEncodedHeader))
        .withRequestBody(equalTo("grant_type=client_credentials"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody(bearerTokenResponse)))
  }
  
  def locationsStub(): Unit = {
    stubFor(get(urlPathMatching(twitterClosetLocationsUrl))
      .withHeader("Authorization", containing(bearerToken))
      .withQueryParam("lat", containing(latitude))
      .withQueryParam("long", containing(longitude))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(closetLocationsResponse)))
  }

  def verifyBearerTokenCall(): Unit = {
    verify(postRequestedFor(urlEqualTo(bearerTokenUrl))
        .withHeader("Authorization", containing(basicAuthorizationHeader))
        .withHeader("Content-Type", containing(formUrlEncodedHeader))
        .withRequestBody(equalTo("grant_type=client_credentials")))        
  }
  
  def getPort() = {
    val socket = new ServerSocket(0)
    val port = socket.getLocalPort
    socket.close()
    port
  }
}
