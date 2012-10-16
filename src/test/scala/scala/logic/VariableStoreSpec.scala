package scala.logic

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.specification.Scope

@RunWith(classOf[JUnitRunner])
object VariableStoreSpec extends Specification {
  
  trait store extends Scope {
    implicit val variableStore = new VariableStore
  }
  
  trait sampleData extends store {
    val x = Var[Int]("X")
  }
  
  trait sampleDataBound extends sampleData {
    val y = Var[Int]("Y")
    x =:= y must beUnifiable
  }
  
  def notBeUnifiable = throwA(new UnificationException("", null, null))
  def beUnifiable = throwA[Throwable].not

  "A variable store" should {
    "provide the same variable objects for the same names" in new sampleData {
      Var[Int]("X") must be equalTo(x)
    }
    "raise a runtime exception if retrieving a variable of wrong type" in new sampleData {
      variableStore.provideVar[String]("X") must throwA[Throwable]
      Var[String]("X") must throwA[Throwable]
    }
  }
}