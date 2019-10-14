package services

import repository.DoobieTransactor
import uk.gov.hmrc.uritemplate.syntax.UriTemplateSyntax
import utils.TransactionSyntax

abstract class FuelMeterService extends TransactionSyntax with UriTemplateSyntax {

  def doobieTransactor: DoobieTransactor

}
