
import nexusconvertor.NexusConvertor
import nexusprovider.Convertor
import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

class RDDProcessor {

  def processRDD (sc: SparkContext, convertor: Convertor, inputPath: String, outputPath: String): Unit = {
      val sqlContext = new SQLContext(sc)
      import sqlContext.implicits._

      val input = sc.textFile(inputPath)

      val transformedRDD = input
            .map(line => convertor.convert(line))
            .flatMap(y => y)

      val umcDF = transformedRDD.toDF()

      umcDF.write.parquet(outputPath)
  }

}
