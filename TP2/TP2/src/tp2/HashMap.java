package tp2;

public class HashMap<KeyType, DataType> {

    private static final int DEFAULT_CAPACITY = 20;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final int CAPACITY_INCREASE_FACTOR = 2;

    private Node<KeyType, DataType>[] map;
    private int size = 0;
    private int capacity;
    private final float loadFactor; // Compression factor

    /**
     * Constructeur par défaut
     */
    public HashMap() { this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR); }

    /**
     * Constructeur par parametre
     * @param initialCapacity
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY,
                DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructeur par parametres
     * @param initialCapacity
     * @param loadFactor
     */
    public HashMap(int initialCapacity, float loadFactor) {
        capacity = initialCapacity;
        this.loadFactor = 1 / loadFactor;
        map = new Node[capacity];
    }

    /**
     * Finds the index attached to a particular key
     * This is the hashing function ("Fonction de dispersement")
     * @param key Value used to access to a particular instance of a DataType within map
     * @return Index value where this key should be placed in attribute map
     */
    private int hash(KeyType key){
        int keyHash = key.hashCode() % capacity;
        return Math.abs(keyHash);
    }

    /**
     * @return if map should be rehashed
     */
    private boolean needRehash() {
        return size * loadFactor > capacity;
    }

    /**
     * @return Number of elements currently in the map
     */
    public int size() {
        return size;
    }

    /**
     * @return Current reserved space for the map
     */
    public int capacity(){ return capacity; }

    /**
     * @return if map does not contain any element
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /** TODO
     * Increases capacity by CAPACITY_INCREASE_FACTOR (multiplication) and
     * reassigns all contained values within the new map
     */
    private void rehash() {
        Node<KeyType, DataType>[] oldMap = map;

        map = new Node[capacity() * CAPACITY_INCREASE_FACTOR];
        size = 0;
        capacity *= CAPACITY_INCREASE_FACTOR;

        for ( int i = 0; i < oldMap.length; i++ ) {
            Node<KeyType, DataType> currentNode = oldMap[i];
            if ( currentNode != null )
                while ( currentNode != null ){
                    put(currentNode.key, currentNode.data);
                    currentNode = currentNode.next;
                }
        }
//
//        Node<KeyType, DataType>[] newMap = new Node[capacity() * CAPACITY_INCREASE_FACTOR];
//        size = 0;
//
//        for(int i = 0; i < oldMap.length; i++ )
//            if( oldMap[ i ] != null )
//                insert( oldMap[ i ].element )

        return;
    }

    /** TODO
     * Finds if map contains a key
     * @param key Key which we want to know if exists within map
     * @return if key is already used in map
     */
    public boolean containsKey(KeyType key) {
        int whichList = hash(key);
        if ( map[whichList] == null )
            return false;
        else {
            Node<KeyType, DataType> node = map[whichList];
            while ( !node.key.equals(key) && node.next != null ) {
                node = node.next;
            }
            if ( node != null && node.key.equals(key) )
                return true;
            else
                return false;
        }
    }

    /** TODO
     * Finds the value attached to a key
     * @param key Key which we want to have its value
     * @return DataType instance attached to key (null if not found)
     */
    public DataType get(KeyType key) {
        int whichList = hash(key);
        if ( map[whichList] == null ) {
            return null;
        }
        else {
            Node<KeyType, DataType> node = map[whichList];
            while ( !node.key.equals(key) && node != null ) {
                boolean test = node.key.equals(key);
                node = node.next;
            }
            return node.data;
//            Node<KeyType, DataType> node = map[whichList];
//            KeyType newKey = map[whichList].key;
//            while ( newKey != key && newKey != null ) {
//                    node = node.next;
//                    newKey = node.next.key;
//            }
//            return node.data;
        }
    }

    /**TODO
     * Assigns a value to a key
     * @param key Key which will have its value assigned or reassigned
     * @return Old DataType instance at key (null if none existed)
     */
    public DataType put(KeyType key, DataType value) {
        int whichList = hash(key);

        if ( containsKey(key) ) {
            for ( int i = 0; i < capacity(); i++ ) {
                Node<KeyType, DataType> currentNode = map[i];
                while ( currentNode != null ) {
                    if (currentNode.key.equals(key)) {
                        DataType oldValue = currentNode.data;
                        currentNode.data = value;
                        if ( needRehash() )
                            rehash();
                        return oldValue;
                    }

                }
            }
//            Node<KeyType, DataType> nodeWithSameKey = new Node<KeyType, DataType>(key, get(key));
//            DataType valueOfSameKey = get(key);
//            valueOfSameKey = value;
//            get(key) = value;
//            return nodeWithSameKey.data;
////            Node<KeyType, DataType> nodeWithSameKey = new Node<KeyType, DataType>(key, value);
////            DataType valueOfSameKey = get(key);
////            valueOfSameKey = value;
////            //DataType oldValue = value;
////            return nodeWithSameKey.data;
        }
        if ( map[whichList] != null ) {
            Node<KeyType, DataType> newNode = new Node<KeyType, DataType>(key, value);
            Node<KeyType, DataType> oldNode = map[whichList];
            newNode.next = oldNode;
            map[whichList] = newNode;
            size++;
            if ( needRehash() )
                rehash();
            return oldNode.data;
        }
        else {
            map[whichList] = new Node<KeyType, DataType>(key, value);
            size++;
            if ( needRehash() )
                rehash();
            return null;
        }
    }

    /**TODO
     * Removes the node attached to a key
     * @param key Key which is contained in the node to remove
     * @return Old DataType instance at key (null if none existed)
     */
    public DataType remove(KeyType key) {
        int whichList = hash(key);
        if ( map[whichList] == null )
            return null;
        else {
            Node<KeyType, DataType> prevNode = null;
            Node<KeyType, DataType> currentNode = map[whichList];
            while ( !currentNode.key.equals(key) && currentNode != null ) {
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            if ( prevNode != null ) {
                prevNode.next = currentNode.next;
            }
            if ( prevNode == null && currentNode.next != null)
                map[whichList] = currentNode.next;

            if ( prevNode == null && currentNode.next == null )
                map[whichList] = null;
                size--;
            currentNode.next = null;
            return currentNode.data;
        }
    }

    /**TODO
     * Removes all nodes contained within the map
     */
    public void clear() {
        for ( int i = 0; i < capacity(); i++) {
            Node<KeyType, DataType> currentNode = map[i]; //attention aux index ou les elements sont dans
            //map, commences pas forcement a 0
            while ( currentNode != null ) {
                remove(currentNode.key);
                currentNode = currentNode.next;
            }
        }
    }

    /**
     * Definition et implementation de la classe Node
     */
    static class Node<KeyType, DataType> {
        final KeyType key;
        DataType data;
        Node<KeyType, DataType> next; // Pointer to the next node within a Linked List

        Node(KeyType key, DataType data)
        {
            this.key = key;
            this.data = data;
            next = null;
        }
    }
}