import net.sf.cglib.proxy.NoOp
import org.apache.spark.{SparkConf, SparkContext}

object Main extends App {

  case class AppConfig (
     facebookProviderInputPath: String = "",
     nexusProviderInputPath: String = "",
     outputPath: String = "")

  val parser = new scopt.OptionParser[AppConfig]("unifier") {
    opt[String]('f', "facebook").required().text("facebook input path is required")
      .action { (x, c) => c.copy(facebookProviderInputPath = x)}
    opt[String]('n', "nexus").required().text("nexus input path is required")
      .action { (x, c) => c.copy(nexusProviderInputPath = x)}
    opt[String]('o', "output").required().text("output path is required")
      .action { (x, c) => c.copy(outputPath = x)}
  }

  parser.parse(args, AppConfig()) match {
    case Some(config) => {
      val conf = new SparkConf()
        .setAppName("data unifier")
        .setMaster("local")
      val sparkContext = new SparkContext(conf)

      val facebookContextProcessor = new AppContext.RDDProcessorImpl("facebook", sparkContext)
      facebookContextProcessor.processRDD(config.facebookProviderInputPath,config.outputPath)

      val nexusContextProcessor = new AppContext.RDDProcessorImpl("nexus", sparkContext)
      nexusContextProcessor.processRDD(config.nexusProviderInputPath, config.outputPath)
    }
    case None => {
      println("Invalid input")
      System.exit(1)
    }
  }

}
