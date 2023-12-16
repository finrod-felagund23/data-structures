package com.tirkishovkadyr.datastructure;

public class Stack<T> implements Iterable<T> {

    private java.util.LinkedList<T> list = new java.util.LinkedList<>();

    // Create an empty stack
    public Stack () { }

    // Create a Stack with an initial element
    public Stack (T firstElem) {
        this.push(firstElem);
    }

    // Return the number of the elements in the stack
    public int size() {
        return this.list.size();
    }

    // Is the stack empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // Push an element on the stack
    public void push(T elem) {
        this.list.addLast(elem);
    }

    // Pop an element off the stack
    // Throws an error if the stack is empty
    public T pop() {
        if (this.isEmpty())
            throw new java.util.EmptyStackException();
        return this.list.removeLast();
    }

    // Peek the top of the stack without removing an element
    // Throws an exception if the stack is empty
    public T peek() {
        if (this.isEmpty())
            throw new java.util.EmptyStackException();
        return this.list.peekLast();
    }

    // Allow users to iterate through the stack using an iterator
    @Override public java.util.Iterator<T> iterator() {
        return list.iterator();
    }


}
