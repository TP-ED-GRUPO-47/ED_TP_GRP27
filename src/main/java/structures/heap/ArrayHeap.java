package structures.heap;
import exceptions.EmptyCollectionException;
import structures.tree.ArrayBinaryTree;

/**
 * Array-backed min-heap implementation.
 * <p>
 * Extends {@link structures.tree.ArrayBinaryTree} to store elements in a binary
 * heap shape and maintains the heap-order property for efficient retrieval of
 * the smallest element.
 * </p>
 *
 * @param <T> element type (must be {@link Comparable} for ordering)
 */
public class ArrayHeap<T> extends ArrayBinaryTree<T>
    implements HeapADT<T>
{
    /**
     * Creates an empty array-based heap with default capacity.
     */
    public ArrayHeap()
    {
        super();
    }

    /**
     * Expands the capacity of the heap by doubling the size of the array.
     */
    private void expandCapacity() {
        T[] newTree = (T[]) new Object[tree.length * 2];
        System.arraycopy(tree, 0, newTree, 0, tree.length);
        tree = newTree;
    }

    /**
     * Inserts a new element while preserving heap order.
     * The element is added at the end and then moved up to maintain the min-heap property.
     *
     * @param obj the element to be added to this heap
     */
    @Override
    public void addElement (T obj)
    {
        if (size==tree.length)
            expandCapacity();
        tree[size] =obj;
        size++;
        if (size>1)
            heapifyAdd();
    }

    /**
     * Reorders this heap to maintain the min-heap property after adding an element.
     * The element is moved up the tree until it reaches its correct position.
     */
    private void heapifyAdd() {
        T temp;
        int next = size - 1;

        temp = tree[next];

        while ((next != 0) && (((Comparable)temp).compareTo(tree[(next-1)/2]) < 0)) {
            tree[next] = tree[(next-1)/2];
            next = (next-1)/2;
        }
        tree[next] = temp;
    }

    /**
     * Removes and returns the minimum element from this heap.
     * The last element replaces the root and is moved down to maintain the min-heap property.
     *
     * @return the minimum element from this heap
     * @throws EmptyCollectionException if the heap is empty
     */
    @Override
    public T removeMin() throws EmptyCollectionException
    {
        if (isEmpty())
            throw new EmptyCollectionException ("Empty Heap");
        T minElement = tree[0];
        tree[0] = tree[size-1];
        heapifyRemove();
        size--;

        return minElement;
    }

    /**
     * Reorders this heap to maintain the min-heap property after removing the root.
     * The replacement element is moved down the tree until it reaches its correct position.
     */
    private void heapifyRemove() {
        T temp;
        int node = 0;
        int left = 1;
        int right = 2;
        int next;

        if ((tree[left] == null) && (tree[right] == null))
            next = size;
        else if (tree[left] == null)
            next = right;
        else if (tree[right] == null)
            next = left;
        else if (((Comparable)tree[left]).compareTo(tree[right]) < 0)
            next = left;
        else
            next = right;
        temp = tree[node];

        while ((next < size) && (((Comparable)tree[next]).compareTo
                (temp) < 0))
        {
            tree[node] = tree[next];
            node = next;
            left = 2*node+1;
            right = 2*(node+1);
            if ((tree[left] == null) && (tree[right] == null))
                next = size;
            else if (tree[left] == null)
                next = right;
            else if (tree[right] == null)
                next = left;
            else if (((Comparable)tree[left]).compareTo(tree[right]) < 0)
                next = left;
            else
                next = right;
        }
        tree[node] = temp;
    }

    /**
     * Returns (without removing) the minimum element from this heap.
     *
     * @return the minimum element in this heap
     * @throws RuntimeException if the heap is empty
     */
    @Override
    public T findMin() {
        if (isEmpty())
            throw new RuntimeException("Empty Heap");
        return tree[0];
    }

    /**
     * Indicates whether the heap contains no elements.
     *
     * @return true if this heap is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}


