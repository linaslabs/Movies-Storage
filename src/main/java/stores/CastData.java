package stores;

public class CastData {
    private Person person;
    private int[] movies;
    private int nextFreeMoviePos = 1;
    private int[] moviesTop3;
    private int nextFreeTop3Pos;
    private int totalCredits;

    /**
     * Constructor for the CastData class
     * 
     * @param person
     * @param movies
     * @param moviesTop3
     * @param totalCredits
     */
    public CastData(Person person, int[] movies, int[] moviesTop3, int totalCredits) {
        this.person = person;
        this.movies = movies;
        this.moviesTop3 = moviesTop3;
        this.totalCredits = totalCredits;
        this.nextFreeTop3Pos = moviesTop3.length;
    }

    /**
     * Method to add a given movie ID to a movie array
     * 
     * @param movieID
     */
    public void addMovie(int movieID) {

        // Check to see if the movieID is already present in array
        for (int movie : this.movies) {
            if (movie == movieID) {
                return;
            }
        }

        // If not present, adds the movie to the array
        if (this.nextFreeMoviePos == this.movies.length) {
            int[] newMovies = new int[this.movies.length * 2];
            System.arraycopy(this.movies, 0, newMovies, 0, this.movies.length);
            this.movies = newMovies;
        }

        this.movies[this.nextFreeMoviePos++] = movieID;
    }

    /**
     * Method to add a given movieID to a top 3 movie array
     * 
     * @param movieID
     */
    public void addMovieTop3(int movieID) {

        // Check to see if the movieID is already present in array
        for (int movie : this.moviesTop3) {
            if (movie == movieID) {
                return;
            }
        }

        // If not present, adds the movie to the array
        if (this.nextFreeTop3Pos == this.moviesTop3.length) {
            int[] newMoviesTop3 = new int[(this.moviesTop3.length == 0) ? this.moviesTop3.length + 1
                    : this.moviesTop3.length * 2];
            System.arraycopy(this.moviesTop3, 0, newMoviesTop3, 0, this.moviesTop3.length);
            this.moviesTop3 = newMoviesTop3;
        }
        this.moviesTop3[this.nextFreeTop3Pos++] = movieID;
    }

    public void addCredit() {
        this.totalCredits += 1;
    }

    public void removeCredit() {
        this.totalCredits -= 1;
    }

    /**
     * Method to remove a movie from the movie array
     * 
     * @param movieID
     */
    public void removeMovie(int movieID) {
        int[] newMoviesArray = new int[this.movies.length - 1];
        int index = 0;
        for (int movie : this.movies) {
            if (movie == movieID) {
                continue;
            } else {
                newMoviesArray[index++] = movie;
            }
        }

        this.movies = newMoviesArray;
        this.nextFreeMoviePos--;
    }

    /**
     * Method to remove a movie from the top 3 movie array
     * 
     * @param movieID
     */
    public void removeTop3Movie(int movieID) {
        int[] newTop3MoviesArray = new int[this.moviesTop3.length - 1];
        int index = 0;
        for (int movie : this.moviesTop3) {
            if (movie == movieID) {
                continue;
            } else {
                newTop3MoviesArray[index++] = movie;
            }
        }
        this.moviesTop3 = newTop3MoviesArray;
        this.nextFreeTop3Pos--;
    }

    public Person getPerson() {
        return this.person;
    }

    public int[] getMovies() {
        return this.movies;
    }

    public int[] getMoviesTop3() {
        return this.moviesTop3;
    }

    public int getTotalCredits() {
        return this.totalCredits;
    }
}
