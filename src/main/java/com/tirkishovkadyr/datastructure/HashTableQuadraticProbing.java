package com.tirkishovkadyr.datastructure;

/*
  An implementation of a hash-table using open addressing with
  quadratic probing as a collision resolution method.

  In this implementation we are using the following probing function:
                   H(K, x) = h(k) + f(x) mod 2^n

  Where h(k) is the hash for the given key, f(x) = (x + x^2) / 2 and n
  is a natural number. We are using this probing function because it
  is guaranteed to find an empty cell (i.e it generates all the numbers
  in the range [0, 2^n) without repetition for the first 2^n numbers).
 */

import java.util.*;

@ SuppressWarnings("unchecked")
public class HashTableQuadraticProbing <K,V> implements Iterable <K> {

    private double loadFactor;
    private int capacity, threshold, modificationCount = 0;

    // 'usedBuckets' counts the total number of used buckets inside the
    // hash-table (includes cells marked as deleted). While 'keyCount'
    // tracks the number of unique keyTable currently inside the hash-table
    private int usedBuckets = 0, keyCount = 0;

    // These arrays store the key-value pairs
    private K[] keyTable;
    private V[] valueTable;

    // Flag used to indicate whether an item was found in the hash-table
    private boolean containsFlag = false;

    // Special marker token used to indicate the deletion of a key-value pair
    private final K TOMBSTONE = (K) (new Object());

    private static final int DEFAULT_CAPACITY = 8;
    private static final double DEFAULT_LOAD_FACTOR = 0.45;

    public HashTableQuadraticProbing() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    // Designated constructor
    public HashTableQuadraticProbing(int capacity, double loadFactor) {

        if (capacity <= 0)
            throw new IllegalArgumentException("Illegal capacity: " + capacity);

        if (loadFactor <= 0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor))
            throw new IllegalArgumentException("Illegal loadFactor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, next2Power(capacity));
        threshold = (int) (this.capacity * loadFactor);

        keyTable = (K[]) new Object[this.capacity];
        valueTable = (V[]) new Object[this.capacity];
    }

    // Given a number this method finds the next
    // power of two above this value
    private static int next2Power(int n) {
        return Integer.highestOneBit(n) << 1;
    }

    // Quadratic probing function (x^2+x)/2
    private static int P(int x) {
        return (x * x + x) >> 1;
    }

