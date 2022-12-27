import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;
    private Item[] items = (Item[]) new Object[1];

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (items.length == size) {
            resize(size * 2);
        }
        items[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(size);
        Item item = items[random];
        items[random] = items[size - 1];
        items[size - 1] = null;
        size--;
        if (size != 0 && size <= items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(size);
        return items[random];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {

        Item[] newItems = shuffleWithCopy();
        return new Iterator<>() {

            private final Item[] items = newItems;
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < items.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item item = items[index];
                index++;
                return item;
            }
        };
    }

    private void resize(int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];
        int min = Math.min(size, newSize);
        for (int i = 0; i < min; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    private Item[] shuffleWithCopy() {
        Item[] newItems = (Item[]) new Object[size];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }
        for (int i = 0; i < size; i++) {
            int random = StdRandom.uniform(0, i + 1);
            Item temp = newItems[random];
            newItems[random] = newItems[i];
            newItems[i] = temp;
        }
        return newItems;
    }

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue("First");
        randomizedQueue.enqueue("Second");
        randomizedQueue.enqueue("Third");

        if (randomizedQueue.size() != 3) {
            throw new IllegalStateException("RandomizedQueue should have size 3");
        }

        Iterator<String> iterator = randomizedQueue.iterator();
        boolean isFound = false;
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("First")) {
                isFound = true;
            }
        }
        if (!isFound) {
            throw new IllegalStateException("Bad iterator for RandomizedQueue");
        }

        randomizedQueue.dequeue();
        if (randomizedQueue.size() != 2) {
            throw new IllegalStateException("RandomizedQueue should have size 2");
        }

        StdOut.println("Test is successful");
    }

}
