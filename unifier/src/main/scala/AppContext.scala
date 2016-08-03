import facebookprovider.FacebookConvertor
import nexusprovider.NexusConvertor
import org.apache.spark.{SparkConf, SparkContext}
import utils.Convertor


object AppContext {

  case class AppConfig(
                        facebookProviderInputPath: String = "",
                        nexusProviderInputPath: String = "",
                        outputPath: String = "")

  class RDDProcessorImpl(providerType: String, sparkContext: SparkContext) extends RDDProcessor {
    override lazy val sc = sparkContext
    override lazy val convertor = convertorMatcher(providerType)

    def convertorMatcher(x: String): Convertor = x match {
      case "nexus" => NexusConvertor
      case "facebook" => FacebookConvertor
    }
  }

  def runJobs(config: AppConfig): Unit = {
    val conf = new SparkConf()
      .setAppName("data unifier")
      .setMaster("local")
    val sparkContext = new SparkContext(conf)

    val params = Seq(config.facebookProviderInputPath -> "facebook",
      config.nexusProviderInputPath -> "nexus")

    params.foreach { case (k, v) =>
      new AppContext.RDDProcessorImpl(v, sparkContext).processRDD(k, config.outputPath + "/" + v)
    }
  }
}
