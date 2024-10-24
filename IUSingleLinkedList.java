import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-Linked node-based structure implemenetation of the index unsorted list
 * Includes a basic Iterator to support remove operations.
 * 
 * @author Kyle Truschel and CS221-3 F24
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    // Represents the beginning of the list.
    private Node<T> head;

    // Represents the end of the list.
    // Ensures addToRear is an O(1) operation.
    private Node<T> tail;

    // Similar to the rear in order to keep track of size.
    // Ensures size is an O(1) operation.
    private int size;

    // Assists the Iterator in comparing changes.
    private int versionNumber;

    /**
     * Initialize a new EMPTY list, no nodes to start.
     */
    public IUSingleLinkedList() {
        // Trick to assign right to left of the same type.
        this.head = this.tail = null;
        this.size = 0;
        this.versionNumber = 0;
    }

    @Override
    public void addToFront(T element) {
        // Cannot add to a link list without pointing to a node.
        // A new node must be created to hold the new element.
        // Take this new node and set its setNextNode reference to point to the head
        // node.
        // After the node is added, we must increment versionNumber and size.
        // If adding to an empty list, the tail needs to be incremented.
        // This also results in an O(1) operation, no loops and no need to calculate the
        // size.
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);

        // In the event the the linked list is empty we must assign the tail to also be
        // the newNode.
        // Since the isEmpty method checks size, we can use the method again without
        // issues
        if (isEmpty()) {
            tail = newNode;
        }

        // Assigning the head to become the new newNode object
        head = newNode;

        size++;
        versionNumber++;
    }

    @Override
    public void addToRear(T element) {
        // There will not be a need to point to a next node, since it is at the end of
        // the list;
        // there will be no nodes and only null will exist.
        // Similarly, this also results in an O(1) operation.
        Node<T> newNode = new Node<T>(element);

        // If the linked list is empty, every list requires a head to function, even
        // though we are adding
        // to rear.
        if (isEmpty()) {
            head = newNode;
        }
        // Otherwise, perform the function as normal, add the node to the rear as the
        // method implies.
        else {
            tail.setNextNode(newNode);
        }

        tail = newNode;
        size++;
        versionNumber++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> currentNode = head;
        boolean found = false;

        // For the beginning
        while (target != currentNode.getElement() && currentNode.getNextNode() != null) {
            currentNode = currentNode.getNextNode();
        }

        // for (int i = 0; i < size; i++) {
        // if (currentNode == currentNode.getElement()) {
        // found = true;
        // break;
        // }
        // else {
        // currentNode = currentNode.getNextNode();
        // }
        // }

        // If target was not found
        // if (!found) {
        // throw new NoSuchElementException();
        // }

        // if (currentNode == null) {
        // throw new NoSuchElementException();
        // }

        if (currentNode == tail) {
            add(element);
        } else {
            // Temp storage, the node after the target, which must be stored to later
            // reintegrate into the linked list.
            Node<T> tempNode = currentNode.getNextNode();

            // Create a new node to be added
            Node<T> newNode = new Node<T>(element);

            currentNode.setNextNode(newNode);

            // Now set the nextNode after X, back to C.
            newNode.setNextNode(tempNode);

            size++;
            versionNumber++;
        }
    }

    @Override
    public void add(int index, T element) {
        // Check is if the index is in bounds.
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        // First handle of the possible of adding at the front position, the head.
        // Simply call the addToFront method.
        if (index == 0) {
            addToFront(element);
        }

        else {
            // Now, handle the sceneario of adding elements in the middle.
            // For instance if we try add(2, E) we would need the reference at index 1, the
            // node
            // in front of the place of add (or remove).
            // We need a way to keep track of nodes, since we do not use indexing in linked
            // lists.
            Node<T> currentNode = head;

            // We know how many times we will move, since we have the index known, so we use
            // a for loop.
            // Stop in front of index, since if try to add at 2, we actually need to get 1,
            // which is 2 - 1.
            for (int i = 0; i < index - 1; i++) {
                // Assignment is from right to left, so currentNode now points at spot 1.
                currentNode = currentNode.getNextNode();
            }

            // First create a new node
            Node<T> newNode = new Node<T>(element);

            // Now attach the node at the spot of 2, using the getNextNode.
            newNode.setNextNode(currentNode.getNextNode());

            // Now assign our previously stored currentNode
            currentNode.setNextNode(newNode);

            // Did we add onto the very end of the list?
            // If the currentNode is equal to the tail, and we just added after currentNode,
            // we must update tail.
            if (newNode.getNextNode() == null) {
                tail = newNode;
            }
        }

        size++;
        versionNumber++;
    }

    @Override
    public T removeFirst() {
        // Overwrite the head to the next node
        Node<T> nextHead = head.getNextNode();
        T returnValue = head.getElement();
        head = nextHead;

        size--;
        versionNumber++;
        return returnValue;
    }

    @Override
    public T removeLast() {
        // Cannot remove if nothing exists
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T returnValue = tail.getElement();
        Node<T> currentNode = head;

        // If a one element list
        if (size == 1) {
            head = null;
            tail = null;

        } else {
            // Iterate up to the the node before tail.
            while (currentNode.getNextNode() != tail && head != tail) {
                currentNode = currentNode.getNextNode();
            }

            currentNode.setNextNode(null);
        }

        size--;
        versionNumber++;
        return returnValue;
    }

    @Override
    public T remove(T element) {
        // Very first check is if the list is empty.
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // Avoid using indexOf, since it will result in a search of n^2.
        // If we want to remove a known element, C, which is at spot 2 we need to find
        // the node before it
        // at spot 1.
        T returnValue = null;

        // Very first check is to see if the removal element is in the head node.
        if (element.equals(head.getElement())) {
            returnValue = head.getElement();

            // Whatever node was second, is now the first node, or the head.
            head = head.getNextNode();

            // In the event of a single element linked list, and we remove and element.
            if (head == null) {
                tail = null;
            }
        } else {
            // We must use nextNode, getElement, and equals method for this to function.
            Node<T> currentNode = head;

            // Seek out the node before the desired removal node.
            // Ensure the tail node does not keep seeking, since it would result in a null
            // pointer; if currentNode
            // hits tail, there are no other candidates to search.
            // As long as the currentNode searching DOES NOT equal the desired element, keep
            // searching.
            while (currentNode.getNextNode() != null && !currentNode.getNextNode().getElement().equals(element)) {
                currentNode = currentNode.getNextNode();
            }

            // If the node is the tail, or null, throw a no such element exception.
            if (currentNode.getNextNode() == null) {
                throw new NoSuchElementException();
            }

            // If this while loop completes successfully, we must store the value we are
            // about to remove.
            returnValue = currentNode.getNextNode().getElement();

            // Node hopping. We must ensure the node pointing reference is one after the
            // element we wish to remove.
            currentNode.setNextNode(currentNode.getNextNode().getNextNode());

            // Possibility of removing the last node, we must update the tail.
            if (currentNode.getNextNode() == null) {
                tail = currentNode;
            }
        }

        size--;
        versionNumber++;
        return returnValue;
    }

    @Override
    public T remove(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, T element) {
        Node<T> currentNode = head;

        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        else {
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.getNextNode();
            }
            Node<T> tempNode = currentNode.getNextNode();
            Node<T> newNode = new Node<T>(element);
            currentNode.setNextNode(newNode);
            newNode.setNextNode(tempNode);
        }
        
        versionNumber++;
    }

    @Override
    public T get(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public int indexOf(T element) {
        // Concept is to navigate through the list using node references, always start
        // at the head.
        // Check if the current node is equal to the node storing the desired element.
        // If not, advance the current node and incremenet a current node count.
        // If current is equal to null, one past the tail, then the node containing the
        // element
        // was not found.
        Node<T> currentNode = head;
        int currentIndex = 0;

        // While loop since we do not know the exact size.
        // The equals() method compares object contents.
        // As long as the current node is not null, the end of the list, and the element
        // is not found.
        while (currentNode != null && !currentNode.getElement().equals(element)) {
            // The getNextNode() method retrieves the address of the second node first,
            // which then.
            // overwrites the address of the actual current node.
            // Right side of the statement is figured out first, and then the variable is
            // updated.
            currentNode = currentNode.getNextNode();

            // The mode has shifted, now update the current index.
            currentIndex++;
        }

        // If the currentNode is null, we have reached the end of the list.
        if (currentNode == null) {
            // Best practice to prevent a "short circuit" in a loop, assign currentIndex to
            // -1 to
            // only have ONE return statement.
            currentIndex = -1;
        }

        // If the element was found, return the current index.
        // Following the best practice, only ONE return statement should exist.
        return currentIndex;
    }

    @Override
    public T first() {
        // Conditional check to see if an exception must be thrown according to the
        // interface.
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // Must retrieve the node's contents, in this case, the head's contents.
        return head.getElement();
    }

    @Override
    public T last() {
        // Conditional check to see if an exception must be thrown according to the
        // interface.
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // Must retrieve the node's contents, in this case, the tail's contents.
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        // Simply calls the indexOf method and if it passes, which would mean
        // currentIndex is
        // not assigned to -1, the value of the target will exist and be returned to the
        // console.
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        // Emphasize clarity and concise information.
        return size == 0;

        // Alternative statement:
        // If head is null, it cannot possibly wrong since if head is lost, the list is
        // lost.
        // return head == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (T element : this) {
            stringBuilder.append(element.toString());
            stringBuilder.append(", ");
        }

        if (size() > 0) {
            // Remove trailing comma.
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
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