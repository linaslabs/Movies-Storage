package stores;

import java.time.LocalDateTime;

public class Rating {
    private int userID;
    private int movieID;
    private float rating;
    private LocalDateTime timestamp;

    public Rating(int userID, int movieID, float rating, LocalDateTime timestamp){
        this.userID = userID;
        this.movieID = movieID;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public int getUserID(){
        return userID;
    }

    public int getMovieID(){
        return movieID;
    }
    
    public float getRating(){
        return rating;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public boolean updateRating(float newRating, LocalDateTime newTimestamp){
        this.rating = newRating;
        this.timestamp = newTimestamp;
        return true;
    } 
}
