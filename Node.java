/**
 * A node class for creating a linked data structre.
 * Also functions for double linked lists.
 * 
 * @author Kyle Truschel and CS221-3 F24
 */

public class Node<T> {
    private T element;
    private Node<T> nextNode;
    private Node<T> previousNode;

    /**
     * Initialize a new node with a desired element.
     * @param element a desired element to be stored.
     */
    public Node(T element) {
        // Nodes are always a placeholder for an element.
        this.element = element;
        nextNode = null;
        previousNode = null;
    }

    /**
     * Initialize a new node with a given element and known next node.
     * @param element a desired element to be stored.
     * @param nextNode a known node in the chain/linked data structure.
     */
    public Node(T element, Node<T> nextNode) {
        this.element = element;
        this.nextNode = nextNode;
    }

    /**
     * Display the desired element.
     * @return an element value.
     */
    public T getElement() {
        return element;
    }

    /**
     * Overwrite an existing element.
     * @param element a desired element to be stored.
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * Display the next node in the chain.
     * @return the next node value.
     */
    public Node<T> getNextNode() {
        return nextNode;
    }

    /**
     * Overwrite an existing known node.
     * @param nextNode a desired node to be overwritten.
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * Retrieve the previous node
     * @return the previous node
     */
    public Node<T> getPreviousNode() {
        return previousNode;
    }

    /**
     * Set the previous node
     * @param previousNode the previous existing Node
     */
    public void setPreviousNode(Node<T> previousNode) {
        this.previousNode = previousNode;
    }
}
