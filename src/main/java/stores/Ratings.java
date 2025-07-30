package stores;

import java.time.LocalDateTime;

import interfaces.IRatings;
import structures.*;

public class Ratings implements IRatings {
    Stores stores;

    ChainingHashMap<Integer, ChainingHashMap<Integer, Rating>> userMap;
    ChainingHashMap<Integer, ChainingHashMap<Integer, Rating>> movieMap;
    ChainingHashMap<Integer, Statistics> movieStatsMap;
    ChainingHashMap<Integer, Statistics> userStatsMap;

    int size;

    /**
     * The constructor for the Ratings data store. This is where you should
     * initialise your data structures.
     * 
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Ratings(Stores stores) {
        this.stores = stores;

        // Following two maps map the userID/movieID to a hashmap of the other ID to the
        // rating composed of both ID's
        userMap = new ChainingHashMap<>(2153); // <UserID, HashMap<MovieID,Rating>>
        movieMap = new ChainingHashMap<>(2153); // <MovieID, HashMap<UserID,Rating>>

        // Followign two maps are the user and movie ID's to their statistic objects
        // which contain the average ratings and num of ratings for O(1) retrieval
        userStatsMap = new ChainingHashMap<>(2153); // <UserID, Statistics>
        movieStatsMap = new ChainingHashMap<>(2153); // <MovieID, Statistics>
        size = 0;
    }

    /**
     * Adds a rating to the data structure. The rating is made unique by its user ID
     * and its movie ID
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The rating gave to the film by this user (betweenb 0 and 5
     *                  inclusive)
     * @param timestamp The time at which the rating was made
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int userid, int movieid, float rating, LocalDateTime timestamp) {

        Rating ratingObj = new Rating(userid, movieid, rating, timestamp);

        // If the userMap is null at the userID (there is no current ratings for the user)
        // then add a new (null) rating map and initialise the user in the stats map
        if (userMap.get(userid) == null) {
            ChainingHashMap<Integer, Rating> movieToRatingMap = new ChainingHashMap<>(2153);
            userMap.add(userid, movieToRatingMap);

            userStatsMap.add(userid, new Statistics()); // Adding a new statistics element for this user
        }

        // If the movieMap is null at the movieID (there is no current ratings for the movie)
        // then add a new (null) rating map and initalise the movie in the stats map
        if (movieMap.get(movieid) == null) {
            ChainingHashMap<Integer, Rating> userToRatingMap = new ChainingHashMap<>(2153);
            movieMap.add(movieid, userToRatingMap);

            movieStatsMap.add(movieid, new Statistics()); // Adding a new statistics element for this movie
        }

        // Populates (or adds new ratings to) the ratings hashmaps for both the user and
        // the movie, and likewise the statistics maps for both
        // The condition of the if statement will be false if rating already exists in
        // the tables, since the add functions return false in this case
        if (userMap.get(userid).add(movieid, ratingObj) && movieMap.get(movieid).add(userid, ratingObj)) {
            userStatsMap.get(userid).addRating(rating);
            movieStatsMap.get(movieid).addRating(rating);
            this.size++;
            return true;
        }

        return false; // Same ratings exist, so cant be added

    }

    /**
     * Removes a given rating, using the user ID and the movie ID as the unique
     * identifier
     * 
     * @param userID  The user ID
     * @param movieID The movie ID
     * @return TRUE if the data was removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int userid, int movieid) {
        ChainingHashMap<Integer,Rating> userToRatingMap = userMap.get(userid);
        ChainingHashMap<Integer,Rating> movieToRatingMap = movieMap.get(movieid);

        // If ratings don't exist for the movie or the user, then return false
        if (userToRatingMap == null || movieToRatingMap == null) {
            return false;
        } else if (userToRatingMap.remove(movieid) && movieToRatingMap.remove(userid)) { // Check if the removal returns true
            // Then removes from the stats maps too
            userStatsMap.remove(userid);
            movieStatsMap.remove(movieid);
            this.size--;
            return true;
        }

        return false; // If ratings not found, then return fase
        
    }

    /**
     * Sets a rating for a given user ID and movie ID. Therefore, should the given
     * user have already rated the given movie, the new data should overwrite the
     * existing rating. However, if the given user has not already rated the given
     * movie, then this rating should be added to the data structure
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The new rating to be given to the film by this user (between
     *                  0 and 5 inclusive)
     * @param timestamp The time at which the new rating was made
     * @return TRUE if the data able to be added/updated, FALSE otherwise
     */
    @Override
    public boolean set(int userid, int movieid, float rating, LocalDateTime timestamp) {

        if (add(userid, movieid, rating, timestamp)) { // Adds the rating or user/movie keys if not present
            return true;
        }

        Rating ratingObj = userMap.get(userid).get(movieid); // Creates rating object
        userStatsMap.get(userid).updateRating(ratingObj.getRating(), rating); // Update the stats for the user
        movieStatsMap.get(movieid).updateRating(ratingObj.getRating(), rating); // Update the stats for the movie

        return (movieMap.get(movieid).get(userid).updateRating(rating, timestamp)
                && ratingObj.updateRating(rating, timestamp)); // Updates the ratings in user and movie maps

    }

