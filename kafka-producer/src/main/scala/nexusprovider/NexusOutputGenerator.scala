package nexusprovider

import utils.OutputGenerator

import scala.util.Random

object NexusOutputGenerator extends OutputGenerator {

  override def generateData(): String = {
    val size = 20
    val seed = Random.nextInt(10)
    val command = if (seed < 5) "add" else "delete"
    val userId = Random.nextInt(200).toString
    val segmentsList = Seq.fill(Random.nextInt(size))(randomAlpha(5)).mkString(",")
    val timestamp = Random.nextInt(90) + ":" + Random.nextInt(90) + ":" + Random.nextInt(90)
    timestamp + "," + userId + "," + command + "," + segmentsList
  }

}
