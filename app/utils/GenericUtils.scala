package utils

import scala.collection.GenTraversable
import scala.math.BigDecimal.RoundingMode

object GenericUtils {

  implicit class Averages[T: Numeric](xs: GenTraversable[T]) {
    def avg = BigDecimal(implicitly[Numeric[T]].toDouble(xs.sum) / xs.size).setScale(2, RoundingMode.HALF_EVEN)
  }

}
