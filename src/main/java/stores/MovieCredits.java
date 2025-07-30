package stores;

import structures.CustomMinHeap;
import structures.KeyValuePair;

public class MovieCredits {
    private CastCredit[] castCredits;
    private CrewCredit[] crewCredits;

    /**
     * Constructor for the MovieCredits class
     * 
     * @param castCredits
     * @param crewCredits
     */
    public MovieCredits(CastCredit[] castCredits, CrewCredit[] crewCredits) {
        this.castCredits = castCredits;
        this.crewCredits = crewCredits;
        sortCastCredits();
        sortCrewCredits();
    }

    public CastCredit[] getCastCredits() {
        return castCredits;
    }

    public CrewCredit[] getCrewCredits() {
        return crewCredits;
    }

    /**
     * Function to modify the current castCredits array, and sort it according to
     * order
     */
    private void sortCastCredits() {
        CastCredit[] newCastCredits = new CastCredit[castCredits.length];

        // Sorts via a minimum heap
        CustomMinHeap<Integer> castMinHeap = new CustomMinHeap<>(castCredits.length) {
        };
        // Iterates through the cast credits array, adding each cast credit as a KVP
        // with its order and its position in the array
        for (int oldPos = 0; oldPos < castCredits.length; oldPos++) {
            KeyValuePair<Integer, Integer> castCreditIdentifier = new KeyValuePair<>(castCredits[oldPos].getOrder(), oldPos);
            castMinHeap.add(castCreditIdentifier);
        }

        // The array returned from the minimum heap is the ascending order of cast credits based on their order
        // However the cast credits themselves are represented by their position in the original cast credits array
        int[] newPositions = castMinHeap.getSortedAscendingValues();

        // The array is iterated through. Each element is taken as the index of where to find its
        // corresponding value in the original cast credits array.
        // So the new array represents the original array but in order of order!
        for (int newPos = 0; newPos < newCastCredits.length; newPos++) {
            newCastCredits[newPos] = castCredits[newPositions[newPos]];
        }

        this.castCredits = newCastCredits;
    }

    /**
     * Function to modify the current crewCredits array, and sort it according to ID
     */
    private void sortCrewCredits() {
        CrewCredit[] newCrewCredits = new CrewCredit[crewCredits.length];

        // Sorts via a minimum heap
        CustomMinHeap<Integer> crewMinHeap = new CustomMinHeap<>(crewCredits.length) {
        };
        // Iterates through the crew credits array, adding each crew credit as a KVP
        // with its ID and its position in the array
        for (int oldPos = 0; oldPos < crewCredits.length; oldPos++) {
            KeyValuePair<Integer, Integer> crewCreditIdentifier = new KeyValuePair<>(crewCredits[oldPos].getID(), oldPos);
            crewMinHeap.add(crewCreditIdentifier);
        }

        // The array returned from the minimum heap is the ascending order of cast
        // credits based on their ID
        // However the cast credits themselves are represented by their position in the
        // original crew credits array
        int[] newPositions = crewMinHeap.getSortedAscendingValues();

        // The array is iterated through. Each element is taken as the index of where to
        // find its
        // corresponding value in the original crew credits array.
        // So the new array represents the original array but in order of ID!
        for (int newPos = 0; newPos < newCrewCredits.length; newPos++) {
            newCrewCredits[newPos] = crewCredits[newPositions[newPos]];
        }

        this.crewCredits = newCrewCredits;
    }
}
