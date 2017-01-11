import org.apache.spark.ml.classification.BinaryLogisticRegressionSummary;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.classification.LogisticRegressionTrainingSummary;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;


import static org.apache.spark.sql.functions.col;

public class Test1 {
  public static void main(String[] args) throws AnalysisException {
    SparkSession spark = SparkSession
        .builder()
        .appName("Test1")
        .config("spark.some.config.option", "some-value")
        .getOrCreate();

//    runBasicDataFrameExample(spark);

     runLogisticRegression(spark);
    //test(spark);

//    Utils.csvToLibsvm("src/main/resources/iris.csv", "src/main/resources/iris_libsvm.txt");

    spark.stop();

  }

  private static void test(SparkSession spark) {
    Dataset<Row> training = spark.read().format("libsvm")
        .load("src/main/resources/sample_libsvm_data.txt");

//    training.show();
//    training.printSchema();

    Utils.csvToLibsvm("src/main/resources/binary.csv", "src/main/resources/binary.libsvm");

  }

  private static void runBasicDataFrameExample(SparkSession spark) throws AnalysisException {
    Dataset<Row> df = spark.read().json("src/main/resources/people.json");

    df.show();
    df.printSchema();
    df.select("name").show();
    df.select(col("name"), col("age").plus(1)).show();
    df.filter(col("age").gt(21)).show();
    df.groupBy("age").count().show();

//    Dataset<Row> df = spark.read().format("org.apache.spark.csv")
//        .option("header", "true")
//        .csv("src/main/resources/binary.csv");
//
//    df = df.withColumn("admit", df.col("admit").cast(DataTypes.BooleanType))
//        .withColumn("gre", df.col("gre").cast(DataTypes.IntegerType))
//        .withColumn("gpa", df.col("gpa").cast(DataTypes.DoubleType))
//        .withColumn("rank", df.col("rank").cast(DataTypes.IntegerType));
//
//    df.show();
//    df.printSchema();
  }


  private static void runLogisticRegression(SparkSession spark) {
    Dataset<Row> training = spark.read().format("libsvm")
        .load("src/main/resources/iris_libsvm.txt");

//    Dataset<Row> training = spark.read().format("libsvm")
//        .load("src/main/resources/sample_libsvm_data.txt");

    training.show();
    training.printSchema();

    LogisticRegression lr = new LogisticRegression()
        .setMaxIter(100)
        .setRegParam(0.3)
        .setElasticNetParam(0.8);

    // Fit the model
    LogisticRegressionModel lrModel = lr.fit(training);
//    try {
//      lrModel.save("lrModel");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    // Print the coefficients and intercept for logistic regression
    System.out.println("** Coefficients: "
        + lrModel.coefficients() + " Intercept: " + lrModel.intercept());

    LogisticRegressionTrainingSummary trainingSummary = lrModel.summary();
    System.out.println("numIterations: " + trainingSummary.totalIterations());

    BinaryLogisticRegressionSummary binarySummary = (BinaryLogisticRegressionSummary) trainingSummary;
    Dataset<Row> roc = binarySummary.roc();
    roc.show();
    roc.select("FPR").show();
    System.out.println(binarySummary.areaUnderROC());

    spark.stop();
  }


}









