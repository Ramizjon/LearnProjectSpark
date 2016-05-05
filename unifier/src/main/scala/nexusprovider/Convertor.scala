package nexusprovider

import utils.UserModCommand

trait Convertor {
  def convert (text: String): List[UserModCommand]
}
