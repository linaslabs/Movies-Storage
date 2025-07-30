package stores;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private String tagline;
    private String status;
    private Genre[] genres;
    private LocalDate release;
    private long budget;
    private long revenue;
    private String[] languages;
    private String originalLanguage;
    private double runtime;
    private String homepage;
    private boolean adult;
    private boolean video;
    private String poster;

    private int voteCount;
    private double voteAverage;
    private int collectionID;
    private String imdbID;
    private double popularity;
    private Company[] companies;
    private int nextFreeCompanyPos = 0;
    private String[] countries;
    private int nextFreeCountryPos = 0;

    /**
     * Constructor for the Movie class
     */
    public Movie(int id, String title, String originalTitle, String overview, String tagline, String status,
            Genre[] genres, LocalDate release, long budget,
            long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult,
            boolean video, String poster) {

        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.genres = genres;
        this.release = release;
        this.budget = budget;
        this.revenue = revenue;
        this.languages = languages;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.homepage = homepage;
        this.adult = adult;
        this.video = video;
        this.poster = poster;
        
        this.companies = new Company[0];
        this.countries = new String[0];
    }

    /**
     * Function to set the new vote statistics of the movie
     * 
     * @param newVoteCount
     * @param newVoteAverage
     */
    public void setVote(int newVoteCount, double newVoteAverage) {
        this.voteCount = newVoteCount;
        this.voteAverage = newVoteAverage;
    }

    /**
     * Function to set the new IMDBID of the movie
     * 
     * @param newIMDBID
     */
    public void setIMDBID(String newIMDBID) {
        this.imdbID = newIMDBID;
    }

    /**
     * Function to set the new popularity of the movie
     * 
     * @param newPopularity
     */
    public void setPopularity(double newPopularity) {
        this.popularity = newPopularity;
    }

    /**
     * Function to set the new collectionID of the movie
     * 
     * @param newCollectionID
     */
    public void setCollectionID(int newCollectionID) {
        this.collectionID = newCollectionID;
    }

    /**
     * Function to add a new company to the companies array in the movie
     * 
     * @param newCompany
     */
    public void addCompany(Company newCompany) {
        
        if (this.nextFreeCompanyPos == this.companies.length){
            Company[] newCompanies = new Company[(this.companies.length == 0) ? this.companies.length + 1 : this.companies.length * 2];
            System.arraycopy(this.companies, 0, newCompanies, 0, this.companies.length);
            this.companies = newCompanies;
        }

        this.companies[this.nextFreeCompanyPos++] = newCompany;
    }

    /**
     * Function to add a new countrie to the countries array in the movie
     * 
     * @param newCountry
     */
    public void addCountry(String newCountry) {
        
        if (this.nextFreeCountryPos == this.countries.length){
            String[] newCountries = new String[(this.countries.length == 0) ? this.countries.length + 1 : this.countries.length * 2];
            System.arraycopy(this.countries, 0, newCountries, 0, this.countries.length);
            this.countries = newCountries;
        }

        this.countries[this.nextFreeCountryPos++] = newCountry;

    }

    public int getID() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getOrginalTitle() {
        return this.originalTitle;
    }

    public String getOverview() {
        return this.overview;
    }

    public String getTagline() {
        return this.tagline;
    }

    public String getStatus() {
        return this.status;
    }

    public Genre[] getGenres() {
        return this.genres;
    }

    public LocalDate getRelease() {
        return this.release;
    }

    public long getBudget() {
        return this.budget;
    }

    public long getRevenue() {
        return this.revenue;
    }

    public String[] getLanguages() {
        return this.languages;
    }

    public String getOriginalLanguage() {
        return this.originalLanguage;
    }

    public double getRuntime() {
        return this.runtime;
    }

    public String getHomepage() {
        return this.homepage;
    }

    public boolean getAdult() {
        return this.adult;
    }

    public boolean getVideo() {
        return this.video;
    }

    public String getPoster() {
        return this.poster;
    }

    public int getVoteCount() {
        return this.voteCount;
    }

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public int getCollectionID() {
        return this.collectionID;
    }

    public String getIMDBID() {
        return this.imdbID;
    }

    public double getPopularity() {
        return this.popularity;
    }

    public Company[] getCompanies() {
        return this.companies;
    }

    public String[] getCountries() {
        return this.countries;
    }
}
