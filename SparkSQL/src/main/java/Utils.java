import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.List;

public class Utils {
  public static void csvToLibsvm(String inputPath, String outputPath) {
    try {
      CSVReader reader = new CSVReader(new FileReader(inputPath));
      List<String[]> data = reader.readAll();

      String[] columnNames = data.get(0);
      data.remove(0);

      FileWriter outputFile = new FileWriter(outputPath, false);
      BufferedWriter fileWriter = new BufferedWriter(outputFile);
      for (String[] line : data) {
        fileWriter.write(line[4]);

        for (int i=0; i<line.length-1; i++) {
          if (Float.parseFloat(line[i]) != 0) {
            fileWriter.write(" " + (i+1) + ":" + line[i]);
          }
        }
        fileWriter.write("\n");
      }

      fileWriter.close();
      outputFile.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
