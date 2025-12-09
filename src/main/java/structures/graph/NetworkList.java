package structures.graph;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

import java.util.Iterator;

public class NetworkList<T> implements NetworkADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected T[] vertices;

    protected ArrayUnorderedList<WeightedEdge>[] adjList;

    protected class WeightedEdge {
        public int neighborIndex;
        public double weight;

        public WeightedEdge(int neighborIndex, double weight) {
            this.neighborIndex = neighborIndex;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + neighborIndex + ", " + weight + ")";
        }
    }

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

                    if (edge.neighborIndex == indexToRemove) {
                        continue;
                    } else if (edge.neighborIndex > indexToRemove) {
                        newList.addToRear(new WeightedEdge(edge.neighborIndex - 1, edge.weight));
                    } else {
                        newList.addToRear(edge);
                    }
                }
                adjList[i] = newList;
            }
        }
    }

    /**
     * Remove uma aresta entre dois vértices.
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
            if (edge.neighborIndex != targetIndex) {
                newList.addToRear(edge);
            }
        }
        adjList[sourceIndex] = newList;
    }

    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

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
                if (!visited[edge.neighborIndex]) {
                    traversalQueue.enqueue(edge.neighborIndex);
                    visited[edge.neighborIndex] = true;
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
                if (!visited[edge.neighborIndex]) {
                    traversalStack.push(edge.neighborIndex);
                    resultList.addToRear(vertices[edge.neighborIndex]);
                    visited[edge.neighborIndex] = true;
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

            Iterator<WeightedEdge> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();

                if (!visited[edge.neighborIndex]) {
                    visited[edge.neighborIndex] = true;
                    predecessor[edge.neighborIndex] = x.intValue();
                    traversalQueue.enqueue(edge.neighborIndex);

                    if (edge.neighborIndex == targetIndex) {
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
                int v = edge.neighborIndex;
                double weight = edge.weight;

                if (!visited[v]) {
                    if (pathWeight[u] + weight < pathWeight[v]) {
                        pathWeight[v] = pathWeight[u] + weight;
                    }
                }
            }
        }

        return pathWeight[targetIndex];
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
     * Retorna um iterador com os vértices adjacentes a um dado vértice.
     * Este método é genérico e serve para qualquer aplicação do grafo.
     *
     * @param vertex O vértice do qual queremos os vizinhos.
     * @return Iterador com os vizinhos.
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