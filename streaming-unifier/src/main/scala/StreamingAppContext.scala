import facebookprovider.FacebookConvertor
import kafka.serializer.StringDecoder
import nexusprovider.NexusConvertor
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import utils.Convertor


object StreamingAppContext {

  case class AppConfig(
                        broker: String = "",
                        topics: String = "",
                        provider_type: String = "")

  class RDDProcessorImpl(providerType: String) extends RDDProcessor {
    override lazy val convertor = convertorMatcher(providerType)

    def convertorMatcher(x: String): Convertor = x match {
      case "nexus" => NexusConvertor
      case "facebook" => FacebookConvertor
    }
  }

  def runStreamingJobs (config: AppConfig): Unit ={
    val sparkConf = new SparkConf()
      .setAppName("DirectKafkaUnifier")
    val ssc = new StreamingContext(sparkConf, Duration.apply(2))

    val topicsSet = config.topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> config.broker)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)
    new StreamingAppContext.RDDProcessorImpl(config.provider_type).processRDDStreaming(messages)

    ssc.start()
    ssc.awaitTermination()
  }

}
