package facebookprovider

import nexusprovider.Convertor
import utils.UserModCommand


object FacebookConvertor extends Convertor{

  override def convert(text: String): List[UserModCommand] = {

    val umcList: List [UserModCommand] = Nil
    val divisions = text.split("::")
    val outputPath = divisions(1)

    val structureElements = divisions(0).split("/")

    val segmentsToAdd = structureElements(1).split(",")
    val segmentsToRemove = structureElements(2).split(",")

    val pathArray = outputPath.split("/")
    val timestamp = pathArray(pathArray.length - 3) + "T" + pathArray(pathArray.length - 2) + ":00:00"

    if (segmentsToAdd.length > 0){
      val segmentsMap = segmentsToAdd
                      .map {
                        elem => (elem, timestamp)
                      }.toMap
      new UserModCommand(structureElements(0), "add", segmentsMap):: umcList
    }


    if (segmentsToRemove.length > 0){
      val segmentsMap = segmentsToRemove
        .map {
          elem => (elem, timestamp)
        }.toMap
      new UserModCommand(structureElements(0), "delete", segmentsMap):: umcList
    }

    return umcList
  }
}
