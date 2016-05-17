import java.io.Serializable

import facebookprovider.FacebookConvertor
import nexusprovider.NexusConvertor
import org.apache.spark.{SparkConf, SparkContext}
import utils.Convertor


object AppContext extends Serializable {

  class RDDProcessorImpl(providerType: String, sparkContext: SparkContext) extends RDDProcessor {

    @transient
    override lazy val sc = sparkContext
    override lazy val convertor = convertorMatcher(providerType)

    def convertorMatcher(x: String): Convertor = x match {
      case "nexus" => NexusConvertor
      case "facebook" => FacebookConvertor
    }
  }

  def createSparkContext(): SparkContext = {
    val conf = new SparkConf()
      .setAppName("data unifier")
      .setMaster("local")
    new SparkContext(conf)
  }

}
