import java.util.Properties

import facebookprovider.FacebookOutputGenerator
import nexusprovider.NexusOutputGenerator
import utils.OutputGenerator
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}

object KafkaProducer {

  val numEvents = 50
  val brokers = "localhost:9092"
  val props = initProperties()


  private def initProperties(): Properties = {
    val properties = new Properties()
    properties.put("metadata.broker.list", brokers)
    properties.put("serializer.class", "kafka.serializer.StringEncoder")
    properties.put("producer.type", "async")
    properties
  }

  def produceMessages(topic: String, provider_mode: String): Unit = {
    val config = new ProducerConfig(props)
    val producer = new Producer[String, String](config)
    val outputGenerator = getGenerator(provider_mode)

    for (events <- Range(0, numEvents)) {
      Thread.sleep(1000)
      val message = outputGenerator.generateData()
      val data = new KeyedMessage[String, String](topic, message)
      producer.send(data)
    }
  }

  private def getGenerator(providerType: String): OutputGenerator = providerType match {
    case "facebook" => FacebookOutputGenerator
    case "nexus" => NexusOutputGenerator
    case default => throw new IllegalArgumentException
  }

}
