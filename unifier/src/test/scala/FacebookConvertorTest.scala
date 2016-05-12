import facebookprovider.FacebookConvertor
import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, _}
import utils.UserModCommand

import scala.collection.Map

object FacebookConvertorTest extends Properties("UserModCommand") {

  val strGen = (n: Int) => Gen.listOfN(n, Gen.alphaChar).map(_.mkString)
  val stringLength = 6

  val pathGen = for {
    c1 <- strGen(stringLength)
    c2 <- strGen(stringLength)
    c3 <- strGen(stringLength)
    c4 <- strGen(stringLength)
  } yield List(c1,c2,c3,c4).mkString("/")

  def mapGen (timestamp: String): Gen[scala.Predef.Map[String,String]] = for {
    k1 <- strGen(stringLength)
    k2 <- strGen(stringLength)
    k3 <- strGen(stringLength)
    v1 <- strGen(stringLength)
  } yield scala.Predef.Map (k1->timestamp,k2->timestamp,k3->timestamp)

  val umcsGens: Gen[(List[(UserModCommand, Map[String, String])],String)] =
    for {
      userId <- strGen(2)
      command1 <- Gen.const("add")
      command2 <- Gen.const("remove")

      outputPath <- pathGen
      pathArray <- Gen.const(outputPath.split("/"))
      timestamp <- Gen.const(s"${pathArray(pathArray.length - 3)}T${pathArray(pathArray.length - 2)}:00:00")

      segmentTimestamps1 <- mapGen(timestamp.toString)
      segmentTimestamps2 <- mapGen(timestamp.toString)

    } yield {
      List(UserModCommand (userId, command1, segmentTimestamps1)
        -> segmentTimestamps1,
        UserModCommand (userId, command2, segmentTimestamps2)
      ->segmentTimestamps2)->outputPath
    }

  property("Convert strings to umcs") = forAll(umcsGens) {
    umcsGens =>

      val userId1 = umcsGens._1.head._1.userId
      val segments1 = umcsGens._1.head._2.keys.mkString(",")
      val segments2 = umcsGens._1.head._2.keys.mkString(",")

      val complexInput = s"$userId1/$segments1/$segments2::${umcsGens._2}"

      val userModCommandList = umcsGens._1(0)._1 :: umcsGens._1(1)._1 :: Nil

      //checks both UserModCommand classes with "add" and "remove" commands
      FacebookConvertor.convert(complexInput) == userModCommandList
  }

}
