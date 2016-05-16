
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import utils.{Convertor, UserModCommand}

abstract class RDDProcessor extends Serializable {

  @transient val sc: SparkContext
  val convertor: Convertor

  def processRDD(inputPath: String, outputPath: String): RDD[UserModCommand] = {
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val input = sc.textFile(inputPath)
    val conv = convertor
    val transformedRDD = transformRDD(input,conv)

    val umcDF = transformedRDD.toDF()
    umcDF.write.parquet(outputPath)

    transformedRDD
  }

  def transformRDD(input: RDD[String], conv: Convertor): RDD[UserModCommand] = {
     input
      .map(line => conv.convert(line))
      .flatMap(y => y)
  }
}
