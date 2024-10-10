import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUArrayList<T> implements IndexedUnsortedList<T> {
    public static final int DEFAULT_CAPACITY = 10;
    private T[] array;
    private int rear;

    /**
     * Default constructor that creates an array of size 10, the 
     * default capacity, to start a list
     */
    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Second constructor that can take in a user specified array size to start a list
     * 
     * @param initialCapacity An integer value that will serve as the array
     * size
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity) {
        array = (T[])(new Object[initialCapacity]);
        rear = 0;
    }

    /**
     * Double list capacity if necessary before adding.
     */
    private void expandIfNecessary() {
        if (array.length == rear) {
            // Out of room
            // Use the Array class to copy and overwrite the list now with twice the size
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    @Override
    public void addToFront(T element) {
        expandIfNecessary();
        // Shift everything in the list by one index position
        // Use a for loop since we have a range value known
        // Index must be greater than 0 since nothing is to the left of 0
        // This also introduces an O(n) growth factor, where n is the elements in the list.
        for (int i = rear; i > 0; i--) {
            // index - 1 is the left value to the current index position
            array[i] = array[i - 1];
        }
        
        // Now assign the element to the front
        array[0] = element;

        // Rear now needs to be updated after an add
        rear++;
    }

    @Override
    public void addToRear(T element) {
        expandIfNecessary();
        // Rear would be the last spot prior to expanding
        array[rear] = element;

        // Incremenet rear
        rear++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
    }

    @Override
    public void add(int index, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public T removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public T removeLast() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
    }

    @Override
    public T remove(T element) {
        // Search once for the element at the first occurence
        // using the already tested indexOf method
        int index = indexOf(element);

        // Conditional check if not found
        if ( index < 0) {
            throw new NoSuchElementException();
        }

        // Store the return value before trashing it, we need to provide this later
        // according to the interface javadoc
        T returnValue = array[index];

        // Cannot start at the back, since it will overwrite values
        // Start at the first value to be index
        // Last index would now be rear - 2 (or up to and not including
        // rear - 1)
        // From this : [a, b, c, rear]
        // to now this after removing b: [a, c, , rear]
        for (int i = index; i < rear - 1; i++) {
            array[i] = array[i + 1];
        }

        // Decrement rear since it is out of poisition by one
        rear--;

        // Now we must prevent a memory leak by removing the reference to the 
        // element value that was removed by setting to null
        array[rear] = null;

        // Return the removed value according to the interface javadoc
        return returnValue;
    }

    @Override
    public T remove(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(T element) {
        int returnIndex = -1;
        int currentIndex = 0;
    
        while (returnIndex < 0 && currentIndex < rear) {
            // the '==' operater checks object address
            // equals() method compares contents
            if (element.equals(array[currentIndex])) {
                returnIndex = currentIndex;
            }
                
            else {
                currentIndex++;
            }
        }
        return returnIndex;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[rear-1];
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }
    
}
