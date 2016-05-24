import java.util.Properties

object KafkaConfig {

  type KafkaParams = Map[String, String]

  def defaultConfig(brokerList: String): KafkaParams = {
    Map(
      "metadata.broker.list" -> brokerList,
      "serializer.class" -> "kafka.serializer.StringEncoder",
      //"serializer.class" -> "kafka.serializer.DefaultEncoder",
      "key.serializer.class" -> "kafka.serializer.StringEncoder"
    )
  }

  implicit class KafkaConfigToProperties(kafkaConfig: KafkaParams) {
    def toProperties: Properties = {
      val producerConf = new Properties()
      kafkaConfig.foreach{ case (k, v) => producerConf.put(k, v)}
      producerConf
    }
  }
}