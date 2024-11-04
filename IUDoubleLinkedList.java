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
        newNode.setNextNode(head);

        // Update the original head to refer back to the newNode we are inserting
        // If empty, tail also must be the newNode [] to [A] where A is both the tail and the head.
        if (isEmpty()) {
            tail = newNode;
        }
        // Only perform if NOT empty
        else {
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
        // if (isEmpty()) {
        //     throw new NoSuchElementException();
        // }

        // Start at the head
        Node<T> targetNode = head;

        while (targetNode != null && !targetNode.getElement().equals(element)) {
            targetNode = targetNode.getNextNode();
        }

        if (targetNode == null) {
            throw new NoSuchElementException();
        }

        if (targetNode != tail) {
            targetNode.getNextNode().setPreviousNode(targetNode.getPreviousNode());
        }
        else {
            tail = targetNode.getPreviousNode();
        }

        if (targetNode != head) {
            targetNode.getPreviousNode().setNextNode(targetNode.getNextNode());
        }
        else {
            head = targetNode.getNextNode();
        }

        // Node<T> targetNode = head;
        // Node<T> tempNext = null;
        // Node<T> tempPrev = null;

        // // Check at the head
        // // [A, B, C]
        // if (element.equals(targetNode.getElement())) {
        //     head = head.getNextNode();
        //     // [A] only for single element list
        //     if (head == null) {
        //         tail = null;
        //     }
        // }
        
        // // Check at the tail
        // else if (tail.getElement().equals(element)) {
        //     // Update the new tail
        //     // [A, B, C] and we remove C, then B is the new tail
        //     // First store the old tail, which is C
        //     targetNode = tail;

        //     // Now update the new tail to B
        //     tail = tail.getPreviousNode();

        //     // Update the new null position
        //     tail.setNextNode(null);
        // }

        // // Check in the middle
        // // If we are removing B from [A, B, C]
        // else {
        //     while (targetNode.getNextNode() != null && !targetNode.getElement().equals(element)) {
        //         targetNode = targetNode.getNextNode();
        //     }
        //     // Temp next is C, temp prev is B
        //     tempNext = targetNode.getNextNode();
        //     tempPrev = targetNode.getPreviousNode();

        //     // Now update the connection after B is unlinked
        //     // Now set A to point to C
        //     tempPrev.setNextNode(tempNext);
            
        //     // Set C previous point to A
        //     tempNext.setPreviousNode(tempPrev);
        // }

        // // If no element was found after the entire search
        // if (targetNode == null) {
        //     throw new NoSuchElementException();
        // }

        size--;
        versionNumber++;
        return targetNode.getElement();
    }

    @Override
    public T remove(int index) {
        // Index is always one less than size, so we must also check it if equals
        // [A, B, C] has size of 3 but max index of 2 (0, 1, 2).
        if (index >= size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        // Always start at the beginning
        Node<T> currentNode = head;

        // Unlike with a single list, we can go directly to the node.
        // [A, B, C] and we want index 2, which is C, we keep going
        for (int i = 0; i < index - 1; i++) {
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