package stores;

import structures.*;

import interfaces.ICredits;

public class Credits implements ICredits {
    Stores stores;

    ChainingHashMap<Integer, MovieCredits> movieCreditsMap;
    ChainingHashMap<Integer, CastData> castDataMap;
    ChainingHashMap<Integer, CrewData> crewDataMap;

    int size;

    /**
     * The constructor for the Credits data store. This is where you should
     * initialise your data structures.
     * 
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Credits(Stores stores) {
        this.stores = stores;

        movieCreditsMap = new ChainingHashMap<>(2153);
        castDataMap = new ChainingHashMap<>(2153);
        crewDataMap = new ChainingHashMap<>(2153);

        size = 0;
    }

    /**
     * Adds data about the people who worked on a given film. The movie ID should be
     * unique
     * 
     * @param cast An array of all cast members that starred in the given film
     * @param crew An array of all crew members that worked on a given film
     * @param id   The (unique) movie ID
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int id) {

        if (movieCreditsMap.add(id, new MovieCredits(cast, crew))) { // Checks if the film is unique

            // Iterates through the cast members in the cast array
            for (CastCredit castCredit : cast) {
                int castID = castCredit.getID();
                CastData castData = castDataMap.get(castID);
                
                // If the (unique) cast member doesn't exist in the cast data map, add it in
                if (castData == null) {
                    castDataMap.add(castID, new CastData(new Person(castID, castCredit.getName(), castCredit.getProfilePath()),
                                    new int[] { id }, (castCredit.getOrder() <= 3) ? new int[] { id } : new int[0], 1));
                } else { // If the cast member already exists, add the movie id and a credit (the movie arrays deal with duplicates)
                    castData.addMovie(id);
                    if (castCredit.getOrder() <= 3) castData.addMovieTop3(id);
                    castData.addCredit();
                }
            }

            // Iterates through the crew members in the crew array
            for (CrewCredit crewCredit : crew) {
                int crewID = crewCredit.getID();
                CrewData crewData = crewDataMap.get(crewID);

                // If the (unique) crew member doesn't exist in the crew data map, add it in
                if (crewData == null) {
                    crewDataMap.add(crewID, new CrewData(
                            new Person(crewID, crewCredit.getName(), crewCredit.getProfilePath()), new int[] { id }));
                } else { // Add the movie id if already exists
                    crewData.addMovie(id);
                }
            }

            size++;
            return true;
        }
        
        return false;
        

    }

    /**
     * Remove a given films data from the data structure
     * 
     * @param id The movie ID
     * @return TRUE if the data was removed, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        MovieCredits movieCredits = movieCreditsMap.get(id);

        // If the id is found in the movie map, and thus the id is removed, remove the references of the removed movie from each cast and crew
        if (movieCreditsMap.remove(id)) {
            CastCredit[] castCredits = movieCredits.getCastCredits();
            CrewCredit[] crewCredits = movieCredits.getCrewCredits();

            // Iterates through the cast array
            for (CastCredit castCredit : castCredits) {
                CastData castData = castDataMap.get(castCredit.getID());
                
                // Removes the movie id reference from the cast's movie array
                castData.removeMovie(id);
                // Removes from the top3movie array if necessary
                if (castCredit.getOrder() <= 3) {
                    castData.removeTop3Movie(id);
                }
                // Removes the movie credit
                castData.removeCredit();
            }

            // Iterates through the crew array and removes the movie references
            for (CrewCredit crewCredit : crewCredits) {
                crewDataMap.get(crewCredit.getID()).removeMovie(id);
            }

            size--;
            return true;
        }

        return false;
    }

    /**
     * Gets all the cast members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CastCredit objects, one for each member of cast that is
     *         in the given film. The cast members should be in "order" order. If
     *         there is no cast members attached to a film, or the film cannot be
     *         found in Credits, then return an empty array
     */
    @Override
    public CastCredit[] getFilmCast(int filmID) {

        MovieCredits movieCredits = movieCreditsMap.get(filmID);

        if (movieCredits == null) {
            return new CastCredit[0];
        }

        return movieCredits.getCastCredits();
    }

