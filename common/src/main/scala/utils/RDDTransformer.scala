package utils

import org.apache.spark.rdd.RDD

object RDDTransformer {
  def transformRDD(input: RDD[String], conv: Convertor): RDD[UserModCommand] = {
    input
      .map(line => conv.convert(line))
      .flatMap(identity)
  }
}
