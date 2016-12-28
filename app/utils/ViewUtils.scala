package utils

import views.html

object ViewUtils {

  import views.html.helper.FieldConstructor

  implicit val myFields = FieldConstructor(html.customInputField.f)

}
