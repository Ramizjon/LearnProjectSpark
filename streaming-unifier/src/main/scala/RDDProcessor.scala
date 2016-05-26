import java.util.Properties

import KafkaConfig._
import kafka.producer.KeyedMessage
import org.apache.spark.rdd.RDD
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.cloudera.spark.streaming.kafka.KafkaWriter._
import org.slf4j.LoggerFactory
import utils.{Convertor, UserModCommand}

trait LogTrait {
  protected lazy val logger = LoggerFactory.getLogger(getClass)
}

abstract class RDDProcessor extends LogTrait with Serializable {

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
            new KeyedMessage[String, String](outPutTopic, x.toString)
          })
    }
    updatedRdd.unpersist()
  }



  def transformRDD(input: RDD[String], conv: Convertor): RDD[UserModCommand] = {
    input
      .map(line => conv.convert(line))
      .flatMap(identity)
  }
}
