package facebookprovider

import utils.OutputGenerator
import scala.util.Random

object FacebookOutputGenerator extends OutputGenerator {

  override def generateData(): String = {
    val size = 20
    val userId = Random.nextInt(200).toString
    val segmentsToAddList = Seq.fill(Random.nextInt(size))(randomAlpha(5)).mkString(",")
    val segmentsToRemoveList = Seq.fill(Random.nextInt(size))(randomAlpha(5)).mkString(",")
    val path = Seq.fill(size / 2)(Random.nextInt(size)).mkString("/")
    userId + "/" + segmentsToAddList + "/" + segmentsToRemoveList + "::" + path
  }

}