    /**
     * Gets all the crew members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CrewCredit objects, one for each member of crew that is
     *         in the given film. The crew members should be in "id" order (not
     *         "elementID"). If there
     *         is no crew members attached to a film, or the film cannot be found in
     *         Credits,
     *         then return an empty array
     */
    @Override
    public CrewCredit[] getFilmCrew(int filmID) {

        MovieCredits movieCredits = movieCreditsMap.get(filmID);

        if (movieCredits == null) {
            return new CrewCredit[0];
        }

        return movieCredits.getCrewCredits();
    }

    /**
     * Gets the number of cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {

        MovieCredits movieCredits = movieCreditsMap.get(filmID);

        if (movieCredits == null) {
            return -1;
        }

        return movieCredits.getCastCredits().length;
    }

    /**
     * Gets the number of crew that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of crew member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCrew(int filmID) {

        MovieCredits movieCredits = movieCreditsMap.get(filmID);

        if (movieCredits == null) {
            return -1;
        }

        return movieCredits.getCrewCredits().length;
    }

    /**
     * Gets a list of all unique cast members present in the data structure
     * 
     * @return An array of all unique cast members as Person objects. If there are
     *         no cast members, then return an empty array
     */
    @Override
    public Person[] getUniqueCast() {
        // Creates an arraylist to easily accumulate new cast people
        CustomArrayList<Person> castList = new CustomArrayList<>();

        // Iterates through the linked list buckets in the cast map to find all the people in it
        for (int i = 0; i < castDataMap.getCapacity(); i++) {
            ListElement<Integer, CastData> element = castDataMap.getHead(i);
            while (element != null) {
                castList.add(element.getValue().getPerson());
                element = element.getNext();
            }
        }
        
        return castList.getAsArrayPerson(); // Returns the arraylist as a list of Persons
    }

    /**
     * Gets a list of all unique crew members present in the data structure
     * 
     * @return An array of all unique crew members as Person objects. If there are
     *         no crew members, then return an empty array
     */
    @Override
    public Person[] getUniqueCrew() {

        CustomArrayList<Person> crewList = new CustomArrayList<>();

        // Iterates through the linked list buckets in the crew map to find all the people in it
        for (int i = 0; i < crewDataMap.getCapacity(); i++) {
            ListElement<Integer, CrewData> element = crewDataMap.getHead(i);
            while (element != null) {
                crewList.add(element.getValue().getPerson());
                element = element.getNext();
            }
        }

        return crewList.getAsArrayPerson();
    }

    /**
     * Get all the cast members that have the given string within their name
     * 
     * @param cast The string that needs to be found
     * @return An array of unique Person objects of all cast members that have the
     *         requested string in their name. If there are no matches, return an
     *         empty array
     */
    @Override
    public Person[] findCast(String cast) {

        CustomArrayList<Person> castList = new CustomArrayList<>();
        
        // Iterates through the linked list buckets in the cast map to find all the people with the string in their name
        for (int i = 0; i < castDataMap.getCapacity(); i++) {
            ListElement<Integer, CastData> element = castDataMap.getHead(i);
            while (element != null) {
                Person person = element.getValue().getPerson();
                if (person.getName().contains(cast)) {
                    castList.add(person);
                }
                element = element.getNext();
            }
        }

        return castList.getAsArrayPerson();
    }

    /**
     * Get all the crew members that have the given string within their name
     * 
     * @param crew The string that needs to be found
     * @return An array of unique Person objects of all crew members that have the
     *         requested string in their name. If there are no matches, return an
     *         empty array
     */
    @Override
    public Person[] findCrew(String crew) {

        CustomArrayList<Person> crewList = new CustomArrayList<>();

        // Iterates through the linked list buckets in the crew map to find all the people with the string in their name
        for (int i = 0; i < crewDataMap.getCapacity(); i++) {
            ListElement<Integer, CrewData> element = crewDataMap.getHead(i);
            while (element != null) {
                Person person = element.getValue().getPerson();
                if (person.getName().contains(crew)) {
                    crewList.add(person);
                }
                element = element.getNext();
            }
        }

        return crewList.getAsArrayPerson();
    }

