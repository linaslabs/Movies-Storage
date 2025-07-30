import stores.*;
import structures.*;
import utils.*;

public class Testbed {

    public static void main(String args[]) {
        //Add your own code here!
        Stores stores = new Stores();
        Movies movies = new Movies(stores);
        Ratings rating = new Ratings(stores);
        Credits credit = new Credits(stores);

    }
}
