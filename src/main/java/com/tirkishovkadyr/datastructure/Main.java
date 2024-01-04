package com.tirkishovkadyr.datastructure;

import java.util.Arrays;

public class  Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Array<String> arr = new Array<>(99);
        System.out.println(arr.toString());

        DoublyLinkedList<Integer> dl = new DoublyLinkedList<>();
        for (int i = 0; i < 11; i++)
            dl.add(i);

        System.out.println(dl);
        Integer[] int_arr = {4,2,1,5,7,2,5,8,1,90};

//        for (int i = 0; i != 10; i++)
//            int_arr[i] = i;

        PQueue<Integer> pq = new PQueue<>(int_arr);

        System.out.println(pq);


        UnionFind uf = new UnionFind(20);
        System.out.println(uf);

        HashTableQuadraticProbing<Integer, String> ht  = new HashTableQuadraticProbing<>(999);
        for (int i = 0; i < 999; i++ ) {
            ht.add(i, i+"");
        }
        System.out.println(ht);

        SuffixArraySlow sa = new SuffixArraySlow("ABBABAABAA");
        System.out.println(Arrays.toString(sa.getSa()));
        System.out.println(Arrays.toString(sa.getLcpArray()));
        System.out.println(sa);

        // Balanced Binary Tree
        AVLTreeRecursive<Integer> tree = new AVLTreeRecursive<>();
        for (int i = 0; i < 50; i++)
            tree.insert((int) (Math.random() * 100));
        System.out.println(tree.display());
    }

}