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
        Integer[] int_arr = {4,2,1,5,7,2,5,8,1,90};

//        for (int i = 0; i != 10; i++)
//            int_arr[i] = i;

        PQueue<Integer> pq = new PQueue<>(int_arr);

        System.out.println(pq);

    }

}