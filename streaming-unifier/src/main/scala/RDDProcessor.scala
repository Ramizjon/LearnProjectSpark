import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import utils.{Convertor, UserModCommand}

abstract class RDDProcessor {

  val convertor: Convertor

  def processRDDStreaming(inputDStream: InputDStream[(String, String)]): Unit = {
    inputDStream.map(_._2)
      .foreachRDD(rdd => {
      transformRDD(rdd, convertor)
      //write back to Kafka
    })
  }

  def transformRDD(input: RDD[String], conv: Convertor): RDD[UserModCommand] = {
    input
      .map(line => conv.convert(line))
      .flatMap(identity)
  }
}
