package utils

import scala.collection.GenTraversable
import scala.math.BigDecimal.RoundingMode

object GenericUtils {

  implicit class Averages[T](xs: GenTraversable[T])(implicit num: Numeric[T]) {
    def avg: BigDecimal = xs match {
      case Nil => BigDecimal(0)
      case _   => BigDecimal(num.toDouble(xs.sum) / xs.size).setScale(2, RoundingMode.HALF_EVEN)
    }
  }

}
