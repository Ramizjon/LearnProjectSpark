import com.holdenkarau.spark.testing.SharedSparkContext
import nexusprovider.NexusConvertor

import org.scalatest.{Matchers, GivenWhenThen, FlatSpec}

class RDDProcessorTest extends FlatSpec with GivenWhenThen with SharedSparkContext with Matchers {

  val rddProcessor = new AppContext.RDDProcessorImpl("nexus", sc)
  val expected = NexusConvertor.convert("10:12:2016,11,add,click,open_page,generate_link") ::
    NexusConvertor.convert("09:12:2016,7,delete,drag,drop") :: Nil flatten

  "text file" should "be converted into list of UserModCommands " +
    "line by line with RDDProcessor" in {

    Given("Received data in raw text format")
    val lines = Array("10:12:2016,11,add,click,open_page,generate_link",
      "09:12:2016,7,delete,drag,drop")

    When("Transforming data line by line with RDDProcessor")
    val actualResult = rddProcessor.transformRDD(sc.parallelize(lines),
      NexusConvertor).collect toList

    Then("Data is transformed into UMC's List")
    actualResult should equal(expected)
  }

}