    /**
     * Get all the ratings for a given film
     * 
     * @param movieID The movie ID
     * @return An array of ratings. If there are no ratings or the film cannot be
     *         found in Ratings, then return an empty array
     */
    @Override
    public float[] getMovieRatings(int movieid) {

        ChainingHashMap<Integer, Rating> movieToRatingMap = movieMap.get(movieid);

        // Checks if there are no ratings for the movie, or movie doesn't exist
        if (movieToRatingMap == null) {
            return new float[0];
        }

        CustomArrayList<Float> ratingList = new CustomArrayList<>();
        // Iterates through the rating map corresponding to the movieID, and compiles
        // the user ratings into one list
        for (int i = 0; i < movieToRatingMap.getCapacity(); i++) {
            ListElement<Integer, Rating> element = movieToRatingMap.getHead(i);
            while (element != null) {
                ratingList.add(element.getValue().getRating());
                element = element.getNext();
            }
        }

        return ratingList.getAsArrayFloat();
    }

    /**
     * Get all the ratings for a given user
     * 
     * @param userID The user ID
     * @return An array of ratings. If there are no ratings or the user cannot be
     *         found in Ratings, then return an empty array
     */
    @Override
    public float[] getUserRatings(int userid) {

        ChainingHashMap<Integer, Rating> userToRatingMap = userMap.get(userid);

        // Checks if there are no ratings for the user, or user doesn't exist
        if (userToRatingMap == null) {
            return new float[0];
        }

        CustomArrayList<Float> ratingList = new CustomArrayList<>();
        // Iterates through the rating map corresponding to the userID, and compiles the
        // mpvie ratings into one list
        for (int i = 0; i < userToRatingMap.getCapacity(); i++) { // capacity, because items could be spread out
            ListElement<Integer, Rating> element = userToRatingMap.getHead(i);
            while (element != null) {
                ratingList.add(element.getValue().getRating());
                element = element.getNext();
            }
        }

        return ratingList.getAsArrayFloat();
    }

    /**
     * Get the average rating for a given film
     * 
     * @param movieID The movie ID
     * @return Produces the average rating for a given film.
     *         If the film cannot be found in Ratings, but does exist in the Movies
     *         store, return 0.0f.
     *         If the film cannot be found in Ratings or Movies stores, return
     *         -1.0f.
     */
    @Override
    public float getMovieAverageRating(int movieid) {

        if (movieMap.get(movieid) == null) {
            if (stores.getMovies().getTitle(movieid) == null) { // Getting the title tells you if it exists (could be another attribute)
                return -1.0f;
            }
            
            return 0.0f;
        }

        return movieStatsMap.get(movieid).getAverageRating();
    }

    /**
     * Get the average rating for a given user
     * 
     * @param userID The user ID
     * @return Produces the average rating for a given user. If the user cannot be
     *         found in Ratings, or there are no rating, return -1.0f
     */
    @Override
    public float getUserAverageRating(int userid) {

        // Checks if the user has a rating map, or if the user even exists in the map to start with
        if (userMap.get(userid) == null) {
            return -1.0f;
        }

        return userStatsMap.get(userid).getAverageRating();
    }

    /**
     * Gets the top N movies with the most ratings, in order from most to least
     * 
     * @param num The number of movies that should be returned
     * @return A sorted array of movie IDs with the most ratings. The array should
     *         be
     *         no larger than num. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies in
     *         Ratings
     */
    @Override
    public int[] getMostRatedMovies(int num) {
        // Creates a minheap with each "node" being KeyValuePair<Integer (numOfRatings), Integer (movieID)>
        CustomMinHeap<Integer> minHeap = new CustomMinHeap<>(num);

        // Iterates through the movies statistics map which maps every movie to a
        // statistics object (which includes the num of ratings and average rating)
        // Builds a min heap of "num" values as it iterates along containing the highest
        // "num" values
        for (int i = 0; i < movieStatsMap.getCapacity(); i++) {
            ListElement<Integer, Statistics> element = movieStatsMap.getHead(i); // Gets the head in case there are
                                                                                 // multiple elements in this bucket

            while (element != null) {
                KeyValuePair<Integer, Integer> elementDetails = new KeyValuePair<>(element.getValue().getNumOfRatings(),
                        element.getKey()); // Gets the <numOfRatings, movieID> as a KVP
                if (!minHeap.isFull()) { // If not full or is empty, add the element!
                    minHeap.add(elementDetails);
                } else if (elementDetails.compareTo(minHeap.peek()) > 0) { // Heap full - Compares the numberOfRatings
                                                                           // of current movie element, with minimum on
                                                                           // the heap so far
                    // Pops the minimum and adds the new element if condition is satisfied
                    minHeap.popRoot();
                    minHeap.add(elementDetails);
                }
                element = element.getNext();
            }
        }

        return minHeap.getSortedDescendingValues(); // Returns the sorted values in descending order of the heap
    }

