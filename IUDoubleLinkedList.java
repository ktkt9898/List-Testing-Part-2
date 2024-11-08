import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double linked node base implementation of IndexUnsortedList that supports
 * basic iterators and list iterators
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int versionNumber;

    /**
     * Initialzie a new empty list
     */
    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        versionNumber = 0;
    }

    @Override
    public void addToFront(T element) {
        // Similar to single linked list, we essentially overwrite the head to the new node
        Node<T> newNode = new Node<T>(element);

        // If empty, tail also must be the newNode [] to [A] where A is both the tail and the head.
        if (isEmpty()) {
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
            newNode = tail = head;
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
        
        // While the targetNode is not null, since the addAfter could be trying to add at tail.
        // and keep searching until we retrieve the node's element.
        while (targetNode != null && !targetNode.getElement().equals(target)) {
            targetNode = targetNode.getNextNode();
        }

        // If the target is null, we never found the target value in the list
        if (targetNode == null) {
            throw new NoSuchElementException();
        }

        // For adding to the middle and to the head/start of a list:
        // Always start by connecting where the newNode is placed so we do not lose our last references
        // 1. Create a node first
        Node<T> newNode = new Node<T>(element);

        // 2. Set the node after targetNode to newNode. Attach newNode first
        newNode.setNextNode(targetNode.getNextNode());
        
        // 3. Set previous to target Node
        newNode.setPreviousNode(targetNode);

        // 4. NOW set targetNode's forward reference, the next node, to the newNode
        targetNode.setNextNode(newNode);

        // 5. lastly, set the newNode's reference to point back to the previous node in the double chain.
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
            if (isEmpty()) {
                // If empty, head and tail are the newNode
                head = tail = newNode;
            } else {
                // If not empty, inserting at 0 means this new Node points to the old head, and will later become
                // the new head
                newNode.setNextNode(head);
                head.setPreviousNode(newNode);
                // Overwrite the old head to become the new head
                head = newNode;
            }
        } else if (index == size()) {
            // Maximum index value, this will add at the tail
            newNode.setPreviousNode(tail);
            tail.setNextNode(newNode);
            tail = newNode;
        } else {
            // Inserting node in the middle of the list, so if [A, B, C], and we try add(1,E) we account index - 1,
            // to become 1 - 1 = 0. We do not enter the loop in this situation so currentNode stays as head, or index 1,
            // or A
            Node<T> currentNode = head;
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.getNextNode();
            }
            // Calling currentNode, which is currently A, getNext will retrieve B, and we use this later
            Node<T> afterIndexNode = currentNode.getNextNode();
            
            // 1. currentNode is at A, calling setNextNode to be A's next node for newNode, E,
            // means we have [A, E, B, C]
            currentNode.setNextNode(newNode);

            // 2. Set E to have its backward pointing back to C
            newNode.setPreviousNode(currentNode);

            // 3. E now needs to point forward to B, which was stored earlier
            newNode.setNextNode(afterIndexNode);

            // 4. Lastly, B needs to point backwards to E
            afterIndexNode.setPreviousNode(newNode);
        }
    
        size++;
        versionNumber++;
        // if (index >= size() || index < 0) {
        //     throw new IndexOutOfBoundsException();
        // }

        // // If adding at 0, the head
        // if (index == 0) {
        //     addToFront(element);
        // }
        // else {
        //     // Start at the head
        //     Node<T> currentNode = head;

        //     // [A, B, C, D] 0, 1, 2, 3 and we want to add E at index 2, we go up to index 2 but not including, so we stop at
        //     // index 1, B and call get next which assings currentNode to be C, the correct index
        //     for (int i = 0; i < index; i++) {
        //         currentNode = currentNode.getNextNode();
        //     }
            
        //     Node<T> newNode = new Node<T>(element);
        //     Node<T> tempNext = currentNode.getNextNode();

        //     if (tempNext != null) {
        //         // Set E to have it's next Node point to D
        //         newNode.setNextNode(tempNext);

        //         // We stored the original next of C, which was D, so
        //         tempNext.setPreviousNode(newNode); 
        //     }
        //     else {
        //         // tempNext being null means we are at the end of the list, therefore
        //         // the newNode is the tail
        //         newNode.setNextNode(null);
        //         tail = newNode;
        //     }

        //     // Set C to have it's next Node point to E
        //     currentNode.setNextNode(newNode);

        //     // Set E's previous Node to point to C
        //     newNode.setPreviousNode(currentNode);
        //     // [A, B, C, E, D]
        // }
        // size++;
        // versionNumber++;
    }

    @Override
    public T removeFirst() {
        // // Use the list iterator
        // ListIterator<T> lit = listIterator();
        // T returnValue = lit.next();
        // lit.remove();
        // return returnValue;

        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T returnValue = head.getElement();

        // Overwrite head by pointing to the next node
        head = head.getNextNode();

        // [A] only for single element list
        if (head == null) {
            tail = null;
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
            head = tail = null;
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
        // if (isEmpty()) {
        //     throw new NoSuchElementException();
        // }

        // Could use the list iterator
        ListIterator<T> lit = listIterator();
        T returnValue = null;

        boolean found = false;
        while (lit.hasNext() && !found) {
            returnValue = lit.next();
            if (returnValue.equals(element)) {
                found = true;
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }
        lit.remove();
        return returnValue;

        // Using a standard method signature
        // Node<T> targetNode = head;
        // Node<T> tempNext = null;
        // Node<T> tempPrev = null;

        // // Check at the head
        // // [A, B, C]
        // if (element.equals(targetNode.getElement())) {
        //     return removeFirst();
        // }
        
        // // Check at the tail
        // else if (tail.getElement().equals(element)) {
        //     return removeLast();
        // }

        // // Check in the middle
        // // If we are removing B from [A, B, C]
        // else {
        //     while (targetNode.getNextNode() != null && !targetNode.getElement().equals(element)) {
        //         targetNode = targetNode.getNextNode();
        //         if (targetNode.equals(tail)) {
        //             throw new NoSuchElementException();
        //         }
        //     }
        //     // Temp next is C, temp prev is B
        //     tempNext = targetNode.getNextNode();
        //     tempPrev = targetNode.getPreviousNode();

        //     // Now update the connection after B is unlinked
        //     // Now set A to point to C
        //     tempPrev.setNextNode(tempNext);
            
        //     // Set C previous point to A
        //     tempNext.setPreviousNode(tempPrev);

        //     size--;
        //     versionNumber++;
        // }

        // return targetNode.getElement();
    }

    @Override
    public T remove(int index) {
        // Index is always one less than size, so we must also check it if equals
        // [A, B, C] has size of 3 but max index of 2 (0, 1, 2).
        if (index >= size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        // // Could also use the Iterator's remove functionality
        // // In front of the given index
        // ListIterator<T> lit = listIterator(index);
        // T returnValue = lit.next();
        // // remove the last element we returned, above
        // lit.remove();
        // return returnValue;

        // Always start at the beginning
        Node<T> currentNode = head;

        // Unlike with a single list, we can go directly to the node.
        // [A, B, C] and we want index 2, which is C, we keep going
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNextNode();
        }

        // The idea is to ensure the references for the forward and previous do not point to C.
        // If we are removing head, we must ensure the previous node does not result in a null pointer exception.
        // If this is true, we must ensure the nextNode is the new head, so [A, B, C] and we remove A, then B
        // becomes the new head.
        if (index == 0) {
            head = currentNode.getNextNode();
        }
        else {
            currentNode.getPreviousNode().setNextNode(currentNode.getNextNode());
        }

        // Now check if we are modifying the end, the tail
        // If this is the case, we can move backwards with a double link and set the tail to the previous Node
        // So [A, B, C] and we remove C, then B is the new tail.
        if (currentNode == tail) {
            tail = currentNode.getPreviousNode();
        }
        else {
            currentNode.getNextNode().setPreviousNode(currentNode.getPreviousNode());
        }

        // Now the node can be handled by java's garbage collection
        size--;
        versionNumber++;

        // We never modified currentNode, so we can retrieve it
        return currentNode.getElement();
    }

    @Override
    public void set(int index, T element) {
        // Check if index is in bounds
        if (index >= size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        // Start at the head
        Node<T> targetNode = head;

        // In the case of a single element list
        if (index == 0) {
            targetNode.setElement(element);
        }

        // Go up to the element index previous value
        // So, for [A, B, C, D] and we call set(2, F) we really want to replace C at index 2
        // Go up to index but not including, which is 2, so we stop at B and call getNextNode
        // targetNode is now updated to become C
        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNextNode();
        }

        // Now call setElement onto the index 2 node to hold element F or whatever element value.
        targetNode.setElement(element);

        versionNumber++;
    }

    @Override
    public T get(int index) {
        if (index >= size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        T returnValue = null;

        // In the case of index is 0, the head
        if (index == 0) {
            returnValue = head.getElement();
        }

        Node<T> targetNode = head;

        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNextNode();
        }

        returnValue = targetNode.getElement();

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

    // Just need to make one iteartor class, since we can inherit all the methods of the standard iterator
    @Override
    public Iterator<T> iterator() {
        // Inherits the standard java iterator methods to be used as a basic iterator
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        // Inherits the basic iterator methods but also the added functionality of the list iterator
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    /**
     * ListIterator for use with a Double Linked List, and also inherits a basic iterator
     */
    private class DLLIterator implements ListIterator<T> {
        // Only requires a nextNode, iterators purpose is to navigate beginning to end of the list
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
         * @param startIndex takes in a given index value to start
         */
        public DLLIterator(int startIndex) {
            // Bounds check, allows the start before null but nothing after
            // Does not allow -1
            if (startIndex < 0 || startIndex > size) {
                throw new IndexOutOfBoundsException();
            }

            // For a large index, could also determine the best starting point instead of always from the beginning
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

            T returnValue = nextNode.getPreviousNode().getElement();

            // Similar to next() but we decrement the nextIndex
            nextNode.getPreviousNode();
            nextIndex--;
            return returnValue;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
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

            // If the lastReturnNode is not equal to nextNode, the last move was next to the right
            if (lastReturnedNode != nextNode) { // last move was next
                nextIndex--; // fewer nodes to the left than there used to be
            }
            else { // last move was previous
                // [A, B, C, D] iterator was in front of C, but after removal it is now in front of D
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
            // TODO Auto-generated method stub
            // No restrictions on how many times it can be called
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }

        @Override
        public void add(T e) {
            // TODO Auto-generated method stub
            // Reset lastReturnedNode
            throw new UnsupportedOperationException("Unimplemented method 'add'");
        }
    }
}