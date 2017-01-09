package models.forms

import java.text.SimpleDateFormat

import com.perhac.fuelmeter.FuelMeterTest
import models.Reading
import models.forms.ReadingForm.form
import utils.DateUtils.DateProvider

class ReadingFormTest extends FuelMeterTest {

  val fixedDate = "2017/01/09"

  "Reading Form" should {

    "have a default date if not provided by user" in {
      implicit val fixedDateProvider: DateProvider = () => new SimpleDateFormat("yyyy/MM/dd").parse(fixedDate)
      val readingForm = form.bind(Seq("reg" -> "NA08MYW", "mi" -> "12", "total" -> "123", "litres" -> "1.1", "cost" -> "1.3").toMap)
      inside(readingForm.value) {
        case Some(reading) => reading.date mustBe fixedDate
      }
    }

    "have the date as specified by user, if provided in correct format" in {
      val readingForm = form.fill(Reading("TESTREG", fixedDate, 1.0, 1, 1.0, 1.0))
      inside(readingForm.value) {
        case Some(reading) => reading.date mustBe fixedDate
      }
    }
  }
}
