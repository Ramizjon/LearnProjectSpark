import AppContext.AppConfig

object Main extends App {

  val parser = new scopt.OptionParser[AppConfig]("unifier") {
    opt[String]('f', "facebook").required().text("facebook input path is required")
      .action { (x, c) => c.copy(facebookProviderInputPath = x) }
    opt[String]('n', "nexus").required().text("nexus input path is required")
      .action { (x, c) => c.copy(nexusProviderInputPath = x) }
    opt[String]('o', "output").required().text("output path is required")
      .action { (x, c) => c.copy(outputPath = x) }
  }

  parser.parse(args, AppConfig()) match {
    case Some(config) => {
      AppContext.runJobs(config)
    }
    case None => {
      println("Invalid input")
      System.exit(1)
    }
  }

}
