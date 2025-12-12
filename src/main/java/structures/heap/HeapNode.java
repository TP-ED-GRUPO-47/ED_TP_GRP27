package structures.heap;
import structures.tree.BinaryTreeNode;

/**
 * Heap node wrapper storing element and priority.
 *
 * @param <T> element type stored
 */
public class HeapNode<T> extends BinaryTreeNode<T> {
    /**
     * The parent of this node.
     */
    protected HeapNode<T> parent;

    /**
     * Creates a new heap node with the specified element.
     *
     * @param element the element that will become a part of the new heap node
     */
    public HeapNode(T element) {
        super(element);
        parent = null;
    }

    /**
     * Creates a new heap node with the specified element and children.
     *
     * @param element the element that will become a part of the new heap node
     * @param left    the left child of the new heap node
     * @param right   the right child of the new heap node
     */
    public HeapNode(T element, HeapNode<T> left, HeapNode<T> right) {
        super(element, left, right);
        parent = null;
    }

    /**
     * Creates a new heap node with the specified element and parent.
     *
     * @param element the element that will become a part of the new heap node
     * @param parent  the parent of the new heap node
     */
    public HeapNode(T element, HeapNode<T> parent) {
        super(element);
        this.parent = parent;
    }

    /**
     * Creates a new heap node with the specified element, children, and parent.
     *
     * @param element the element that will become a part of the new heap node
     * @param left    the left child of the new heap node
     * @param right   the right child of the new heap node
     * @param parent  the parent of the new heap node
     */
    public HeapNode(T element, HeapNode<T> left, HeapNode<T> right, HeapNode<T> parent) {
        super(element, left, right);
        this.parent = parent;
    }

    /**
     * Returns the parent of this node.
     *
     * @return the parent of this node
     */
    public HeapNode<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent of this node.
     *
     * @param parent the parent of this node
     */
    public void setParent(HeapNode<T> parent) {
        this.parent = parent;
    }
}

