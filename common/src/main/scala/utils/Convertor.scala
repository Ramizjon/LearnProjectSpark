package utils

trait Convertor extends Serializable {
  def convert(text: String): List[UserModCommand]
}
