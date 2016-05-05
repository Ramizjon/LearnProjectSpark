package utils


case class UserModCommand (userId: String, command: String, segmentTimestamps: Map[String,String]) {}
