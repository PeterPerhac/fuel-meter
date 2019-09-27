package models

import play.api.mvc.Cookie

object VRegCookie {

  def toCookie(reg: String): Cookie = Cookie(
    name = VRegCookie.name,
    value = reg.filter(_.isLetterOrDigit).mkString,
    maxAge = Some(Int.MaxValue)
  )

  val name = "vreg"
}
