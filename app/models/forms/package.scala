package models

import java.net.URL

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms.{jodaLocalDate, optional}
import play.api.data.{FormError, Mapping}
import play.api.data.format.Formatter
import utils.DateUtils._

package object forms {

  def dateStringMapping(datePattern: String, defaultDateProvider: DateProvider): Mapping[String] = {
    optional(jodaLocalDate(datePattern)) transform(
      old => old map (_.toString(datePattern)) getOrElse defaultDateProvider().toFormat(datePattern),
      s => Some(LocalDate.parse(s, DateTimeFormat.forPattern(datePattern)))
    )
  }


  private def parsing[T](parse: String => T, errMsg: String, errArgs: Seq[Any])(key: String, data: Map[String, String]): Either[Seq[FormError], T] = {
    play.api.data.format.Formats.stringFormat.bind(key, data).right.flatMap { s =>
      scala.util.control.Exception.allCatch[T]
        .either(parse(s))
        .left.map(e => Seq(FormError(key, errMsg, errArgs)))
    }
  }

  implicit def urlFormatter: Formatter[URL] = new Formatter[URL] {

    override val format = Some(("format.url", Nil))

    override def bind(key: String, data: Map[String, String]) = parsing(new URL(_), "error.url", Nil)(key, data)

    override def unbind(key: String, value: URL) = Map(key -> value.toString)
  }
}
