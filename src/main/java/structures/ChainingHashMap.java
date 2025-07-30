package structures;

/**
 * 
 * My implementation of a hash map making use of separate chaining and linked
 * lists
 */
public class ChainingHashMap<K, V> {
    private LinkedList<K, V>[] table;
    private CustomArrayList<K> keys;
    private int capacity;
    private int size;
    private double lf;

    /**
     * Constructor for the hash map
     * 
     * @param capacity the capacity requested
     */
    @SuppressWarnings("unchecked")
    public ChainingHashMap(int capacity) {
        table = new LinkedList[capacity];
        // Initialise a running arraylist of the current keys in the hash table (so
        // returning all the ID's is quicker)
        keys = new CustomArrayList<>();
        this.capacity = capacity;
        this.size = 0;
        // Initialise the table by setting all the buckets as empty linked lists
        initTable(this.table, this.capacity);
        // Update/initialise the load factor
        updateLF();
    }

    /**
     * Function to add a key and value as a list element to the linked list at the
     * index specified by the key % capacity
     * 
     * @param key   an key that, once hashed, specifies the index/bucket location to
     *              insert the element
     * @param value an object V which represents the value to be added
     * @return a boolean represeting a successful addition if the key doesn't
     *         already exist in the table, false if it does
     */
    public boolean add(K key, V value) {
        // If the load factor for this separate chaining hash map is above 1.5, the
        // table will be rehashed
        if (lf > 1.5) {
            rehash();
        }
        // The location is simply the hashcode key MOD the capacity of the table
        int hashCode = key.hashCode();
        int location = Math.abs(hashCode) % capacity;

        // If the location already has the same key, return false, if it doesn't, add
        // the key and the value to the linked list at the location
        if (table[location].find(key) == null) {
            table[location].add(key, value);
            this.size++;
            updateLF();
            // Add the key to the key arraylist
            keys.add(key);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function to remove a key and its value specified from the hash table
     * 
     * @param key the key to be removed
     * @return a boolean value representing if the removal was a success, or the key
     *         is not present to remove
     */
    public boolean remove(K key) {
        int hashCode = key.hashCode();
        int location = Math.abs(hashCode) % capacity;
        if (table[location].remove(key)) {
            this.size--;
            updateLF();
            // Remove the key from the key arraylist
            keys.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Function to return the value associated with a key from the hash table
     * 
     * @param key the key of the value to be returned
     * @return the object value to be returned
     */
    public V get(K key) {
        int hashCode = key.hashCode();
        int location = Math.abs(hashCode) % capacity;
        return table[location].find(key);
    }

    /**
     * Function to return the head of the linked list at an index, to help with with
     * functions relying on iterating through the table...
     * 
     * @param index the index of the location to access
     * @return a list element representing the head of the linked list at the
     *         location
     */
    public ListElement<K, V> getHead(int index) {
        return table[index].head;
    }

    /**
     * Function to return all the keys stored in the hash table as integers
     * 
     * @return an integer array representing the keys stored
     */
    public int[] getKeysAsInt() {
        return keys.getAsArrayInt();
    }

    /**
     * Function to initialise the hash table
     * 
     * @param tableInit     the table to initialise
     * @param tableCapacity the table capacity to intialise to
     */
    private void initTable(LinkedList<K, V>[] tableInit, int tableCapacity) {
        for (int i = 0; i < tableCapacity; i++) {
            tableInit[i] = new LinkedList<>();
        }
    }

    /**
     * Function to update the load factor
     */
    private void updateLF() {
        lf = (double) size / (double) capacity;
    }

    /**
     * Function to return the capacity of the hash table
     * 
     * @return capacity of the hash table
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Function to return the size of the hash table
     * 
     * @return size of the hash table
     */
    public int getSize() {
        return size;
    }

    /**
     * Function to rehash the hash table
     */
    private void rehash() {

        int[] calculatedPrimes = new int[]{4307, 8623, 17257, 34537, 69091, 138193, 276401, 552811, 1105657, 2211329, 4422677};

        // Doubles the current capacity
        int newCapacity = this.capacity * 2;

        // IF the number of elements goes beyond the last element, then revert to calculations for the next prime (failsafe)
        if (newCapacity > calculatedPrimes[10]){
            newCapacity = findNextPrime(newCapacity);
        }else{
            // Speeds up rehashing process by setting the new capacity to the next biggest prime in the list
            for (int prime : calculatedPrimes){
                if (newCapacity < prime){
                    newCapacity = prime;
                    break;
                }
            }
        }

        // Create the new table, and initialise
        @SuppressWarnings("unchecked")
        LinkedList<K, V>[] newTable = new LinkedList[newCapacity];
        initTable(newTable, newCapacity);

        // Rehash the elements in the original table to the new table
        int newLocation = 0;
        for (LinkedList<K, V> element : table) {
            ListElement<K, V> currentNode = element.head;
            while (currentNode != null) {
                int hashCode = currentNode.getKey().hashCode();
                newLocation = Math.abs(hashCode) % newCapacity;
                newTable[newLocation].add(currentNode.getKey(), currentNode.getValue());
                currentNode = currentNode.getNext();
            }
        }

        this.capacity = newCapacity;
        this.table = newTable;

    }



    // *** Following functions only used when "failsafe" is activated ***

    /**
     * Function to find the next prime number of the capacity
     * @param newCapacity
     * @return the next prime number
     */
    private int findNextPrime(int newCapacity){
        // Find next odd number
        if ((newCapacity % 2) == 0) {
            newCapacity++;
        }
        // From odd number, skip the evens and check if the next odd is a prime
        while (!isPrime(newCapacity)) {
            newCapacity += 2;
        }

        return newCapacity;
    }

    /**
     * Function to check if the given number is a prime
     * 
     * @param num number to check
     * @return a boolean representing whether the number is a prime or not
     */
    private boolean isPrime(int num) {
        // Checking if the number is even, so automatically not prime
        if (num % 2 == 0) {
            return false;
        }
        // Calculates the square root of a number, since checking up until that is
        // enough to determine a prime
        int sqrt = (int) Math.sqrt(num);
        // Checks only odd numbers, since check for even has been done
        for (int i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;

    }
}
