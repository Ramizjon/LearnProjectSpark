package facebookprovider

import utils.{Convertor, UserModCommand}


@transient
object FacebookConvertor extends Convertor {

  override def convert(text: String): List[UserModCommand] = {

    val divisions = text.split("::")
    val outputPath = divisions(1)

    val structureElements = divisions(0).split("/")

    val pathArray = outputPath.split("/")
    val timestamp = s"${pathArray(pathArray.length - 3)}T${pathArray(pathArray.length - 2)}:00:00"

    val segmentsCombinedList = structureElements(1).split(",")->"add" :: structureElements(2).split(",")->"remove" :: Nil

    return segmentsCombinedList.map( t => {
      val segMap = t._1.map {
        elem => (elem, timestamp)
      }.toMap
      new UserModCommand(structureElements(0), t._2, segMap)
    })

  }
}
