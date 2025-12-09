package structures.graph;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

import java.util.Iterator;

/**
 * Graph represents an adjacency matrix implementation of a graph.
 *
 */
public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected boolean[][] adjMatrix;
    protected T[] vertices;

    /**
     * Creates an empty graph.
     */

    public Graph() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**Inserts an edge between two vertices of the graph.
    *
     * @param index1 the first index
    * @param index2 the second index
    */
    private void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }
    }

    /** Adds a vertex to the graph, expanding the capacity of the graph
    * if necessary. It also associates an object with the vertex.
    *
     * @param vertex the vertex to add to the graph
    */
    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;

        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;
    }

    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

    public Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();

        if (!indexIsValid(startIndex))
            return resultList.iterator();

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        return iteratorDFS(getIndex(startVertex));
    }

    public Iterator<T> iteratorDFS(int startIndex) {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(startIndex))
            return resultList.iterator();

        for (int i = 0; i < numVertices; i++)
            visited[i] = false;

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }


    protected boolean indexIsValid(int index) {
        return ((index < numVertices) && (index >= 0));
    }


    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++)
            if (vertices[i].equals(vertex))
                return i;
        return -1;
    }

    protected void expandCapacity() {
        T[] largerVertices = (T[]) (new Object[vertices.length * 2]);
        boolean[][] largerAdjMatrix = new boolean[vertices.length * 2][vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            largerVertices[i] = vertices[i];
            for (int j = 0; j < numVertices; j++) {
                largerAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }
        vertices = largerVertices;
        adjMatrix = largerAdjMatrix;
    }

    @Override
    public void removeVertex(T vertex) {
        int index = getIndex(vertex);

        if (indexIsValid(index)) {
            numVertices--;

            for (int i = index; i < numVertices; i++) {
                vertices[i] = vertices[i+1];
            }
            vertices[numVertices] = null;

            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j <= numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i+1][j];
                }
            }

            for (int i = 0; i < numVertices; i++) {
                for (int j = index; j < numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i][j+1];
                }
            }
        }
    }
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }
    private void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = false;
            adjMatrix[index2][index1] = false;
        }
    }
    @Override
    public boolean isEmpty() { return numVertices == 0; }

    @Override
    public boolean isConnected() {
        if (isEmpty()) return false;
        Iterator<T> it = iteratorBFS(0);
        int count = 0;

        while (it.hasNext()) {
            it.next();
            count++;
        }

        return count == numVertices;
    }

    @Override
    public int size() { return numVertices; }

    public String toString() {
        String result = "";
        for (int i = 0; i < numVertices; i++) {
            result += vertices[i].toString() + "\n";
        }
        return result;
    }

    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return iteratorShortestPath(getIndex(startVertex), getIndex(targetVertex));
    }

    protected Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return resultList.iterator();

        if (startIndex == targetIndex) {
            resultList.addToRear(vertices[startIndex]);
            return resultList.iterator();
        }

        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        boolean[] visited = new boolean[numVertices];
        int[] predecessor = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
            predecessor[i] = -1;
        }

        boolean found = false;
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty() && !found) {
            Integer x = traversalQueue.dequeue();

            if (x.intValue() == targetIndex) {
                found = true;
                break;
            }

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    visited[i] = true;
                    predecessor[i] = x.intValue();
                    traversalQueue.enqueue(i);

                    if (i == targetIndex) {
                        found = true;
                        break;
                    }
                }
            }
        }

        if (!found) return resultList.iterator();

        LinkedStack<Integer> pathStack = new LinkedStack<Integer>();
        int current = targetIndex;

        while (current != startIndex && current != -1) {
            pathStack.push(current);
            current = predecessor[current];
        }
        pathStack.push(startIndex);

        while (!pathStack.isEmpty()) {
            resultList.addToRear(vertices[pathStack.pop().intValue()]);
        }

        return resultList.iterator();
    }
}