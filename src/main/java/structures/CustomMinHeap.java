package structures;

/**
 * 
 * My implementation of a minimum heap of key value pairs <K, Integer> where K
 * is the value to be sorted on,
 * and Integer is (usually) the identifier link
 */
public class CustomMinHeap<K extends Comparable<K>> {
    private KeyValuePair<K, Integer>[] heap; // Heap stores K which is the comparable data type, and the corresponding
                                             // ID (movie or user)
    private int lastElementIndex = -1;

    /**
     * Constructor for the min heap
     * 
     * @param capacity this variable corresponds to the max number of items to be
     *                 returned
     */
    @SuppressWarnings("unchecked")
    public CustomMinHeap(int capacity) {
        heap = new KeyValuePair[capacity];
    }

    /**
     * Function to pop the root of the minimum heap
     * 
     * @return the minimum element on the heap
     */
    public KeyValuePair<K, Integer> popRoot() {
        if (isEmpty()) {
            return null;
        }
        KeyValuePair<K, Integer> result = this.heap[0];
        // Replaces the root with the last element (whilst decrementing the last element
        // index)
        this.heap[0] = this.heap[lastElementIndex--];
        this.heap[lastElementIndex + 1] = null;
        // Calls downheap from the root to find new min
        downheap(0, lastElementIndex);
        return result;
    }

    /**
     * Function to return the minimum (root) element of the heap
     * 
     * @return the minimum element on the heap
     */
    public KeyValuePair<K, Integer> peek() {
        if (isEmpty()) {
            return null;
        }

        return this.heap[0];
    }

    /**
     * Function to add a new element to the min heap
     * 
     * @param element the new element to be added
     * @return a boolean represeting a successful or unsucessful addution
     */
    public boolean add(KeyValuePair<K, Integer> element) {
        if (isFull()) {
            // In a normal heap, it would be resized, but the custom heap is set to a set
            // size for my implementation
            return false; // Add is never called if full, but in case used then returns false
        }
        // Increment the lastElementIndex, accesses the index, and keep the
        // lastElementIndex incremented
        this.heap[++lastElementIndex] = element;
        // Find new min after adding element
        upheap(lastElementIndex);
        return true;
    }

    /**
     * Method to process the heap to find the new min from the down up
     * 
     * @param startIndex the beginning index
     */
    private void upheap(int startIndex) {
        int parentIndex = (startIndex - 1) / 2;
        // Swaps the current index with the parent if the current value of comparison is
        // less than that of the parent
        while (parentIndex >= 0 && this.heap[startIndex].compareTo(this.heap[parentIndex]) < 0) {
            swap(startIndex, parentIndex);
            // The start index is now the parent (the upheap travels upwards)
            startIndex = parentIndex;
            parentIndex = (startIndex - 1) / 2;
        }
    }

    /**
     * Method to process the heap to find the new min from the up down
     * 
     * @param startIndex the beginning index (usually the root)
     * @param lastIndex  the ending index (the bottom of the heap)
     */
    private void downheap(int startIndex, int lastIndex) {
        if (lastIndex == -1) { // heap is empty
            return;
        }
        while (startIndex <= lastIndex) {
            int leftChildIndex = 2 * startIndex + 1;
            int rightChildIndex = 2 * startIndex + 2;
            int childSwapIndex;

            // If left out of bounds, then right is also out of bounds, so the end is
            // reached
            if (leftChildIndex > lastIndex) {
                break; // no longer need to swap
            }
            // If the above is not true, but the right is greater than the end, then the
            // left child is the only one left to choose
            if (rightChildIndex > lastIndex) {
                childSwapIndex = leftChildIndex;
                // If both left and right childs before the end index, then compare the left to
                // right, mark the least child to swap
            } else if (this.heap[leftChildIndex].compareTo(this.heap[rightChildIndex]) < 0) {
                childSwapIndex = leftChildIndex;
            } else {
                childSwapIndex = rightChildIndex;
            }

            // If the current index is already less than the child marked to swap, then
            // minimum is already current
            if (this.heap[startIndex].compareTo(this.heap[childSwapIndex]) < 0) {
                break;
            }
            swap(startIndex, childSwapIndex);
            startIndex = childSwapIndex;
        }
    }

    /**
     * Function to create a sorted array of id's in descending order dependant on
     * their keys being compared
     * 
     * @return the final sorted array of id's in descending order
     */
    public int[] getSortedDescendingValues() {
        int[] sortedArray = new int[lastElementIndex + 1];
        // Return the root (min) of the heap, add it as an element of the array from
        // right to left consecutively for the length of the array
        for (int i = sortedArray.length - 1; i >= 0; i--) {
            Integer val = popRoot().getValue();
            sortedArray[i] = val;
        }
        return sortedArray;
    }

    /**
     * Function to create a sorted array of id's in ascending order dependant on
     * their keys being compared
     * 
     * @return the final sorted array of id's in ascending order
     */
    public int[] getSortedAscendingValues() {
        int[] sortedArray = new int[lastElementIndex + 1];
        // Return the root (min) of the heap, add it as an element of the array from
        // right to left consecutively for the length of the array
        for (int i = 0; i < sortedArray.length; i++) {
            Integer val = popRoot().getValue();
            sortedArray[i] = val;
        }
        return sortedArray;
    }


    /**
     * Method to swap two given values in the indexes
     * 
     * @param firstIndex
     * @param secondIndex
     */
    private void swap(int firstIndex, int secondIndex) {
        KeyValuePair<K, Integer> temp = this.heap[firstIndex];
        this.heap[firstIndex] = this.heap[secondIndex];
        this.heap[secondIndex] = temp;
    }

    /**
     * Function to return the full state of the current heap
     * 
     * @return a boolean representing whether or not the heap is full
     */
    public boolean isFull() {
        return (this.heap.length - 1 == lastElementIndex);
    }

    /**
     * Function to return the empty state of the current heap
     * 
     * @return a boolean representing whether or not the heap is empty
     */
    public boolean isEmpty() {
        return lastElementIndex == -1;
    }
}
