
import nexusconvertor.NexusConvertor
import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

abstract class RDDProcessor {

  def processRDD (inputPath: String, outputPath: String): Unit = {
      val sc = getSparkContext()
      val convertor = getConvertor()
      val sqlContext = new SQLContext(sc)
      import sqlContext.implicits._

      val input = sc.textFile(inputPath)
      val transformedRDD = input
            .map(line => convertor.convert(line))
            .flatMap(y => y)

      val umcDF = transformedRDD.toDF()

      umcDF.write.parquet(outputPath)
  }

  def getSparkContext(): SparkContext
  def getConvertor(): Convertor


}
