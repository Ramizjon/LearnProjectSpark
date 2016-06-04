package driver

import java.util.Properties

import kafka.producer.KeyedMessage
import org.apache.spark.rdd.RDD
import org.cloudera.spark.streaming.kafka.KafkaWriter._
import org.slf4j.LoggerFactory
import utils.{RDDTransformer, UserModCommand, Convertor, UMCKryoEncoder}

trait LogTrait {
  protected lazy val logger = LoggerFactory.getLogger(getClass)
}

abstract class RDDProcessor extends LogTrait with RDDTransformer {

  @transient
  val convertor: Convertor

  def processRDDStreaming(rdd: RDD[(String, String)],
                          producerConf: Properties,
                          outPutTopic: String): Unit = {

    val updatedRdd = rdd.values
    val conv = convertor
    updatedRdd.persist()
    val count = rdd.count()
    if (count > 0) {
      transformRDD(updatedRdd, conv)
        .writeToKafka(producerConf,
          (x: UserModCommand) => {
            new KeyedMessage[String, Array[Byte]](outPutTopic, UMCKryoEncoder.toBytes(x))
          })
    }
    updatedRdd.unpersist()
  }
}
