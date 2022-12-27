import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Node next;
        Node prev;
        Item item;
    }

    private Node head;
    private Node tail;
    private int size = 0;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (size == 0) {
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
        }
        head = node;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (size == 0) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        Item item = head.item;
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            head.next.prev = null;
            head = head.next;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        Item item = tail.item;
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            tail.prev.next = null;
            tail = tail.prev;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {

        return new Iterator<>() {

            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {

        Deque<String> deque = new Deque<>();
        if (!deque.isEmpty()) {
            throw new IllegalStateException("Deque should be empty");
        }

        deque.addFirst("First");
        if (deque.isEmpty()) {
            throw new IllegalStateException("Deque should not be empty");
        }

        if (deque.size() != 1) {
            throw new IllegalStateException("Deque should have size = 1");
        }

        deque.addFirst("FirstFirst");
        if (deque.size() != 2) {
            throw new IllegalStateException("Deque should have size = 2");
        }

        String str1 = deque.removeFirst();
        if (!str1.equals("FirstFirst")) {
            throw new IllegalStateException("Incorrect item after remove first");
        }

        String str2 = deque.removeLast();
        if (!str2.equals("First")) {
            throw new IllegalStateException("Incorrect item after remove last");
        }

        if (deque.size() != 0) {
            throw new IllegalStateException("Deque should have size = 0");
        }

        deque.addLast("Last");
        String str3 = deque.removeFirst();
        if (!str3.equals("Last")) {
            throw new IllegalStateException("Incorrect item after remove first");
        }

        if (deque.size() != 0) {
            throw new IllegalStateException("Deque should have size = 0");
        }

        deque.addFirst("first");
        deque.addLast("last");
        deque.addLast("last-last");
        Iterator<String> iterator = deque.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException("Iterator should point to existed item");
        }
        String next = iterator.next();
        if (!next.equals("first")) {
            throw new IllegalStateException("Iterator should point to item with text \"first\"");
        }
        String nextnext = iterator.next();
        if (!nextnext.equals("last")) {
            throw new IllegalStateException("Iterator should point to item with text \"last\"");
        }

        StdOut.println("Test is successful");
    }

}