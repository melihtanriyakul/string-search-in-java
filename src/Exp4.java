import java.io.*;
import java.util.ArrayList;

public class Exp4 {

    public static void main(String[] args) {
        /** The path of the current directory */
        String currentDir = System.getProperty("user.dir");

        /** Building paths of the files in the argument */
        String dirPath = currentDir + "/" + args[0];
        String inputFilePath = args[1];
        String outputFilePath = args[2];

        /** Reading the files, extracting the info and creating countries into an array list. */
        final File folder = new File(dirPath);
        ArrayList<Country> countries = new ArrayList<>();
        Operations.createCountries(folder, countries);

        /** Reading queries into a string. */
        String queries = Operations.readFile(inputFilePath);

        /** Creating an output writer for printing the results into an output file. */
        PrintWriter outputWriter = Operations.writeToaFile(outputFilePath);

        /** Sorting the statistics of countries and printing the results into an output file. */
        assert queries != null;
        Operations.extractResults(queries, outputWriter, countries);

        /** Closing the output file. */
        assert outputWriter != null;
        outputWriter.close();
    }

}
