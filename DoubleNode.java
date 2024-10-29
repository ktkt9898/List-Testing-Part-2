/**
 * A double node class for creating a linked data strucutre.
 * @author Kyle Truschel and CS221-3 F24
 */

 public class DoubleNode<T> {
    private T element;
    private Node<T> nextNode;
    private Node<T> previousNode;

    /**
     * Initialize a new node with a desired element.
     * @param element a desired element to be stored.
     */
    public DoubleNode(T element) {
        // Nodes are always a placeholder for an element.
        this.element = element;
        this.nextNode = null;
        this.previousNode = null;
    }

    /**
     * Initialize a new node with a given element and known next node.
     * @param element a desired element to be stored.
     * @param nexNode a known node in the chain/linked data structure.
     */
    public DoubleNode(T element, Node<T> nexNode) {
        this.element = element;
        this.nextNode = nextNode;
        this.previousNode = null;
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
}

