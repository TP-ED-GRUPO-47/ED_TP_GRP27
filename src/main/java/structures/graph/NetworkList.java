package structures.graph;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

import java.util.Iterator;

/**
 * List-based implementation of a weighted directed graph (Network).
 * <p>
 * This class implements the {@link NetworkADT} interface using adjacency lists.
 * Each vertex stores a list of weighted edges to its neighbors, allowing efficient
 * traversal and weight queries. Supports graph operations like shortest path calculation.
 * </p>
 *
 * @param <T> The type of elements stored as vertices.
 * @author Group 27
 * @version 2025/2026
 */
public class NetworkList<T> implements NetworkADT<T> {
    /** Initial capacity for vertices. */
    protected final int DEFAULT_CAPACITY = 10;
    /** Current number of vertices in the network. */
    protected int numVertices;
    /** Array holding vertex elements. */
    protected T[] vertices;

    /** Adjacency lists storing weighted edges for each vertex index. */
    protected ArrayUnorderedList<WeightedEdge>[] adjList;

    /**
     * Lightweight edge container storing neighbor index and weight.
     */
    protected class WeightedEdge {
        private final int neighborIndex;
        private final double weight;

        /**
         * Creates an edge to a neighbor with a given weight.
         *
         * @param neighborIndex index of the adjacent vertex
         * @param weight        cost of the connection
         */
        public WeightedEdge(int neighborIndex, double weight) {
        	this.neighborIndex = neighborIndex;
        	this.weight = weight;
        }

        /**
         * Returns the adjacency-list index of the neighbor.
         *
         * @return the adjacency-list index of the neighbor
         */
        public int getNeighborIndex() {
            return neighborIndex;
        }

