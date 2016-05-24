import facebookprovider.FacebookConvertor
import kafka.serializer.StringDecoder
import nexusprovider.NexusConvertor
import java.util.Properties
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import utils.Convertor


object StreamingAppContext extends Serializable {

  case class AppConfig(
                        broker: String = "",
                        inputTopics: String = "",
                        outputTopic: String = "",
                        provider_type: String = "")

  class RDDProcessorImpl(providerType: String) extends RDDProcessor {
    override lazy val convertor = convertorMatcher(providerType)

    def convertorMatcher(x: String): Convertor = x match {
      case "nexus" => NexusConvertor
      case "facebook" => FacebookConvertor
    }
  }

  def runStreamingJobs(config: AppConfig): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("directkafkaunifier")
    val ssc = new StreamingContext(sparkConf, Duration.apply(10000))

    val inputTopicsSet = config.inputTopics.split(",").toSet
    val kafkaParams = KafkaConfig.defaultConfig(config.broker)

    import KafkaConfig._
    val properties =  kafkaParams.toProperties

    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, inputTopicsSet)

    val runClosure = (rdd: RDD[(String,String)]) =>
      new StreamingAppContext.RDDProcessorImpl(config.provider_type)
      .processRDDStreaming(rdd, properties, config.outputTopic)

    messages.foreachRDD(runClosure)

    ssc.start()
    ssc.awaitTermination()
  }




}