    /**
     * Gets the top N users with the most ratings, in order from most to least
     * 
     * @param num The number of users that should be returned
     * @return A sorted array of user IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num users in the store,
     *         then the array should be the same length as the number of users in
     *         Ratings
     */
    @Override
    public int[] getMostRatedUsers(int num) {
        // Creates a minheap with each "node" being KeyValuePair<Integer (numOfRatings),Integer (userID)>
        CustomMinHeap<Integer> minHeap = new CustomMinHeap<>(num);

        // Iterates through the user statistics map which maps every user to a
        // statistics object (which includes the num of ratings and average rating)
        // Builds a min heap of "num" values as it iterates along containing the highest "num" values
        for (int i = 0; i < userStatsMap.getCapacity(); i++) {
            ListElement<Integer, Statistics> element = userStatsMap.getHead(i);

            while (element != null) {
                KeyValuePair<Integer, Integer> elementDetails = new KeyValuePair<>(element.getValue().getNumOfRatings(),
                        element.getKey()); // Gets the <numOfRatings, userID> as a KVP
                if (!minHeap.isFull()) { // If not full or is empty, add the element!
                    minHeap.add(elementDetails);
                } else if (elementDetails.compareTo(minHeap.peek()) > 0) { // Heap full - Compares the numberOfRatings
                                                                           // of current user element, with minimum on
                                                                           // the heap so far
                    // Pops the minimum and adds the new element if condition is satisfied
                    minHeap.popRoot();
                    minHeap.add(elementDetails);
                }
                element = element.getNext();
            }
        }

        return minHeap.getSortedDescendingValues(); // Returns the sorted values in descending order of the heap
    }

    /**
     * Get the number of ratings that a movie has
     * 
     * @param movieid The movie id to be found
     * @return The number of ratings the specified movie has.
     *         If the movie exists in the Movies store, but there are no ratings for
     *         it, then return 0.
     *         If the movie does not exist in the Ratings or Movies store, then
     *         return -1.
     */
    @Override
    public int getNumRatings(int movieid) {

        // Getting the title tells you if it exists
        if (stores.getMovies().getTitle(movieid) != null) {
            if (movieMap.get(movieid) == null) { // If it exists in movies store but no ratings
                return 0;
            }
        }

        // Can be the case that there may be a movie not in the movies store, but in the ratings structure... (according to tests)
        // Following checks if the movieID has ratings in the ratings store
        if (movieMap.get(movieid) != null) {
            return movieStatsMap.get(movieid).getNumOfRatings();
        }

        return -1;
    }

    /**
     * Get the highest average rated film IDs, in order of there average rating
     * (hightst first).
     * 
     * @param numResults The maximum number of results to be returned
     * @return An array of the film IDs with the highest average ratings, highest
     *         first. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies in
     *         Ratings
     */
    @Override
    public int[] getTopAverageRatedMovies(int numResults) {
        // Creates a minheap with each "node" being KeyValuePair<Integer (numOfRatings), sInteger (movieID)>
        CustomMinHeap<Float> minHeap = new CustomMinHeap<>(numResults);

        // Iterates through the movies statistics map which maps every movie to a
        // statistics object (which includes the num of ratings and average rating)
        // Builds a min heap of "numResults" values as it iterates along containing the highest "numResults" values
        for (int i = 0; i < movieStatsMap.getCapacity(); i++) {
            ListElement<Integer, Statistics> element = movieStatsMap.getHead(i);

            while (element != null) {
                KeyValuePair<Float, Integer> elementDetails = new KeyValuePair<>(element.getValue().getAverageRating(),
                        element.getKey()); // Gets the <averageRating, movieID> as a KVP
                if (!minHeap.isFull()) { // If not full or is empty, add the element!
                    minHeap.add(elementDetails);
                } else if (elementDetails.compareTo(minHeap.peek()) > 0) { // Heap full - Compares the averateRating of
                                                                           // current movie element, with minimum on the
                                                                           // heap so far
                    // Pops the minimum and adds the new element if condition is satisfied
                    minHeap.popRoot();
                    minHeap.add(elementDetails);
                }
                element = element.getNext();
            }
        }

        return minHeap.getSortedDescendingValues(); // Returns the sorted values in descending order of the heap
    }

    /**
     * Gets the number of ratings in the data structure
     * 
     * @return The number of ratings in the data structure
     */
    @Override
    public int size() {
        return this.size; // Size is updated whenever a rating is added or removed
    }
}
