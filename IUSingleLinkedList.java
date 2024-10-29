import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> targetNode = head;
        Node<T> newNode = new Node<T>(element);

        // Add a new node at the beginning head.
        if (size == 1) {
            head.setNextNode(newNode);
        }

        else {
            while (targetNode != null && !targetNode.getElement().equals(target)) {
                targetNode = targetNode.getNextNode();
            }
            // Add a new node in the middle
            if (targetNode == null) {
                throw new NoSuchElementException();
            }

            newNode.setNextNode(targetNode.getNextNode());
            newNode.setElement(element);

            if (targetNode == tail) {
                tail = targetNode;
            }
        }

        size++;
        versionNumber++;
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
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // Overwrite the head to the next node
        Node<T> returnValue = head;
        head = returnValue.getNextNode();

        size--;
        versionNumber++;
        return returnValue.getElement();
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
            Node<T> startNode = null;
            // Size is less than one because we will eventually remove the head and shift down by
            // one
            for (int i = 0; i < size - 1; i++) {
                startNode = currentNode;
                currentNode = currentNode.getNextNode();
            }
            tail = startNode;
            tail.setNextNode(null);
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
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        
        Node<T> currentNode = head;

        // Assign return value to be null at the start.
        T returnValue = null;

        if (size == 1) {
            returnValue = head.getElement();
            head = null;
            tail = null;
        }
        else {
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.getNextNode();
            }
            // [1, 2, 3, 4, null] and we want to remove 3, at index 2.
            // We need the number 2 node to point to number 4. If nothing points to node
            // 3, then the java garbage collector will automatically remove it.
            // Return value is 3.
            returnValue = currentNode.getNextNode().getElement();
            Node<T> nodeAtEnd = currentNode.getNextNode().getNextNode();
            currentNode.setNextNode(nodeAtEnd);

            if (nodeAtEnd == null) {
                tail = currentNode;
            }
        }
        size--;
        versionNumber++;
        return returnValue;
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
        // Start at the head.
        Node<T> currentNode = head;

        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        else {
            for (int i = 0; i < index - 1; i++) {
                // Incremenet up to the input index - 1, to avoid retrieving null.
                currentNode = currentNode.getNextNode();
            }
        }

        // Return the element value at the index that was parsed thru in the for loop.
        return currentNode.getElement();
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
        return new SLLIterator(); // Basic iterator, not to be confused with listIterator (for double linked lists)
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

    /**
     * Iterator class to use within IUSingleLinkedList, to avoid breaking encapsulation.
     * Do not need to include a generic <T> in the private class. This would create a shadow generic.
     */
    private class SLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private boolean canRemove;
        private int iterVersionNumber;

        /**
         * Constructor to initialize the Iterator
         */
        public SLLIterator() {
            // Very first "next" is the head
            nextNode = head;
            canRemove = false;
            iterVersionNumber = versionNumber;
        }

        @Override
        public boolean hasNext() {
            // If something has changed, throw a concurrent mod exception.
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            // Return true if the nextNode is not null.
            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T returnValue = nextNode.getElement();
            nextNode = nextNode.getNextNode();
            canRemove = true;
            return returnValue;
        }

        @Override
        public void remove() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            if (!canRemove) { // Ensure the method call is valid, from next() switching to true.
                throw new IllegalStateException();
            }
            canRemove = false;
            // Declare outside to refractor and avoid code duplication for the if checks.
            Node<T> prevPrevNode = null;
            
            // Scenario where we remove at the head in a normal sized list
            // Simply point to the next node and let the java garbage collector remove the original head.
            if (head.getNextNode() == nextNode) {
                head = nextNode;
            }

            else {
                // Normal scenario to remove in the middle of the linked list.
                // If we want to remove C from [A, B, C, D] we must locate the node before since we have to think using
                // getNextNode twice, which would be A.
                prevPrevNode = head;

                // As long as advancing twice does NOT reference C, keep searching.
                // In the exampple, this while loop would retrieve B after execution.
                while (prevPrevNode.getNextNode().getNextNode() != nextNode) {
                    prevPrevNode = prevPrevNode.getNextNode();
                }

                // To remove C, we must set B to point to D.
                // Set prevPrevNode to the nextNode of C, which is D.
                prevPrevNode.setNextNode(nextNode);
            }

            // Now handle the case where the nextNode is null, which the tail needs to be updated.
            // Also avoids code duplication.
            if (nextNode == null) {
                tail = prevPrevNode;
            }

            versionNumber++;
            iterVersionNumber++;
            size--;
        }

    } // End of Iterator class
} // End of IUSingleLinkedList class