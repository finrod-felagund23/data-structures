package com.tirkishovkadyr.datastructure;

public class DoublyLinkedList <T> implements Iterable <T> {
    // TODO: Fix toString() method. in end makes excess comma and space


    private int size = 0;
    private Node <T> head = null;
    private Node <T> tail = null;

    //Internal node class to represent data
    private class Node <T> {
        T data;
        Node <T> prev, next;
        public Node(T data, Node <T> prev, Node <T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        @Override public String toString() {
            return data.toString();
        }
    }

    // Empty this linked list, O(n)
    public void clear() {
        Node<T> trav = this.head;
        while (trav != null) {
            Node<T> next = trav.next;
            trav.prev = trav.next = null;
            trav.data = null;
            trav = next;
        }
        head = tail = trav = null;
        this.size = 0;
    }

    // Return the size of this linked list
    public int size() {
        return this.size;
    }

    // Is this linked list empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // Add an element to the tail of the linked list, O(1)
    public void add(T elem) {
        this.addLast(elem);
    }

    // Add an element to the begining of this linked list, O(1)
    public void addFirst(T elem) {

        // The linked list is empty
        if (this.isEmpty()) {
            head = tail = new Node<T> ( elem, null, null );
        } else {
            head.prev = new Node<T> ( elem, null, head );
            head = head.prev;
        }

        size++;
    }

    // Add a node to the tail of the linked list, O(1)
    public void addLast(T elem) {

        // The linked list is empty
        if (isEmpty()) {
            head = tail = new Node<T> ( elem, null, null );
        } else {
            tail.next = new Node<T> ( elem, tail, null );
            tail = tail.next;
        }
        size++;
    }

    // Check the value of the first node if it exists, O(1)
    public T peekFirst() {
        if (this.isEmpty()) throw new RuntimeException("Can't peekFirst in empty list!");
        return head.data;
    }

    // Check the value of the last node if it exists, O(1)
    public T peekLast() {
        if (this.isEmpty()) throw new RuntimeException("Can't peekLast in empty list!");
        return tail.data;
    }

    // Remove the first value at the head of the linked list, O(1)
    public T removeFirst() {

        // Can't remove data from an empty list -_-
        if (this.isEmpty()) throw new RuntimeException("Can't remove from empty list!");

        // Extract the data at the head and move
        // the head pointer forwards one node
        T data = head.data;
        head = head.next;
        --size;

        // If the list is empty set the tail to null as well
        if (this.isEmpty()) tail = null;

        // Do a memory clean of the previous node
        else head.prev = null;

        // Return the data that was at the first node we just removed
        return data;
    }

    public T removeLast() {

        // Can't remove data from an empty list -_-
        if (this.isEmpty()) throw new RuntimeException("Empty lsit!");

        // Extract the data at the tail and move
        // the tail pointer backwards one node
        T data = tail.data;
        tail = tail.prev;
        --size;

        // If the list is empty set the head to null as well
        if (this.isEmpty()) head = null;

        // Do a memory clean of the node that was just removed
        else tail.next = null;

        // Return the data that was at the first node we just removed
        return data;
    }

    // Remove an arbitrary node from the linked list, O(1)
    private T remove(Node<T> node) {

        // If the node to remove is somewhere either at the
        // head or the tail handle those independently
        if (node.prev == null) return removeFirst();
        if (node.next == null) return removeLast();

        // Make the pointers of adjacent nodes skip overt 'node'
        node.next.prev = node.prev;
        node.prev.next = node.next;

        // Temporary store the data we want to return
        T data = node.data;

        // Memory cleanup
        node.data = null;
        node = node.prev = node.next = null;

        --size;

        // Return the data at the node we just removed
        return data;
    }

    // Remove a node at particulary index, O(n)
    public T removeAt(int index) {

        // Make sure the index provided is valid -_-
        if (index < 0 || index >= size) throw new IllegalArgumentException();

        int i;
        Node<T> trav;

        // Search from the front of the list
        if (index < size/2) {
            for (i = 0, trav = this.head; i != index; i++)
                trav = trav.next;

        // Search from the back of the list
        } else
            for (i = size-1, trav = this.tail; i != index; i--)
                trav = trav.prev;

        return this.remove(trav);
    }

    // Remove a particular value in the linked list, O(n)
    public boolean remove(Object obj) {
        Node<T> trav = this.head;

        // Support searching for null
        if (obj == null) {
            for (trav = head; trav != null; trav = trav.next) {
                if (trav.data == null) {
                    remove(trav);
                    return true;
                }
            }
            // Search for non null object
        } else {
            for (trav = head; trav != null; trav = trav.next) {
                if (obj.equals(trav.data)) {
                    remove(trav);
                    return true;
                }
            }
        }
        return false;
    }

    // Find the index of a particular value in the linked list, O(n)
    public int indexOf(Object obj) {

        int index = 0;
        Node<T> trav = head;

        // Support searching for null
        if (obj == null) {
            for (trav = head; trav != null; trav = trav.next, index++) {
                if (trav.data == null)
                    return index;
            }
        // Searching for non null object
        } else
            for (trav = head; trav != null; trav = trav.next, index++)
                if (obj.equals(trav.data))
                    return index;

        return -1;
    }

    // Check the value is contained within the linked list
    public boolean contains(Object obj) {
        return this.indexOf(obj) != -1;
    }

    @Override public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T> () {
            private Node<T> trav = head;
            @Override public boolean hasNext() {
                return trav != null;
            }
            @Override public T next() {
                T data = trav.data;
                trav = trav.next;
                return data;
            }
        };
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        Node<T> trav = head;
        while (trav != null) {
            sb.append(trav.data + ", ");
            trav = trav.next;
        }
        sb.append(" ]");
        return sb.toString();
    }

}
