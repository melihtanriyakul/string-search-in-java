import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Operations class has all the methods to
 * read and search through files, extract
 * specific statistics and create countries
 * out of these statistics, then sort these
 * countries with respect to these statistics
 * and obtain the countries with respect to the
 * queries requested.
 */
public class Operations {

    /**
     * Takes a file name as an argument and reads
     * the file into a String.
     *
     * @param fileName the name of the file
     * @return String  the string, containing the file content
     */
    static String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Opens the given output file. If there is no file
     * with the given file name then creates a new one.
     *
     * @param fileName the name of the file
     * @return PrintWriter    the print writer to write output file
     */
    static PrintWriter writeToaFile(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter buffWriter = new BufferedWriter(fw);

            return new PrintWriter(buffWriter);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Takes the folder which contains files that contains
     * data about the countries and an array list called
     * countries. Then reads the files sequentially and
     * collect the information of each country and creates
     * a countries object with the designated statistics
     * and adds this country object into the array list,
     * countries.
     *
     * @param folder    the folder which consists of the files that contains the information about countries
     * @param countries the array list which contains the country objects
     */
    static void createCountries(final File folder, ArrayList<Country> countries) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                createCountries(fileEntry, countries);
            } else {
                try {
                    String name = extractStatistic(fileEntry, "conventional short form");
                    int population = Integer.parseInt(extractStatistic(fileEntry, "Population"));
                    float areaTotal = Float.parseFloat(extractStatistic(fileEntry, "Area-total"));
                    float areaLand = Float.parseFloat(extractStatistic(fileEntry, "Area-land"));
                    float areaWater = Float.parseFloat(extractStatistic(fileEntry, "Area-water"));
                    float median_ageMale = Float.parseFloat(extractStatistic(fileEntry, "Median_age-male"));
                    float median_ageFemale = Float.parseFloat(extractStatistic(fileEntry, "Median_age-female"));
                    float birthRate = Float.parseFloat(extractStatistic(fileEntry, "Birth_rate"));
                    float deathRate = Float.parseFloat(extractStatistic(fileEntry, "Death_rate"));
                    float literacyFemale = Float.parseFloat(extractStatistic(fileEntry, "Literacy-female"));
                    int airports = Integer.parseInt(extractStatistic(fileEntry, "Airports"));

                    countries.add(new Country(name, population, areaTotal, areaLand, areaWater, median_ageMale,
                            median_ageFemale, birthRate, deathRate, literacyFemale, airports));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Takes a file that contains the data of a country and
     * a keyword. Then searches the keyword in the file
     * via Boyer Moore Search and extracts the statistic
     * of the keyword like 'population' or 'airports' and
     * returns it as a string.
     *
     * @param fileEntry The file which contains the data of a country
     * @param keyword   The keyword to be searched for a statistic like 'population' or 'airports'
     * @return returnVal a value of the statistic which is extracted with respect to the keyword
     */
    private static String extractStatistic(File fileEntry, String keyword) throws IOException {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(fileEntry);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**
         * Checks if the keyword has two parts.
         * String methods 'replaceAll' and 'split'
         * used here to obtain two parts keywords.
         * */
        keyword = keyword.replaceAll("_", " ");
        String[] keyList = keyword.split("-");

        String firstKeyword = keyList[0];
        String secondKeyword = "";
        boolean firstKeywordFound = false;
        boolean secondKeywordFound = true;

        /**
         *  If keyList has the length greater than 1
         * then we have a second keyword to look for.
         * */
        if (keyList.length > 1) {
            secondKeyword = keyList[1];
            secondKeywordFound = false;
        }
        String returnVal = "-1";

        try {
            String line;
            while ((line = br.readLine()) != null) {
                /** Searching for the first keyword and the second, if there is one via Boyer Moore Search. */
                if (!firstKeywordFound) {
                    firstKeywordFound = BoyerMoore.search(line.toCharArray(), ("\"" + firstKeyword + "\"").toCharArray());
                } else if (!secondKeywordFound) {
                    secondKeywordFound = BoyerMoore.search(line.toCharArray(), ("\"" + secondKeyword + "\"").toCharArray());
                } else {
                    /** Searching in the text if there is 'million' in written. */
                    boolean millionInWritten = false;
                    if (BoyerMoore.search(line.toCharArray(), "million".toCharArray())) {
                        millionInWritten = true;
                    }
                    /** Extracting the number or the country name by using 'extractInfo' method. */
                    line = extractInfo(line);

                    /** If 'millionInWritten' found above then we multiply the number by million. */
                    if (millionInWritten) {
                        float valueWithMillion = Float.parseFloat(line);
                        valueWithMillion *= 1000000;
                        line = Integer.toString((int) valueWithMillion);
                    }
                    returnVal = line;

                    firstKeywordFound = false;
                    secondKeywordFound = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return returnVal;
    }

    /**
     * Takes a line from the file as an argument
     * and extracts either the country name or
     * a statistic from the line.
     *
     * @param line a line from the file
     * @return info  either the name of the country or any statistic of the country
     */
    private static String extractInfo(String line) {
        StringBuilder info = new StringBuilder();
        char[] lineInChars = line.toCharArray();
        boolean doubleDotSeen = false;
        boolean quoteSeen = false;
        boolean isNumber = false;


        for (char i : lineInChars) {
            if (!doubleDotSeen && i == ':') {
                doubleDotSeen = true;
            } else if (doubleDotSeen && !quoteSeen && i == '"') {
                quoteSeen = true;
            } else if (doubleDotSeen && quoteSeen) {
                if (Character.isDigit(i)) {
                    isNumber = true;
                    info.append(i);
                } else if (isNumber && Character.isWhitespace(i)) {
                    break;
                } else if (Character.isAlphabetic(i) || Character.isWhitespace(i) || i == '.') {
                    info.append(i);
                }
            }
        }

        return info.toString();
    }


    /**
     * Takes a statistic keyword and an array list of countries.
     * Builds a Binary Search Tree of statistics with respect to
     * the given keyword and sorts the countries and their statistics
     * into a new array list, statisticList and returns it.
     *
     * @param statisticKeyword a statistic keyword of countries like popularity, death rate.
     * @param countries        an array list which consists of the country objects
     * @return statisticList    an array list of ascendingly sorted statistics of countries.
     */
    private static ArrayList<Node> sortStatistic(String statisticKeyword, ArrayList<Country> countries) {
        BST statisticTree = new BST();
        ArrayList<Node> statisticList = new ArrayList<>();

        for (Country currentCountry : countries) {
            switch (statisticKeyword) {
                case "population":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getPopulation());
                    break;
                case "area-total":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getAreaTotal());
                    break;
                case "area-land":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getAreaLand());
                    break;
                case "area-water":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getAreaWater());
                    break;
                case "median_age-male":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getMedian_ageMale());
                    break;
                case "median_age-female":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getMedian_ageFemale());
                    break;
                case "birth_rate":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getBirth_rate());
                    break;
                case "death_rate":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getDeath_rate());
                    break;
                case "literacy-female":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getLiteracyFemale());
                    break;
                case "airports":
                    statisticTree.insert(currentCountry.getName(), currentCountry.getAirports());
                    break;
                default:
            }
        }
        statisticTree.inorderTraverse(statisticTree.root, statisticList);

        return statisticList;
    }

    /**
     * Takes queries, outputWriter and an array list which consists of country objects
     * as arguments. Firstly, sorts the countries with respect to each statistic given
     * and insert them into array lists like 'populationSorted', 'areaTotalSorted', etc.
     * Then reads the queries which can be like 'four countries with the highest
     * population' and obtains the requested countries, then remove the duplicates and
     * finally sorts the countries alphabetically, write to the output file by using
     * outputWriter.
     *
     * @param queries      the list of queries to be executed, like 'four countries with the highest population
     * @param outputWriter the print writer to be used to write to an output file
     * @param countries    an array list of country objects
     */
    static void extractResults(String queries, PrintWriter outputWriter, ArrayList<Country> countries) {
        ArrayList<Node> populationSorted = sortStatistic("population", countries);
        ArrayList<Node> areaTotalSorted = sortStatistic("area-total", countries);
        ArrayList<Node> areaLandSorted = sortStatistic("area-land", countries);
        ArrayList<Node> areaWaterSorted = sortStatistic("area-water", countries);
        ArrayList<Node> medianAgeMaleSorted = sortStatistic("median_age-male", countries);
        ArrayList<Node> medianAgeFemaleSorted = sortStatistic("median_age-female", countries);
        ArrayList<Node> birthRateSorted = sortStatistic("birth_rate", countries);
        ArrayList<Node> deathRateSorted = sortStatistic("death_rate", countries);
        ArrayList<Node> literacyFemaleSorted = sortStatistic("literacy-female", countries);
        ArrayList<Node> airportsSorted = sortStatistic("airports", countries);

        if (!queries.equals("")) {
            String[] lines = queries.split("\\r?\\n");

            for (String line : lines) {
                ArrayList<String> selectedCountries = new ArrayList<>();
                String[] splitLine = line.split("\\+");
                for (String currentStat : splitLine) {
                    String[] parts = currentStat.split(",");
                    String field = parts[0];
                    String topOrLast = parts[1];
                    int number = Integer.parseInt(parts[2]);
                    ArrayList<String> returnedCountries;

                    switch (field) {
                        case "population":
                            returnedCountries = extractTopOrLastCountries(populationSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "area-total":
                            returnedCountries = extractTopOrLastCountries(areaTotalSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "area-land":
                            returnedCountries = extractTopOrLastCountries(areaLandSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "area-water":
                            returnedCountries = extractTopOrLastCountries(areaWaterSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "median_age-male":
                            returnedCountries = extractTopOrLastCountries(medianAgeMaleSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "median_age-female":
                            returnedCountries = extractTopOrLastCountries(medianAgeFemaleSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "birth_rate":
                            returnedCountries = extractTopOrLastCountries(birthRateSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "death_rate":
                            returnedCountries = extractTopOrLastCountries(deathRateSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "literacy-female":
                            returnedCountries = extractTopOrLastCountries(literacyFemaleSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        case "airports":
                            returnedCountries = extractTopOrLastCountries(airportsSorted, topOrLast, number);
                            selectedCountries.addAll(returnedCountries);
                            break;
                        default:
                    }
                }

                /** Removing duplicates from the array list. */
                for (int i = 0; i < selectedCountries.size(); i++) {
                    for (int j = i + 1; j < selectedCountries.size(); j++) {
                        if (selectedCountries.get(i).equals(selectedCountries.get(j))) {
                            selectedCountries.remove(selectedCountries.get(j));
                        }
                    }
                }

                /** Sorting the countries alphabetically. */
                String[] sortedSelectedCountries = selectedCountries.toArray(new String[selectedCountries.size()]);
                Arrays.sort(sortedSelectedCountries);

                /** Writing to the countries to the output file. */
                String output = "[" + String.join(", ", sortedSelectedCountries) + "]";
                outputWriter.println(output);
            }
        }
    }

    /**
     * Extracts the designated number of top or last countries
     * and adds them into an array list and returns it.
     *
     * @param listSorted the array list of sorted countries
     * @param topOrLast  the keyword to be used to get either top countries or last countries
     * @param number     the number that designates how many countries to be obtained
     * @return selectedCountries the selected countries with respect to the topOrLast and number
     */
    private static ArrayList<String> extractTopOrLastCountries(ArrayList<Node> listSorted, String topOrLast, int number) {
        ArrayList<String> selectedCountries = new ArrayList<>();

        if (topOrLast.equals("last")) {
            for (int i = 0; i < number; i++) {
                selectedCountries.add(listSorted.get(i).getKey());
            }
        } else {
            for (int i = listSorted.size() - 1; i > listSorted.size() - (number + 1); i--) {
                selectedCountries.add(listSorted.get(i).getKey());
            }
        }

        return selectedCountries;
    }
}
