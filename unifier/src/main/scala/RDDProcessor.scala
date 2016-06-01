import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext


import utils.{RDDTransformer, Convertor, UserModCommand}

abstract class RDDProcessor {

  val sc: SparkContext
  val convertor: Convertor

  def processRDD(inputPath: String, outputPath: String): RDD[UserModCommand] = {
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val input = sc.textFile(inputPath)
    val conv = convertor
    val transformedRDD = RDDTransformer.transformRDD(input,conv)

    val umcDF = transformedRDD.toDF()
    umcDF.write.parquet(outputPath)

    transformedRDD
  }

}
