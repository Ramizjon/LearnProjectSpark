import facebookprovider.FacebookConvertor
import nexusconvertor.NexusConvertor
import org.apache.spark.{SparkConf, SparkContext}

object AppContext {

   case class RDDProcessorImpl(providerType: String) extends RDDProcessor {

     override def getSparkContext(): SparkContext = {
       val conf = new SparkConf().setAppName(s"$providerType-unifier")
       return new SparkContext(conf)
     }

     def getConvertor():Convertor = providerType match {
       case "nexus" => return NexusConvertor
       case "facebook" => return FacebookConvertor
     }
   }

}
