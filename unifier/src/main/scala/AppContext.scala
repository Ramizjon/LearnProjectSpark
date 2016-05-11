import facebookprovider.FacebookConvertor
import nexusconvertor.NexusConvertor
import org.apache.spark.{SparkConf, SparkContext}
import utils.Convertor

object AppContext {

   case class RDDProcessorImpl(providerType: String, sparkContext: SparkContext) extends RDDProcessor {

     override def getSparkContext(): SparkContext = {
        return sparkContext
     }

     def getConvertor():Convertor = {
       return convertorMatcher(providerType)
     }

     def convertorMatcher(x: String): Convertor = x match {
       case "nexus" =>  NexusConvertor
       case "facebook" =>  FacebookConvertor
     }
   }

}