    /**
     * Gets the Person object corresponding to the cast ID
     * 
     * @param castID The cast ID of the person to be found
     * @return The Person object corresponding to the cast ID provided.
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCast(int castID) {
        CastData castData = castDataMap.get(castID);

        if (castData == null) {
            return null;
        }

        return castData.getPerson();

    }

    /**
     * Gets the Person object corresponding to the crew ID
     * 
     * @param crewID The crew ID of the person to be found
     * @return The Person object corresponding to the crew ID provided.
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCrew(int crewID) {
        CrewData crewData = crewDataMap.get(crewID);

        if (crewData == null) {
            return null;
        }

        return crewData.getPerson();
    }

    /**
     * Get an array of film IDs where the cast member has starred in
     * 
     * @param castID The cast ID of the person
     * @return An array of all the films the member of cast has starred
     *         in. If there are no films attached to the cast member,
     *         then return an empty array
     */
    @Override
    public int[] getCastFilms(int castID) {
        CastData castData = castDataMap.get(castID);

        if (castData == null) {
            return new int[0];
        }

        return castData.getMovies();

    }

    /**
     * Get an array of film IDs where the crew member has starred in
     * 
     * @param crewID The crew ID of the person
     * @return An array of all the films the member of crew has starred
     *         in. If there are no films attached to the crew member,
     *         then return an empty array
     */
    @Override
    public int[] getCrewFilms(int crewID) {

        CrewData crewData = crewDataMap.get(crewID);

        if (crewData == null) {
            return new int[0];
        }

        return crewData.getMovies();

    }

    /**
     * Get the films that this cast member stars in (in the top 3 cast
     * members/top 3 billing). This is determined by the order field in
     * the CastCredit class
     * 
     * @param castID The cast ID of the cast member to be searched for
     * @return An array of film IDs where the the cast member stars in.
     *         If there are no films where the cast member has starred in,
     *         or the cast member does not exist, return an empty array
     */
    @Override
    public int[] getCastStarsInFilms(int castID) {

        CastData castData = castDataMap.get(castID);

        if (castData == null) {
            return new int[0];
        }

        return castData.getMoviesTop3();

    }

    /**
     * Get Person objects for cast members who have appeared in the most
     * films. If the cast member has multiple roles within the film, then
     * they would get a credit per role played. For example, if a cast
     * member performed as 2 roles in the same film, then this would count
     * as 2 credits. The list should be ordered by the highest to lowest number of
     * credits.
     * 
     * @param numResults The maximum number of elements that should be returned
     * @return An array of Person objects corresponding to the cast members
     *         with the most credits, ordered by the highest number of credits.
     *         If there are less cast members that the number required, then the
     *         list should be the same number of cast members found.
     */
    @Override
    public Person[] getMostCastCredits(int numResults) {
        // Creates a minheap with <Integer, KeyValuePair<Integer,Integer>>
        CustomMinHeap<Integer> minHeap = new CustomMinHeap<>(numResults);

        // Iterates through the cast members in the cast data map to create the heap of max "numResults" elements
        for (int i = 0; i < castDataMap.getCapacity(); i++) {
            ListElement<Integer, CastData> element = castDataMap.getHead(i);
            while (element != null) {
                // Gets the <totalCredits, castID> as a KVP
                KeyValuePair<Integer, Integer> elementDetails = new KeyValuePair<>(element.getValue().getTotalCredits(),element.getKey()); 
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

        // Gets the cast IDs in descending order
        int[] topCastIDs = minHeap.getSortedDescendingValues();

        // Declares a person array and iterates through the IDs converting them into person objects 
        // This is because the heap doesn't deal with generic values to a KVP in the heap -would become more complex to deal with
        Person[] topCastPersons = new Person[topCastIDs.length];
        for (int i = 0; i < topCastIDs.length; i++) {
            topCastPersons[i] = castDataMap.get(topCastIDs[i]).getPerson();
        }

        return topCastPersons;
    }

    /**
     * Get the number of credits for a given cast member. If the cast member has
     * multiple roles within the film, then they would get a credit per role
     * played. For example, if a cast member performed as 2 roles in the same film,
     * then this would count as 2 credits.
     * 
     * @param castID A cast ID representing the cast member to be found
     * @return The number of credits the given cast member has. If the cast member
     *         cannot be found, return -1
     */
    @Override
    public int getNumCastCredits(int castID) { 

        CastData castData = castDataMap.get(castID);

        if (castData == null) {
            return -1;
        }

        return castData.getTotalCredits();
    }

    /**
     * Gets the number of films stored in this data structure
     * 
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        return this.size;
    }
}
