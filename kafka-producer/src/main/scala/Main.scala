

object Main extends App{

  case class AppConfig (topic: String = "",
                        providerMode: String = "")

  val parser = new scopt.OptionParser[AppConfig]("unifier") {
    opt[String]('t', "topic").required().text("topic specification is required")
      .action { (x, c) => c.copy(topic = x) }
    opt[String]('p', "provider").required().text("provider mode specification is required")
      .action { (x, c) => c.copy(providerMode = x) }
  }

  parser.parse(args, AppConfig()) match {
    case Some(config) => {
        KafkaProducer.produceMessages(config.topic, config.providerMode)
    }

    case None => {
      println("Invalid input")
      System.exit(1)
    }
  }
}