    // Converts a hash value to an index. Essentially, this strips the
    // negative sign and places the hash value in the domain [0, capacity)
    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    // Clears all the contents of the hash-table
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            keyTable[i] = null;
            valueTable[i] = null;
        }

        keyCount = usedBuckets = 0;
        modificationCount++;
    }

    // Returns the number of the keyTable currently inside the hash-table
    public int size() {
        return keyCount;
    }

    // Returns true/false depending on whether the hash-table is empty
    public boolean isEmpty() {
        return keyCount == 0;
    }

    // Insert, put and all place a value in the hash-table
    public V put(K key, V value) {
        return insert(key, value);
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    // Place a key value pair into the hash-table. If the value already
    // exists inside the hash-table then the value is updated
    public V insert(K key, V val) {

        if (key == null) throw new IllegalArgumentException("Null key");
        if (usedBuckets >= threshold) resizeTable();

        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        do {

            // The current slot was previously deleted 
            if (keyTable[i] == TOMBSTONE) {

                if (j == -1) j = i;

                // The current cell already contains a key
            } else if (keyTable[i] != null) {

                // The key we're trying to insert already exists in the hash-table,
                // so update its value with most recent value
                if (keyTable[i].equals(key)) {

                    V oldValue = valueTable[i];
                    if (j == -1) {
                        valueTable[i] = val;
                    } else {
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;
                        keyTable[j] = key;
                        valueTable[j] = val;
                    }

                    modificationCount++;
                    return oldValue;

                }

                // Current cell is null so an insertion/update can occur
            } else {

                // No previously encountered deleted buckets
                if (j == -1) {
                    usedBuckets++;
                    keyCount++;
                    keyTable[i] = key;
                    valueTable[i] = val;

                    // Previously seen deleted bucket. Instead of inserting 
                    // the new element at 'i' where the null element is insert
                    // it where the deleted token was found
                } else {
                    keyCount++;
                    keyTable[j] = key;
                    valueTable[j] = val;
                }

                modificationCount++;
                return null;

            }

            i = normalizeIndex(hash + P(x++));

        } while (true);
    }

    // Returns true/false on whether a given key exists within the hash-table
    public boolean containsKey(K key) {
        return hasKey(key);
    }

    // Returns true/false on whether a given key exists within the hash-table
    public boolean hasKey(K key) {

        // sets the 'containsFlag
        get(key);

        return containsFlag;

    }

    // Get the value associated with the input key.
    // NOTE: returns null if the value is null AND also returns
    // null if the key does not exists
    public V get(K key) {

        if (key == null) throw new IllegalArgumentException("Null key");
        final int hash = normalizeIndex(key.hashCode());
        int i = hash, j = -1, x = 1;

        // Starting at the original hash index quadratically probe until we find a spot here
        // our key is or we hit a null element in which case our element does not exist.
        do {
            // Ignore deleted cells, but record where the first index
            // of a deleted cell is found to perform lazy relocation later.

            if (keyTable[i] == TOMBSTONE) {

                if (j == -1) j = i;

                // We hit a non-null key, perhaps it's the one we're looking for.
            } else if (keyTable[i] != null) {

                // The key we want is in the hash-table!
                if (keyTable[i].equals(key)) {

                    containsFlag = true;

                    // If j != -1 this means we previously encountered a deleted cell.
                    // We can perform an optimization by swapping the entries in cells
                    // i and j so that the next time we search for this key it will be
                    // found faster. This is called lazy deletion/relocation
                    if (j != -1) {

                        // Copy value to where deleted bucket is
                        keyTable[j] = keyTable[i];
                        valueTable[j] = valueTable[i];

                        // Clear the contents in bucket i and mark it as deleted
                        keyTable[i] = TOMBSTONE;
                        valueTable[i] = null;

                        return valueTable[i];
                    } else {
                        return valueTable[i];
                    }

                }

            } else {
                containsFlag = false;
                return null;
            }

            i = normalizeIndex(hash + P(x++));

        } while (true);

    }

    // Removes a key from the map and returns the value.
    // NOTE: returns null if the value is null AND also returns
    // null if the key does not exist.
    public V remove(K key) {

        if (key == null) throw new IllegalArgumentException("Null key");

        final int hash = normalizeIndex(key.hashCode());
        int i = hash, x = 1;

        // Starting at the hash index quadratically probe until we find a spot where
        // out key is or we hit a null element in which case our element does not exist
        for (; ; i = normalizeIndex(hash + P(x++))) {

            // Ignore deleted cells
            if (keyTable[i] == TOMBSTONE) continue;

            // Key was not found in the hash-table
            if (keyTable[i] == null) return null;

            // The key we want to remove is in the hash-table!
            if (keyTable[i].equals(key)) {
                keyCount--;
                modificationCount++;
                V oldValue = valueTable[i];
                keyTable[i] = TOMBSTONE;
                valueTable[i] = null;
                return oldValue;
            }
        }
    }

    // Returns a list keyTable found in the hash table
    public List<K> keys() {
        List<K> keys = new ArrayList<>(size());
        for (int i = 0; i < capacity; i++)
            if (keyTable[i] != null && keyTable[i] != TOMBSTONE)
                keys.add(keyTable[i]);
        return keys;
    }

    // Returns a list of non-unique values found in the hash-table
    public List<V> values() {
        List<V> values = new ArrayList<>(size());
        for (int i = 0; i < capacity; i++)
            if (valueTable[i] != null && valueTable[i] != TOMBSTONE)
                values.add(valueTable[i]);
        return values;
    }

    // Double the size of the hash-table
    private void resizeTable() {

        capacity *= 2;
        threshold = (int) (capacity * loadFactor);

        K[] oldKeyTable = (K[]) new Object[capacity];
        V[] oldValueTable = (V[]) new Object[capacity];

        // Perform key table pointer swap
        K[] keyTableTmp = keyTable;
        keyTable = oldKeyTable;
        oldKeyTable = keyTableTmp;

        // Perform value table pointer swap
        V[] valueTableTmp = valueTable;
        valueTable = oldValueTable;
        oldValueTable = valueTableTmp;

        // Reset the key count and buckets used since we are about to
        // re-insert all the keyTable into the hash-table.
        keyCount = usedBuckets = 0;

        for (int i = 0; i < oldKeyTable.length; i++) {
            if (oldKeyTable[i] != null && oldKeyTable[i] != TOMBSTONE)
                insert(oldKeyTable[i], oldValueTable[i]);
            oldValueTable[i] = null;
            oldKeyTable[i] = null;
        }
    }

    // Return a String view of this hash-table.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for (int i = 0; i < capacity; i++)
            if (keyTable[i] != null && keyTable[i] != TOMBSTONE) sb.append(keyTable[i] + " => " + valueTable[i] + ", ");
        sb.append("}");

        return sb.toString();
    }

    @Override
    public Iterator<K> iterator() {
        // Before the iteration begins record the number of modifications
        // done to the hash-table. This value should not change as we iterate
        // otherwise a concurrent modification has occurred :0
        final int MODIFICATION_COUNT = modificationCount;

        return new Iterator<K>() {
            int index, keyTableLeft = keyCount;

            @Override
            public boolean hasNext() {
                // The contents of the table have been altered
                if (MODIFICATION_COUNT != modificationCount) throw new ConcurrentModificationException();
                return keyTableLeft != 0;
            }

            // Find the next element and return it
            @Override
            public K next() {
                while (keyTable[index] == null || keyTable[index] == TOMBSTONE) index++;
                keyTableLeft--;
                return keyTable[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
