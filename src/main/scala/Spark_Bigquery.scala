import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.ml._
import com.google.cloud.spark.bigquery._

object Spark_Bigquery {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[3]")
      .appName("Writing to Bigquery")
      .getOrCreate()

    // Configuration for Querying the Bigquery Table
    spark.conf.set("viewsEnabled", "true")
    spark.conf.set("materializationDataset", "yellowdataset")

    // Query Definition
    val sqlQuery =
      """
                SELECT VendorID , CAST(FORMAT("%.2f", SUM(trip_distance)) as DECIMAL) as total_distance
                FROM `project-gcp-378315.yellowdataset.yellowtable_05_2023`
                GROUP BY VendorID
                ORDER BY VendorID
                """
   // Performing Query on BigQuery table using Spark
    val bqQueryDf = spark.read
      .format("bigquery")
      .option("query", sqlQuery)
      .load()

    bqQueryDf.show()


  }
}
