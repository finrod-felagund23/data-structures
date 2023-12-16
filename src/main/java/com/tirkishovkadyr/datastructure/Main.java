package com.tirkishovkadyr.datastructure;

public class  Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Array<String> arr = new Array<>(99);
        System.out.println(arr.toString());

        DoublyLinkedList<Integer> dl = new DoublyLinkedList<>();
        for (int i = 0; i < 11; i++)
            dl.add(i);

        System.out.println(dl);
    }

}