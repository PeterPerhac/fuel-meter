package auth.twitter

import play.api.Configuration
import scalaj.http.Token

case class TwitterOAuthConfig(
    private val baseUri: String,
    apiKey: String,
    apiSecret: String,
    callbackUrl: String,
    private val requestTokenPath: String,
    private val accessTokenPath: String,
    private val authPagePathTemplate: String
) {
  val consumerToken = Token(key = apiKey, secret = apiSecret)

  val requestTokenUrl: String = baseUri + requestTokenPath
  val authPageUrlTemplate: String = baseUri + authPagePathTemplate
  val accessTokenUrl: String = baseUri + accessTokenPath
}

object TwitterOAuthConfig {
  def apply(configuration: Configuration): TwitterOAuthConfig = TwitterOAuthConfig(
    baseUri = configuration.get[String]("oauth.twitter.baseUri"),
    apiKey = configuration.get[String]("oauth.twitter.apiKey"),
    apiSecret = configuration.get[String]("oauth.twitter.apiSecret"),
    callbackUrl = configuration.get[String]("oauth.twitter.callbackUrl"),
    requestTokenPath = configuration.get[String]("oauth.twitter.requestTokenPath"),
    accessTokenPath = configuration.get[String]("oauth.twitter.accessTokenPath"),
    authPagePathTemplate = configuration.get[String]("oauth.twitter.authPagePathTemplate")
  )
}
