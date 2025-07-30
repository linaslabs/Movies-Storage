package structures;

public class LinkedList<K,V> {
    
    ListElement<K,V> head;

    /**
     * Constructor for the linked list, sets the head as null
     */
    public LinkedList(){
        this.head = null;
    }

    /**
     * Function to add a list element to the linked list
     * 
     * @param key the key of the element to add
     * @param value the value of the element to add
     * @return a boolean value representing a successful addition
     */
    public boolean add(K key, V value) {
        ListElement<K,V> temp = new ListElement<>(key,value);
        // If there is already an element in the list, set the element's pointer to the current head
        if (this.head != null) {
            temp.setNext(this.head);
        }
        // Then set the element as the new head
        this.head = temp;

        return true;
    }

    /**
     * Function to remove a list element from a linked list
     * 
     * @param key the key of the element to be removed
     * @return a boolean value representing a successful or unsuccessful removal (if key not found)
     */
    public boolean remove(K key) {
        ListElement<K,V> current = this.head;
        // Previous node is initialised as null, so if the key is found, the previous node can point past the key to "remove" it
        ListElement<K,V> previous = null;

        // Searches for the list element with the key, then changes the pointers to remove it, and sets it to null
        while (current != null) {
            if (current.getKey().equals(key)) {
                if (previous == null) {
                    // If previous is null, it means the value to remove is the head
                    this.head = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                current.setNext(null);
                return true;
            }

            previous = current;
            current = current.getNext();
        }

        // Returns false if the element cannot be found with the specified key
        return false;
    }

    /**
     * Function that finds and returns the value of the element with the specified key
     * 
     * @param key the key of the element to find
     * @return a generic V representing the Object stored as the value in the list element, null if the element isnt found
     */
    public V find(K key){
        
        ListElement<K,V> current = this.head;

        while (current != null){
            if (current.getKey().equals(key)){
                return current.getValue();
            }
            current = current.getNext();
        }

        return null;
    }

    /**
     * Function that checks if the linked list is empty
     * 
     * @return a boolean value representing if the list is empty (head element is null)
     */
    public boolean isEmpty(){
        return this.head == null;
    }

    /**
     * Function to clear the linked list
     */
    public void clear() {
        this.head = null;
    }

    /**
     * Function to compute the size of the linked list
     * 
     * @return the size of the linked list
     */
    public int size() {
        ListElement<K,V> current = this.head;
        int count = 0;
        while (current.getNext() != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }
}

