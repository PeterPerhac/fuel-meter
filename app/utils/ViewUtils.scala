package utils

import views.html

/**
  * Created by peterperhac on 20/12/2016.
  */
object ViewUtils {

    import views.html.helper.FieldConstructor
    implicit val myFields = FieldConstructor(html.customInputField.f)

}
