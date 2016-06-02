import nexusprovider.NexusConvertor
import org.junit.Test
import org.junit.runner.{RunWith, Runner}
import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, _}
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import utils.UserModCommand

import scala.collection.Map

@RunWith(classOf[JUnitRunner])
class NexusConvertorTest extends PropSpec with PropertyChecks with Matchers {

  val strGen = (n: Int) => Gen.listOfN(n, Gen.alphaChar).map(_.mkString)

  val mapGen = for {
    k1 <- strGen(4)
    k2 <- strGen(4)
    k3 <- strGen(4)
    v1 <- strGen(4)
  } yield scala.Predef.Map(k1 -> v1, k2 -> v1, k3 -> v1)

  val umcGens: Gen[(UserModCommand, Map[String, String])] =
    for {
      userId <- strGen(2)
      command <- Gen.oneOf("add", "delete")
      segmentTimestamps <- mapGen
    } yield UserModCommand(userId, command, segmentTimestamps) -> segmentTimestamps

  property("Convert string to umcs") {
    forAll(umcGens) {
      umcGens =>
        val segms = umcGens._2.keys.mkString(",")
        val timestamp = umcGens._2.values.last
        val userId = umcGens._1.userId
        val command = umcGens._1.command
        val complexInput = s"$timestamp,$userId,$command,$segms,"

        NexusConvertor.convert(complexInput).last should be(umcGens._1)
    }
  }
}
