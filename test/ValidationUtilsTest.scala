import org.scalatest.{FlatSpec, Inside, Inspectors, Matchers}
import play.api.data.validation.{Constraint, Invalid, Valid}
import utils.ValidationUtils._


class ValidationUtilsTest extends FlatSpec with Matchers with Inside with Inspectors {

  val doubleConstraint: Constraint[Double] = inRange(1.0, 10.0)

  "Double field validation" should "reject values below min bound" in {
    forAll(List(0.9999, 0.0, -1.23))(d => inside(doubleConstraint(d)) {
      case Invalid(errors) => errors.head.message shouldBe "error.range.below"
    })
  }

  "Double field validation" should "reject values above max bound" in {
    forAll(List(10.001, 12.34, 10000))(d => inside(doubleConstraint(d)) {
      case Invalid(errors) => errors.head.message shouldBe "error.range.above"
    })
  }

  "Double field validation" should "accept values on the boundaries" in {
    forAll(List(1.0, 10.0)) {
      doubleConstraint(_) shouldBe Valid
    }
  }

  "Double field validation" should "accept values within the range" in {
    forAll(List(1.001, 1.2, 2.0, 9.7, 9.9999, 10.0)) {
      doubleConstraint(_) shouldBe Valid
    }
  }

  "Double field validation constraint" should "complain if min >= max" in {
    intercept[AssertionError] {
      inRange(2.0, 1.0).apply(5.0)
    }
  }

  /**
    * =============================
    * test the string constraint that should only accept a string if it's blank or non-existent
    * or if it matches a regex pattern
    * =============================
    */

  val stringConstraint: Constraint[String] = optionallyMatchingPattern("""^20\d\d\/[01]\d\/[0123]\d$""")

  "Optional String field validation" should "accept empty string" in {
    stringConstraint("") shouldBe Valid
  }

  "Optional String field validation" should "accept null string" in {
    stringConstraint(null) shouldBe Valid
  }

  "Optional String field validation" should "accept string matching pattern" in {
    forAll(List("2016/12/17", "2015/04/10")) {
      stringConstraint(_) shouldBe Valid
    }
  }

  "Optional String field validation" should "reject string not matching pattern" in {
    forAll(List("foo foo the snoo", "hello", "2016/34/15", "12/12/2014"))(s => inside(stringConstraint(s)) {
      case Invalid(errors) => errors.head.message shouldBe "error.string.pattern"
    })
  }

}
