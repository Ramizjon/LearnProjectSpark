package driver

import java.util.Properties

import kafka.producer.KeyedMessage
import org.apache.spark.rdd.RDD
import org.cloudera.spark.streaming.kafka.KafkaWriter._
import utils.{RDDTransformer, UserModCommand, Convertor, UMCKryoEncoder}
import org.apache.logging.log4j.LogManager

trait LogTrait {
  protected lazy val logger = LogManager.getLogger(getClass)
  //protected lazy val logger = Logger(LoggerFactory.getLogger(getClass))
  //protected lazy val logger = LoggerFactory.getLogger(getClass)
}

abstract class RDDProcessor extends LogTrait with RDDTransformer {

  val convertor: Convertor

  def processRDDStreaming(rdd: RDD[(String, String)],
                          producerConf: Properties,
                          outPutTopic: String): Unit = {

    val updatedRdd = rdd.values
    updatedRdd.persist()
    val count = rdd.count()
    if (count > 0) {
      val rdd = transformRDD(updatedRdd, convertor)
        rdd.writeToKafka(producerConf,
          (x: UserModCommand) => {
            new KeyedMessage[String, Array[Byte]](outPutTopic, UMCKryoEncoder.toBytes(x))
          })
    }
    updatedRdd.unpersist()
  }
}


