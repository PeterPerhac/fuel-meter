package utils

import play.api.libs.json.{JsValue, Writes}

trait JsonSyntax {
  implicit class JsonOps[T: Writes](t: T) {
    val asJson: JsValue = implicitly[Writes[T]].writes(t)
  }
}
object JsonSyntax extends JsonSyntax
