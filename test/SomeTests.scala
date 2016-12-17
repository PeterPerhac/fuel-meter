import org.scalatest.{FlatSpec, Inside, Inspectors, Matchers}
import play.api.data.validation.{Constraint, Invalid, Valid}

class SomeTests extends FlatSpec with Matchers with Inside with Inspectors {

  import controllers.validation.CustomValidationSupport._

  val constraint: Constraint[Double] = doubleInRange(1.0, 10.0)

  "Double field validation" should "reject values below min bound" in {
    forAll(List(0.9999, 0.0, -1.23))(d => inside(constraint(d)) { case Invalid(errors) => errors.head.message shouldBe "error.double.range.min" })
  }

  "Double field validation" should "reject values above max bound" in {
    forAll(List(10.001, 12.34, 10000))(d => inside(constraint(d)) { case Invalid(errors) => errors.head.message shouldBe "error.double.range.max" })
  }

  "Double field validation" should "accept values on the boundaries" in {
    forAll(List(1.0, 10.0))(constraint(_) shouldBe Valid)
  }

  "Double field validation" should "accept values within the range" in {
    forAll(List(1.001, 1.2, 2.0, 9.7, 9.9999, 10.0))(constraint(_) shouldBe Valid)
  }

  "Double field validation constraint" should "complain if min >= max" in {
    assertThrows[AssertionError](doubleInRange(2.0, 1.0).apply(5.0))
  }

}
