import java.util.Properties

import kafka.producer.KeyedMessage
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import utils.{Convertor, UserModCommand}
import org.cloudera.spark.streaming.kafka.KafkaWriter._


abstract class RDDProcessor {

  val convertor: Convertor

  def processRDDStreaming(inputDStream: InputDStream[(String, String)],
                          producerConf: Properties,
                          topic: String): Unit = {
    inputDStream.map(_._2)
      .foreachRDD(rdd => {
      transformRDD(rdd, convertor)
        .writeToKafka(producerConf,
          (x: UserModCommand) => new KeyedMessage[String, UserModCommand](topic, x))
    })
  }

  def transformRDD(input: RDD[String], conv: Convertor): RDD[UserModCommand] = {
    input
      .map(line => conv.convert(line))
      .flatMap(identity)
  }
}
