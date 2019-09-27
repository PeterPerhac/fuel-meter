package utils

import java.util.Base64.{getEncoder => b64Encoder}
import java.util.UUID.randomUUID

trait OAuthUtils {
  def nonce(): String =
    b64Encoder.encodeToString(randomUUID().toString.getBytes).replaceAll("\\W", "")
}
object OAuthUtils extends OAuthUtils
