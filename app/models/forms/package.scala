package models

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms.{jodaLocalDate, optional}
import play.api.data.Mapping
import utils.DateUtils._

package object forms {

  def dateString(datePattern: String, defaultDateProvider: DateProvider): Mapping[String] = {
    optional(jodaLocalDate(datePattern)) transform(
      old => old map (_.toString(datePattern)) getOrElse defaultDateProvider().toFormat(datePattern),
      s => Some(LocalDate.parse(s, DateTimeFormat.forPattern(datePattern)))
    )
  }

}
