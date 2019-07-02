/**
 * The Country class has 11 different attributes
 * like name, population, areaTotal, etc. And these
 * attributes hold the corresponding statistics.
 */
public class Country {
    private String name;
    private int population;
    private float areaTotal;
    private float areaLand;
    private float areaWater;
    private float median_ageMale;
    private float median_ageFemale;
    private float birth_rate;
    private float death_rate;
    private float literacyFemale;
    private int airports;

    /**
     * Class constructor specifying name,
     * population, total area,
     * land area, water area,
     * median age of male and female,
     * birth and death rate, literacy of
     * female and number of airports.
     */
    public Country(String name, int population, float areaTotal, float areaLand, float areaWater, float median_ageMale, float median_ageFemale, float birth_rate, float death_rate, float literacyFemale, int airports) {
        this.name = name;
        this.population = population;
        this.areaTotal = areaTotal;
        this.areaLand = areaLand;
        this.areaWater = areaWater;
        this.median_ageMale = median_ageMale;
        this.median_ageFemale = median_ageFemale;
        this.birth_rate = birth_rate;
        this.death_rate = death_rate;
        this.literacyFemale = literacyFemale;
        this.airports = airports;
    }

    String getName() {
        return name;
    }

    int getPopulation() {
        return population;
    }


    float getAreaTotal() {
        return areaTotal;
    }


    float getAreaLand() {
        return areaLand;
    }


    float getAreaWater() {
        return areaWater;
    }


    float getMedian_ageFemale() {
        return median_ageFemale;
    }


    float getMedian_ageMale() {
        return median_ageMale;
    }


    float getBirth_rate() {
        return birth_rate;
    }


    float getDeath_rate() {
        return death_rate;
    }


    float getLiteracyFemale() {
        return literacyFemale;
    }

    int getAirports() {
        return airports;
    }

}
