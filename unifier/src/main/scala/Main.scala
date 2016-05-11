import net.sf.cglib.proxy.NoOp
import org.apache.spark.SparkContext
import _root_.AppContext

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
      val facebookContextProcessor = AppContext.RDDProcessorImpl("facebook")
      facebookContextProcessor.processRDD(config.facebookProviderInputPath,config.outputPath)

      val nexusContextProcessor = AppContext.RDDProcessorImpl("nexus")
      nexusContextProcessor.processRDD(config.nexusProviderInputPath, config.outputPath)
    }
    case None => {
      println("Invalid input")
      System.exit(1)
    }
  }



}
