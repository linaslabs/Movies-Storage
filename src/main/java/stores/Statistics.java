package stores;

public class Statistics {
    private int numOfRatings;
    private float sumOfRatings;
    private float averageRating;

    public Statistics(){
        this.numOfRatings = 0;
        this.sumOfRatings = 0;
    }

    public void addRating(float rating){
        this.numOfRatings++;
        this.sumOfRatings += rating;
    }

    public void removeRating(float rating){
        this.numOfRatings--;
        this.sumOfRatings -= rating;
    }

    public void updateRating(float oldRating, float newRating){
        sumOfRatings -= oldRating;
        sumOfRatings += newRating;
    }

    private void updateAverageRating(){
        this.averageRating = this.sumOfRatings / this.numOfRatings;
    }

    public int getNumOfRatings(){
        return this.numOfRatings;
    }

    public float getSumOfRatings(){
        return this.sumOfRatings;
    }

    public float getAverageRating(){
        updateAverageRating();
        return this.averageRating;
    }
}
