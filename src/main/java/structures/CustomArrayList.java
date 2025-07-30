package structures;

import stores.Person;

public class CustomArrayList<E> {
    private Object[] array;
    private int size;
    private int capacity;

    public CustomArrayList() {
        this.array = new Object[2];
        this.capacity = 2;
        this.size = 0;
    }

    public void add(E item) {
        if (this.size == this.capacity) {
            this.capacity = this.capacity * 2;
            Object[] resizedArray = new Object[this.capacity];
            System.arraycopy(this.array, 0, resizedArray, 0, this.size);
            this.array = resizedArray;
        }

        this.array[size++] = item;

    }

    public boolean remove(E item) {
        for (int i = 0; i < size; i++) {
            if (this.array[i].equals(item)) {
                for (int j = i + 1; j < this.size; j++) {
                    this.array[j - 1] = this.array[j];
                }
                this.array[size - 1] = null;
                this.size--;
                return true;
            }
        }
        return false;

    }

    /**
     * Function to return the Object array stored as an array of Person objects
     * 
     * @return an array of person objects
     */
    public Person[] getAsArrayPerson() {
        Person[] arrayAsInt = new Person[this.size];

        for (int i = 0; i < this.size; i++) {
            // Checks if the element being converted is of instance Person, if not, throw an
            // exception
            if (!(this.array[i] instanceof Person)) {
                throw new IllegalStateException("There are elements in the arraylist that are not of the Integer type");
            }
            arrayAsInt[i] = (Person) this.array[i];

        }

        return arrayAsInt;

    }

    /**
     * Function to return the Object array stored as an array of primitive int
     * values
     * 
     * @return an array of integers
     */
    public int[] getAsArrayInt() {
        int[] arrayAsInt = new int[this.size];

        for (int i = 0; i < this.size; i++) {
            // Checks if the element being converted is of instance Integer, if not, throw
            // an exception
            if (!(this.array[i] instanceof Integer)) {
                throw new IllegalStateException("There are elements in the arraylist that are not of the Integer type");
            }
            arrayAsInt[i] = (Integer) this.array[i];

        }

        return arrayAsInt;

    }

    /**
     * Function to return the Object array stored as an array of primitive float
     * values
     * 
     * @return an array of floats
     */
    public float[] getAsArrayFloat() {
        float[] arrayAsFloat = new float[this.size];

        for (int i = 0; i < this.size; i++) {
            // Checks if the element being converted is of instance Float, if not, throw an
            // exception
            if (!(this.array[i] instanceof Float)) {
                throw new IllegalStateException("There are elements in the arraylist that are not of the Float type");
            }
            arrayAsFloat[i] = (Float) this.array[i];

        }

        return arrayAsFloat;

    }
}
