package utils

import org.apache.spark.rdd.RDD

trait RDDTransformer {
  def transformRDD(input: RDD[String], conv: Convertor): RDD[UserModCommand] = {
    val convertor = conv
    input
      .map(convertor.convert(_))
      .flatMap(identity)
  }
}