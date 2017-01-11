import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class JavaLogisticRegression {

  public static void main(String[] args) {
    SparkSession spark = SparkSession
        .builder()
        .appName("LogisticRegression")
        // .config("spark.some.config.option", "some-value")
        .getOrCreate();

    runLogisticRegression(spark);
  }

  private static void runLogisticRegression(SparkSession spark) {
    Dataset<Row> data = spark.read().format("libsvm")
        .load("src/main/resources/iris_libsvm.txt");

//    data.show();
//    data.printSchema();

    Dataset<Row>[] splits = data.randomSplit(new double[] {0.6, 0.4}, 11L);
    Dataset<Row> training = splits[0];
    Dataset<Row> test = splits[1];

    LogisticRegression lr = new LogisticRegression()
        .setMaxIter(100)
        .setRegParam(0.3)
        .setElasticNetParam(0.8);

    LogisticRegressionModel lrModel = lr.fit(training);
    System.out.println("** Coefficients: "
        + lrModel.coefficients() + " \n** Intercept: " + lrModel.intercept()
        + "\n** Threshold: " + lrModel.getThreshold());

//    System.out.println("predicted = " + lrModel.predict(Vectors.dense(5.1, 3.5, 1.4, 0.2)));
//    System.out.println("predicted = " + lrModel.predict(Vectors.dense(5.7, 2.8, 4.1, 1.3)));

    Dataset<Row> predictions = lrModel.transform(test);
//    predictions.select("prediction", "label", "features").show(10);

    MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
        .setLabelCol("label")
        .setPredictionCol("prediction")
        .setMetricName("accuracy");
    double accuracy = evaluator.evaluate(predictions);
    System.out.println("Accuracy = " + accuracy);

  }
}
