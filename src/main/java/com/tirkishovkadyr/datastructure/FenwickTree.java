package com.tirkishovkadyr.datastructure;

/**
 * A Fenwick Tree implementation which supports
 * point updates and sum range queries
 */

public class FenwickTree {

    // This array contains the Fenwick tree ranges
    private long[] tree;

    // Create an empty Fenwick tree
    public FenwickTree(int sz) {
        tree = new long[sz + 1];
    }

    // Make sure the 'values' array is one based meaning
    // values[0] does not used, O(n) construction
    public FenwickTree(long[] values) {
        if (values == null)
            throw new IllegalArgumentException("Values array cannot be null!");

        // Make a clone of the values array since we manipulate
        // the array in place destroying all its original content
        this.tree = values.clone();

        for (int i = 1; i < tree.length; i++) {

            int j = i + lsb(i);
            if (j < tree.length) tree[j] += tree[i];

        }
    }

    // Returns the value of the least significant bit (LSB)
    // lsb(108) = lsb(0b1101100) =     0b100 = 4
    // lsb(104) = lsb(0b1101000) =    0b1000 = 8
    // lsb(96)  = lsb(0b1100000) =  0b100000 = 32
    // lsb(64)  = lsb(0b1000000) = 0b1000000 = 64
    private int lsb(int i) {

        // Isolates the lowest one bit value
        return i & -i;

        // An alternative method is to use the Java's built in method
        // return Integer.lowestOneBit(i);

    }

    // Computes the prefix sum from [1, i], one based
    public long prefixSum(int i) {
        long sum = 0L;
        while (i != 0) {
            sum += tree[i];
            i &= ~lsb(i); // Equivalently, i -= lsb(i);
        }
        return sum;
    }

    // Returns the sum of the interval [i, j], one based
    public long sum(int i, int j) {
        if (j < i) throw new IllegalArgumentException("Make sure j >= i");
        return prefixSum(j) - prefixSum(i - 1);
    }

    // Add 'k' to index 'i', one based
    public void add(int i, long k) {
        while (i < tree.length) {
            tree[i] += k;
            i += lsb(i);
        }
    }

    // Set index i to be equal to k, one based
    public void set (int i, long k) {
        long value = sum(i, i);
        add(i, k - value);
    }
}
