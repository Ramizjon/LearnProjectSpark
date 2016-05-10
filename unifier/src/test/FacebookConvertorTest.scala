import facebookprovider.FacebookConvertor
import org.scalacheck._
import collection.Map
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import utils.UserModCommand

object FacebookConvertorTest extends Properties("UserModCommand") {

  val strGen = (n: Int) => Gen.listOfN(n, Gen.alphaChar).map(_.mkString)

  val pathGen = for {
    c1 <- strGen(6)
    c2 <- strGen(6)
    c3 <- strGen(6)
    c4 <- strGen(6)
  } yield List(c1,c2,c3,c4).mkString("/")

  def mapGen (timestamp: String) = for {
    k1 <- strGen(4)
    k2 <- strGen(4)
    k3 <- strGen(4)
    v1 <- strGen(4)
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

      val userId1 = umcsGens._1(0)._1.userId
      val segments1 = umcsGens._1(0)._2.keys.mkString(",")
      val segments2 = umcsGens._1(1)._2.keys.mkString(",")

      val complexInput = s"$userId1/$segments1/$segments2::${umcsGens._2}"

      val userModCommandList = umcsGens._1(0)._1 :: umcsGens._1(1)._1 :: Nil

      //checks both UserModCommand classes with "add" and "remove" commands
      FacebookConvertor.convert(complexInput) == userModCommandList
  }

}