        /**
         * Returns the weight associated with this edge.
         *
         * @return the weight associated with this edge
         */
        public double getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "(" + neighborIndex + ", " + weight + ")";
        }
    }

    /**
     * Creates an empty network with default capacity.
     */
    public NetworkList() {
        numVertices = 0;
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
        this.adjList = new ArrayUnorderedList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            this.adjList[i] = new ArrayUnorderedList<WeightedEdge>();
        }
    }


    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        adjList[numVertices] = new ArrayUnorderedList<WeightedEdge>();
        numVertices++;
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    private void addEdge(int index1, int index2, double weight) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            WeightedEdge edge1 = new WeightedEdge(index2, weight);
            adjList[index1].addToRear(edge1);

            WeightedEdge edge2 = new WeightedEdge(index1, weight);
            adjList[index2].addToRear(edge2);
        }
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(vertex1, vertex2, 1.0);
    }

    /**
     * Remove um vértice e atualiza todos os índices nas arestas restantes.
     */
    @Override
    public void removeVertex(T vertex) {
        int indexToRemove = getIndex(vertex);

        if (indexIsValid(indexToRemove)) {
            numVertices--;

            for (int i = indexToRemove; i < numVertices; i++) {
                vertices[i] = vertices[i+1];
                adjList[i] = adjList[i+1];
            }
            vertices[numVertices] = null;
            adjList[numVertices] = null;

            for (int i = 0; i < numVertices; i++) {
                ArrayUnorderedList<WeightedEdge> currentList = adjList[i];
                ArrayUnorderedList<WeightedEdge> newList = new ArrayUnorderedList<>();

                Iterator<WeightedEdge> it = currentList.iterator();
                while (it.hasNext()) {
                    WeightedEdge edge = it.next();

                    if (edge.getNeighborIndex() == indexToRemove) {
                        continue;
                    } else if (edge.getNeighborIndex() > indexToRemove) {
                        newList.addToRear(new WeightedEdge(edge.getNeighborIndex() - 1, edge.getWeight()));
                    } else {
                        newList.addToRear(edge);
                    }
                }
                adjList[i] = newList;
            }
        }
    }

    /**
     * Removes an edge between two vertices.
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            removeEdgeFromList(index1, index2);
            removeEdgeFromList(index2, index1);
        }
    }

    private void removeEdgeFromList(int sourceIndex, int targetIndex) {
        ArrayUnorderedList<WeightedEdge> currentList = adjList[sourceIndex];
        ArrayUnorderedList<WeightedEdge> newList = new ArrayUnorderedList<>();

        Iterator<WeightedEdge> it = currentList.iterator();
        while (it.hasNext()) {
            WeightedEdge edge = it.next();
            if (edge.getNeighborIndex() != targetIndex) {
                newList.addToRear(edge);
            }
        }
        adjList[sourceIndex] = newList;
    }

    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

    /**
     * Returns a breadth-first traversal starting from an index.
     *
     * @param startIndex index of the starting vertex
     * @return iterator over visited vertices in BFS order
     */
    public Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();

        if (!indexIsValid(startIndex)) return resultList.iterator();

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) visited[i] = false;

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);

            Iterator<WeightedEdge> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();
                if (!visited[edge.getNeighborIndex()]) {
                    traversalQueue.enqueue(edge.getNeighborIndex());
                    visited[edge.getNeighborIndex()] = true;
                }
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        return iteratorDFS(getIndex(startVertex));
    }

    /**
     * Returns a depth-first traversal starting from an index.
     *
     * @param startIndex index of the starting vertex
     * @return iterator over visited vertices in DFS order
     */
    public Iterator<T> iteratorDFS(int startIndex) {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(startIndex)) return resultList.iterator();

        for (int i = 0; i < numVertices; i++) visited[i] = false;

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            Iterator<WeightedEdge> it = adjList[x.intValue()].iterator();
            while (it.hasNext() && !found) {
                WeightedEdge edge = it.next();
                if (!visited[edge.getNeighborIndex()]) {
                    traversalStack.push(edge.getNeighborIndex());
                    resultList.addToRear(vertices[edge.getNeighborIndex()]);
                    visited[edge.getNeighborIndex()] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }


    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return iteratorShortestPath(getIndex(startVertex), getIndex(targetVertex));
    }

    /**
     * Computes shortest path between two indices using Dijkstra's algorithm.
     *
     * @param startIndex  source vertex index
     * @param targetIndex destination vertex index
     * @return iterator containing the path vertices, or empty if unreachable
     */
    protected Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return resultList.iterator();

        double[] pathWeight = new double[numVertices];
        boolean[] visited = new boolean[numVertices];
        int[] predecessor = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
            predecessor[i] = -1;
        }

        pathWeight[startIndex] = 0;

        for (int i = 0; i < numVertices; i++) {
            int u = -1;
            double minWeight = Double.POSITIVE_INFINITY;

            for (int j = 0; j < numVertices; j++) {
                if (!visited[j] && pathWeight[j] < minWeight) {
                    minWeight = pathWeight[j];
                    u = j;
                }
            }

            if (u == -1) break;
            visited[u] = true;

            if (u == targetIndex) break;

            Iterator<WeightedEdge> it = adjList[u].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();
                int v = edge.getNeighborIndex();
                double weight = edge.getWeight();

                if (!visited[v]) {
                    if (pathWeight[u] + weight < pathWeight[v]) {
                        pathWeight[v] = pathWeight[u] + weight;
                        predecessor[v] = u;
                    }
                }
            }
        }

        if (pathWeight[targetIndex] == Double.POSITIVE_INFINITY)
            return resultList.iterator();

        LinkedStack<Integer> pathStack = new LinkedStack<>();
        int current = targetIndex;
        while (current != -1) {
            pathStack.push(current);
            current = predecessor[current];
        }

        while (!pathStack.isEmpty()) {
            resultList.addToRear(vertices[pathStack.pop()]);
        }

        return resultList.iterator();
    }

    /**
     * Returns a Minimum Spanning Tree (MST) using Prim's algorithm.
     */
    /**
     * Returns a Minimum Spanning Tree (MST) using Prim's algorithm.
     *
     * @return new network containing the MST edges
     */
    public NetworkList<T> mstNetwork() {
        NetworkList<T> mst = new NetworkList<>();
        if (isEmpty()) return mst;

        for (int i = 0; i < numVertices; i++) {
            mst.addVertex(this.vertices[i]);
        }

        boolean[] visited = new boolean[numVertices];
        int[] minEdgeSource = new int[numVertices];
        double[] minEdgeWeight = new double[numVertices];

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
            minEdgeWeight[i] = Double.POSITIVE_INFINITY;
            minEdgeSource[i] = -1;
        }

        minEdgeWeight[0] = 0;

        for (int i = 0; i < numVertices; i++) {
            int u = -1;
            double min = Double.POSITIVE_INFINITY;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && minEdgeWeight[v] < min) {
                    min = minEdgeWeight[v];
                    u = v;
                }
            }

            if (u == -1) break;
            visited[u] = true;

            if (minEdgeSource[u] != -1) {
                mst.addEdge(vertices[minEdgeSource[u]], vertices[u], minEdgeWeight[u]);
            }

            Iterator<WeightedEdge> it = adjList[u].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();
                int v = edge.getNeighborIndex();
                double weight = edge.getWeight();

                if (!visited[v] && weight < minEdgeWeight[v]) {
                    minEdgeWeight[v] = weight;
                    minEdgeSource[v] = u;
                }
            }
        }

        return mst;
    }


    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        int startIndex = getIndex(vertex1);
        int targetIndex = getIndex(vertex2);

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return Double.POSITIVE_INFINITY;

        if (startIndex == targetIndex) return 0.0;

        double[] pathWeight = new double[numVertices];
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }
        pathWeight[startIndex] = 0;

        for (int i = 0; i < numVertices; i++) {
            int u = -1;
            double minWeight = Double.POSITIVE_INFINITY;

            for (int j = 0; j < numVertices; j++) {
                if (!visited[j] && pathWeight[j] < minWeight) {
                    minWeight = pathWeight[j];
                    u = j;
                }
            }

            if (u == -1) break;
            visited[u] = true;

            if (u == targetIndex) return pathWeight[u];

            Iterator<WeightedEdge> it = adjList[u].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();
                int v = edge.getNeighborIndex();
                double weight = edge.getWeight();

                if (!visited[v]) {
                    if (pathWeight[u] + weight < pathWeight[v]) {
                        pathWeight[v] = pathWeight[u] + weight;
                    }
                }
            }
        }

        return pathWeight[targetIndex];
    }


    /**
     * Checks if a vertex index is within bounds.
     *
     * @param index index to validate
     * @return true when the index refers to an existing vertex
     */
    protected boolean indexIsValid(int index) {
        return ((index < numVertices) && (index >= 0));
    }

    /**
     * Returns the internal index for a vertex element.
     *
     * @param vertex vertex element to locate
     * @return index of the vertex or -1 if not found
     */
    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++)
            if (vertices[i].equals(vertex))
                return i;
        return -1;
    }

    /**
     * Doubles storage for vertices and adjacency lists.
     */
    protected void expandCapacity() {
        T[] largerVertices = (T[]) (new Object[vertices.length * 2]);
        ArrayUnorderedList<WeightedEdge>[] largerAdjList = new ArrayUnorderedList[vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            largerVertices[i] = vertices[i];
            largerAdjList[i] = adjList[i];
        }
        for (int i = numVertices; i < largerVertices.length; i++) {
            largerAdjList[i] = new ArrayUnorderedList<WeightedEdge>();
        }

        vertices = largerVertices;
        adjList = largerAdjList;
    }

    @Override
    public boolean isEmpty() { return numVertices == 0; }

    @Override
    public boolean isConnected() {
        if (isEmpty()) return false;
        Iterator<T> it = iteratorBFS(0);
        int count = 0;
        while(it.hasNext()) {
            it.next();
            count++;
        }
        return count == numVertices;
    }

    @Override
    public int size() { return numVertices; }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < numVertices; i++) {
            result += vertices[i].toString() + " -> " + adjList[i].toString() + "\n";
        }
        return result;
    }

    /**
     * Returns an iterator with the vertices adjacent to a given vertex.
     * This method is generic and serves any application of the graph.
     *
     * @param vertex The vertex whose neighbors we want.
     * @return Iterator with the neighbors.
     */
    public Iterator<T> getNeighbors(T vertex) {
        int index = getIndex(vertex);
        ArrayUnorderedList<T> neighbors = new ArrayUnorderedList<>();

        if (!indexIsValid(index)) {
            return neighbors.iterator();
        }

        Iterator<WeightedEdge> it = adjList[index].iterator();
        while (it.hasNext()) {
            WeightedEdge edge = it.next();
            neighbors.addToRear(vertices[edge.neighborIndex]);
        }

        return neighbors.iterator();
    }
}