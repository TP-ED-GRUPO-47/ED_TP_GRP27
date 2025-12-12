package structures.linear;

/**
 * Contract for ordered list implementations that maintain elements in sorted order.
 *
 * @param <T> element type (must support comparison)
 */
public interface OrderedListADT<T> extends ListADT<T> {

    /**
     * Inserts an element in its ordered position.
     *
     * @param element value to insert respecting ordering
     */
    void add(T element);

}
