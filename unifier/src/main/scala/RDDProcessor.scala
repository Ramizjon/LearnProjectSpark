
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import utils.{Convertor, UserModCommand}

abstract class RDDProcessor extends Serializable{

  val sc: SparkContext
  val convertor: Convertor

  def processRDD(inputPath: String, outputPath: String): RDD[UserModCommand] = {
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val input = sc.textFile(inputPath)
    val transformedRDD = transformRDD(input)

    val umcDF = transformedRDD.toDF()
    umcDF.write.parquet(outputPath)

    transformedRDD
  }

  def transformRDD(input: RDD[String]): RDD[UserModCommand] = {
     input
      .map(line => convertor.convert(line))
      .flatMap(y => y)
  }

}
