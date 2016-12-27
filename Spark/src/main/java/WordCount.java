import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCount {

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("Usage: WordCount <file>");
      System.exit(1);
    }

    SparkConf sparkConf = new SparkConf().setAppName("WordCount");
    JavaSparkContext sc = new JavaSparkContext(sparkConf);

    JavaRDD<String> lines = sc.textFile(args[0]);
    JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
    JavaPairRDD<String, Integer> ones = words.mapToPair(word -> new Tuple2(word, 1));
    JavaPairRDD<String, Integer> counts = ones.reduceByKey((Integer a, Integer b) -> a+b);

    counts.foreach(s -> System.out.println(s));
    counts.saveAsTextFile("/Users/dongjun/git/SparkTutorial/Output/WordCount");
  }

}
