package services

import javax.inject.{Singleton, Inject}

import play.api.Configuration

/**
 * @author Aditya Godara
 */
@Singleton
class TwitterAuth @Inject()(config: Configuration) {
  val apiUrl = config.getString("twitter.api.url") 
  val consumerKey = config.getString("twitter.consumer.key")
  val consumerSecret = config.getString("twitter.consumer.secret")  
}
