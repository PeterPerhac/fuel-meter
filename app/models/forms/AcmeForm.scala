package models.forms

import java.net.URL

import play.api.data.Form
import play.api.data.Forms.{bigDecimal, mapping, nonEmptyText, of}
import play.api.data.validation.Constraints.min

case class AcmeProduct(product: String, price: BigDecimal, catalogueUrl: URL)

object AcmeForm {

  val form: Form[AcmeProduct] = Form(
    mapping(
      "product" -> nonEmptyText,
      "price" -> bigDecimal.verifying(min(BigDecimal(0.01))),
      "url" -> of[URL] //implicitly provided custom Formatter[URL] (models.forms.urlFormatter)
    )(AcmeProduct.apply)(AcmeProduct.unapply)
  )

}
