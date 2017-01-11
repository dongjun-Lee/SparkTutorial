#!/bin/sh

mvn clean install && /Users/dongjun/spark-2.0.2-bin-hadoop2.7/bin/spark-submit --class JavaLogisticRegression ./target/spark-tutorial-1.0-SNAPSHOT.jar
