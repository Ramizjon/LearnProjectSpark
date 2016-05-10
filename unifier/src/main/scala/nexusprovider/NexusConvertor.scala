package nexusconvertor

import nexusprovider.Convertor
import utils.UserModCommand


@transient
object NexusConvertor extends Convertor {

  def convert (text: String): List[UserModCommand] = {

    val arr = text.split(",")
    val segmentsMap = arr
      .view(3,arr.size)
      .map { v =>
      (v, arr(0))
      }.toMap

    val userModCommand = new UserModCommand(arr(1), arr(2), segmentsMap)

    return userModCommand :: Nil

  }

}
