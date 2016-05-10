package utils


case class UserModCommand (val userId: String,val command: String,val segmentTimestamps: Map[String,String]) {}
