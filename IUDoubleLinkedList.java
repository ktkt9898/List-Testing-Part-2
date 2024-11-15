import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double linked node base implementation of IndexUnsortedList that supports
 * basic iterators and list iterators
 * 
 * @author Kyle Truschel and CS221-3 F24
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int versionNumber;

    /**
     * Initialzie a new empty list
     */
    public IUDoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        versionNumber = 0;
    }

    @Override
    public void addToFront(T element) {
        // Similar to single linked list, we essentially overwrite the head to the new
        // node
        Node<T> newNode = new Node<T>(element);

        // If empty, tail also must be the newNode [] to [A] where A is both the tail
        // and the head.
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }

        // Only perform if NOT empty
        else {
            // Update the original head to refer back to the newNode we are inserting
            newNode.setNextNode(head);
            head.setPreviousNode(newNode);
        }

        // Now update the final list's head to be the newNode.
        head = newNode;
        size++;
        versionNumber++;
    }

    @Override
    public void addToRear(T element) {
        // 1. Create a new Node
        Node<T> newNode = new Node<T>(element);

        // List has no element values then check
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }

        // List has element values
        else {
            // Start at the end, the rear, which is tail
            Node<T> targetNode = tail;

            // 2. Set the targetNode, tail, to the newNode
            targetNode.setNextNode(newNode);

            // 3. Set newNode's previous to targetNode
            newNode.setPreviousNode(targetNode);

            // 4. Update tail to be the newNode
            tail = newNode;
        }

        size++;
        versionNumber++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        // Start at the beginning, the head
        Node<T> targetNode = head;

        // While the targetNode is not null, since the addAfter could be trying to add
        // at tail.
        // and keep searching until we retrieve the node's element.
        while (targetNode != null && !targetNode.getElement().equals(target)) {
            targetNode = targetNode.getNextNode();
        }

        // If the target is null, we never found the target value in the list
        if (targetNode == null) {
            throw new NoSuchElementException();
        }

        // For adding to the middle and to the head/start of a list:
        // Always start by connecting where the newNode is placed so we do not lose our
        // last references
        // 1. Create a node first
        Node<T> newNode = new Node<T>(element);

        // 2. Set the node after targetNode to newNode. Attach newNode first
        newNode.setNextNode(targetNode.getNextNode());

        // 3. Set previous to target Node
        newNode.setPreviousNode(targetNode);

        // 4. NOW set targetNode's forward reference, the next node, to the newNode
        targetNode.setNextNode(newNode);

        // 5. lastly, set the newNode's reference to point back to the previous node in
        // the double chain.
        // For adding at the tail [A, B, C, D] so D:
        // Step 5 would result in a null pointer exception.
        if (newNode.getNextNode() != null) {
            newNode.getNextNode().setPreviousNode(newNode);
        }

        // If the nextNode IS null, the newNode is now the new tail.
        else {
            tail = newNode;
        }

        size++;
        versionNumber++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<T>(element);

        if (index == 0) {
            addToFront(element);
        } 
        else if (index == size()) {
            addToRear(element);
        } 
        else {
            // Inserting node in the middle of the list, so if [A, B, C], and we try
            // add(1,E) we account index - 1,
            // to become 1 - 1 = 0. We do not enter the loop in this situation so targetNode
            // stays as head, or index 1,
            // or A
            Node<T> targetNode = head;
            for (int i = 0; i < index - 1; i++) {
                targetNode = targetNode.getNextNode();
            }
            // Calling targetNode, which is currently A, getNext will retrieve B, and we use
            // this later
            Node<T> afterIndexNode = targetNode.getNextNode();

            // 1. targetNode is at A, calling setNextNode to be A's next node for newNode,
            // E,
            // means we have [A, E, B, C]
            targetNode.setNextNode(newNode);

            // 2. Set E to have its backward pointing back to C
            newNode.setPreviousNode(targetNode);

            // 3. E now needs to point forward to B, which was stored earlier
            newNode.setNextNode(afterIndexNode);

            // 4. Lastly, B needs to point backwards to E
            afterIndexNode.setPreviousNode(newNode);

            size++;
            versionNumber++;
        } 
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T returnValue = head.getElement();

        // [A] only for single element list
        if (size() == 1) {
            head = null;
            tail = null;
        }

        else {
            // Overwrite head by pointing to the next node
            head = head.getNextNode();
        }

        size--;
        versionNumber++;
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T returnValue = tail.getElement();

        // For one element list
        if (size() == 1) {
            head = null;
            tail = null;
        }

        else {
            tail = tail.getPreviousNode();

            // Update the new null position
            tail.setNextNode(null);
        }

        size--;
        versionNumber++;
        return returnValue;
    }

    @Override
    public T remove(T element) {
        Node<T> targetNode = head;

        // Continue to search until we get an exact match for the element
        while (targetNode != null && !targetNode.getElement().equals(element)) {
            targetNode = targetNode.getNextNode();
        }

        // If no element was found, meaning targetNode reached the end of the list, the
        // element does not exist
        if (targetNode == null) {
            throw new NoSuchElementException();
        }

        // Perform checks at various edge cases after the targetNode was found
        // If not the head, the targetNode was found in the middle
        // Set the targetNode's previous Node to the targetNode's next
        // Ex. [A, B, C] and we remove B, set A to to point to C
        if (targetNode != head) {
            targetNode.getPreviousNode().setNextNode(targetNode.getNextNode());
        } 

        // Simply "overwrite" the head by pointing it to the targetNode's next
        else {
            head = targetNode.getNextNode();
        }

        // Similarly, if not the tail set the targetNode's next Node to be targetNode's
        // previous
        // Ex. [A, B, C] and we already have A pointing to C, we would point C to A.
        if (targetNode != tail) {
            targetNode.getNextNode().setPreviousNode(targetNode.getPreviousNode());
        } 

        // Simply "overwrite" the tail by pointing it to the targetNode's previous
        else {
            tail = targetNode.getPreviousNode();
        }

        size--;
        versionNumber++;

        // Never modified the targetNode after the four scenarios,
        // so we can retrieve it at the end
        return targetNode.getElement();
    }

    @Override
    public T remove(int index) {
        // Index is always one less than size, so we must also check it if equals
        // [A, B, C] has size of 3 but max index of 2 (0, 1, 2).
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        // Always start at the beginning
        Node<T> targetNode = head;

        // Unlike with a single list, we can go directly to the node.
        // [A, B, C] and we want index 2, which is C, we keep going
        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNextNode();
        }

        // The idea is to ensure the references for the forward and previous do not
        // point to C.
        // If we are removing head, we must ensure the previous node does not result in
        // a null pointer exception.
        // If this is true, we must ensure the nextNode is the new head, so [A, B, C]
        // and we remove A, then B
        // becomes the new head.
        if (index == 0) {
            head = targetNode.getNextNode();
        } else {
            targetNode.getPreviousNode().setNextNode(targetNode.getNextNode());
        }

        // Now check if we are modifying the end, the tail
        // If this is the case, we can move backwards with a double link and set the
        // tail to the previous Node
        // So [A, B, C] and we remove C, then B is the new tail.
        if (targetNode == tail) {
            tail = targetNode.getPreviousNode();
        } else {
            targetNode.getNextNode().setPreviousNode(targetNode.getPreviousNode());
        }

        // Now the node can be handled by java's garbage collection
        size--;
        versionNumber++;

        // Similar to the remove element,
        // we never modified targetNode, so we can retrieve it
        return targetNode.getElement();
    }

    @Override
    public void set(int index, T element) {
        // Check if index is in bounds
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        // Start at the head
        Node<T> targetNode = head;

        // In the case of a single element list
        if (index == 0) {
            targetNode.setElement(element);
        }

        else {
            // Go up to the element index previous value
            // So, for [A, B, C, D] and we call set(2, F) we really want to replace C at
            // index 2
            // Go up to index but not including, which is 2, so we stop at B and call
            // getNextNode
            // targetNode is now updated to become C
            for (int i = 0; i < index; i++) {
                targetNode = targetNode.getNextNode();
            }
            // Now call setElement onto the index 2 node to hold element F or whatever
            // element value.
            targetNode.setElement(element);
        }

        versionNumber++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        T returnValue;

        // In the case of index is 0, the head
        if (index == 0) {
            returnValue = head.getElement();
        }

        else {
            Node<T> targetNode = head;

            for (int i = 0; i < index; i++) {
                targetNode = targetNode.getNextNode();
            }
            returnValue = targetNode.getElement();
        }

        return returnValue;
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
        Node<T> targetNode = head;
        int currentIndex = 0;

        // While loop since we do not know the exact size.
        // The equals() method compares object contents.
        // As long as the current node is not null, the end of the list, and the element
        // is not found.
        while (targetNode != null && !targetNode.getElement().equals(element)) {
            // The getNextNode() method retrieves the address of the second node first,
            // which then.
            // overwrites the address of the actual current node.
            // Right side of the statement is figured out first, and then the variable is
            // updated.
            targetNode = targetNode.getNextNode();

            // The mode has shifted, now update the current index.
            currentIndex++;
        }

        // If the targetNode is null, we have reached the end of the list.
        if (targetNode == null) {
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

    // Just need to make one iteartor class, since we can inherit all the methods of
    // the standard iterator
    @Override
    public Iterator<T> iterator() {
        // Inherits the standard java iterator methods to be used as a basic iterator
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        // Inherits the basic iterator methods but also the added functionality of the
        // list iterator
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    /**
     * ListIterator for use with a Double Linked List, and also inherits a basic
     * iterator
     */
    private class DLLIterator implements ListIterator<T> {
        // Only requires a nextNode, iterators purpose is to navigate beginning to end
        // of the list
        // If we require the preivous node, we call getPreviousNode
        // Only time nextNode is null is at the end of the list

        private Node<T> nextNode;

        // nextIndex can return the previous by considering nextIndex - 1
        private int nextIndex;
        private int iterVersionNumber;

        // Concept for removing next and previous
        // Compare last returned Node and next Node for which direction
        private Node<T> lastReturnedNode;

        /**
         * Iterator that starts at the beginning of the list
         */
        public DLLIterator() {
            nextNode = head;
            nextIndex = 0;
            iterVersionNumber = versionNumber;
            lastReturnedNode = null;

            // Or call the second constructor at startIndex of 0
        }

        /**
         * Iterator that can start at a desired index
         * 
         * @param startIndex takes in a given index value to start
         */
        public DLLIterator(int startIndex) {
            // Bounds check, allows the start before null but nothing after
            // Does not allow -1
            if (startIndex < 0 || startIndex > size()) {
                throw new IndexOutOfBoundsException();
            }

            // For a large index, could also determine the best starting point instead of
            // always from the beginning
            nextNode = head;

            // Go up to the index, call next node so we are at the exact index
            for (int i = 0; i < startIndex; i++) {
                nextNode = nextNode.getNextNode();
            }

            nextIndex = startIndex;
            iterVersionNumber = versionNumber;
            lastReturnedNode = null;
        }

        @Override
        public boolean hasNext() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            // Return true if the nextNode is not null
            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Store the next Node's element before moving
            T returnValue = nextNode.getElement();

            // Need to store the lastReturnedNode
            lastReturnedNode = nextNode;

            // Now move AND incremenet index value
            nextNode = nextNode.getNextNode();
            nextIndex++;
            return returnValue;
        }

        @Override
        public boolean hasPrevious() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            // Only one place in the list that doesn't have a previous, which is the head
            // Return true if the nextNode is not head
            return nextNode != head;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (nextNode == null) {
                nextNode = tail;
            } else {
                nextNode = nextNode.getPreviousNode();
            }

            // T returnValue = nextNode.getElement();

            // Need to store the lastReturnedNode
            lastReturnedNode = nextNode;

            // Similar to next() but we decrement the nextIndex
            nextIndex--;
            return nextNode.getElement();
        }

        @Override
        public int nextIndex() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            // Similar to canRemove being false
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }

            // In the middle of a list
            if (lastReturnedNode != head) {
                // Efficient way to set a list [A, B, C] to remove B by pointing A to C
                lastReturnedNode.getPreviousNode().setNextNode(lastReturnedNode.getNextNode());
            }
            // In the event there is no lastReturnedNode and the list contains values
            // Assign the head to simply become the next Node to become the head
            else {
                head = head.getNextNode();
            }

            if (lastReturnedNode != tail) {
                // Now update C to A, which would have been B's next Node
                lastReturnedNode.getNextNode().setPreviousNode(lastReturnedNode.getPreviousNode());
            }
            // Retrieve the value to the left, the previous, of the tail
            // For [A, B, C] and we are at C, the last previous node was B
            // B should now become the tail, the previous node of C
            else {
                tail = tail.getPreviousNode();
            }

            // If the lastReturnNode is not equal to nextNode, the last move was next to the
            // right
            if (lastReturnedNode != nextNode) { // last move was next
                nextIndex--; // fewer nodes to the left than there used to be
            } else { // last move was previous
                     // [A, B, C, D] iterator was in front of C, but after removal it is now in front
                     // of D
                nextNode = nextNode.getNextNode();
            }

            // Now "switch" the ability to remove to false by setting lastReturned node
            lastReturnedNode = null;
            size--;
            versionNumber++;
            iterVersionNumber++;
        }

        @Override
        public void set(T e) {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            // Similar to canRemove being false
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }

            // Simply just need to set the element at the last returned node
            lastReturnedNode.setElement(e);

            versionNumber++;
            iterVersionNumber++;
        }

        @Override
        public void add(T e) {
            if (iterVersionNumber != versionNumber) {
                throw new ConcurrentModificationException();
            }

            // For an empty list, head is null, so we assign a newNode to become the head
            // and tail
            if (head == null) {
                Node<T> newNode = new Node<T>(e);
                head = newNode;
                tail = newNode;
                size++;
                versionNumber++;
            }

            // If the nextNode is equal to head, meaning a list has elements, we simply add
            // to Front, which would be
            // the value before the iterator's cursor
            else if (nextNode == head) {
                addToFront(e);
            }

            // If the nextNode is equal to null, we want to add the tail
            else if (nextNode == null) {
                addToRear(e);
            }

            else {
                Node<T> newNode = new Node<T>(e);
                // [A,/\ B, C, D] cursor is adding between A and B, and iterator will return B
                // as its next value
                // First create a temp variable that retrieves A, the nextNode, B, previous Node
                Node<T> prevNode = nextNode.getPreviousNode();

                // If we add X, call A to set its next node to be X, the newNode
                prevNode.setNextNode(newNode);

                // Set X's next Node to be B
                newNode.setNextNode(nextNode);

                // Set B's previous node to X
                nextNode.setPreviousNode(newNode);

                // Set X's previous node to be A
                newNode.setPreviousNode(prevNode);

                size++;
                versionNumber++;
            }

            nextIndex++;
            iterVersionNumber++;
        }
    }
}