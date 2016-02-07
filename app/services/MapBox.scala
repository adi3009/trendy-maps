package services

import javax.inject.Inject
import javax.inject.Singleton
import play.api.Configuration

@Singleton
case class MapBox @Inject() (configuration: Configuration) {
  val id = configuration.getString("mapbox.id").get
  val accessToken = configuration.getString("mapbox.access_token").get
}