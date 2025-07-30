package stores;

public class CrewData {
    private Person person;
    private int[] movies;
    private int nextFreeMoviePos = 1;

    /**
     * Constructor for the CrewData class
     * 
     * @param person
     * @param movies
     */
    public CrewData(Person person, int[] movies) {
        this.person = person;
        this.movies = movies;
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

    public Person getPerson() {
        return this.person;
    }

    public int[] getMovies() {
        return this.movies;
    }
}
