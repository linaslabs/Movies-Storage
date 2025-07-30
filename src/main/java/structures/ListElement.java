package structures;

public class ListElement<K,V> {
    private K key;
    private V value;
    private ListElement<K,V> next;

    public ListElement(K key, V value){
        this.key = key;
        this.value = value;
        this.next = null;
    }


    public K getKey() {
        return this.key;
    }
    
    public V getValue() {
        return this.value;
    }

    public ListElement<K,V> getNext() {
        return this.next;
    }

    public void setNext(ListElement<K,V> nextElem) {
        this.next = nextElem;
    }
}
