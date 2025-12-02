package structures.graphs;

import structures.lists.ArrayUnorderedList;
import structures.queues.LinkedQueue;
import structures.stacks.LinkedStack;

import java.util.Iterator;

public class GraphList<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected T[] vertices;

    protected ArrayUnorderedList<Integer>[] adjList;

    public GraphList() {
        numVertices = 0;
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
        this.adjList = new ArrayUnorderedList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            this.adjList[i] = new ArrayUnorderedList<Integer>();
        }
    }

    // --- Métodos de Gestão ---

    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        adjList[numVertices] = new ArrayUnorderedList<Integer>();
        numVertices++;
    }

    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            // CORREÇÃO: Autoboxing
            adjList[index1].addToRear(index2);
            adjList[index2].addToRear(index1);
        }
    }

    public void removeVertex(T vertex) {
        // Stub
    }

    public void removeEdge(T vertex1, T vertex2) {
        // Stub
    }

    // --- Travessia BFS (Adaptado para Lista) ---

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

        // CORREÇÃO: Autoboxing
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);

            Iterator<Integer> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                Integer neighborIndex = it.next();
                if (!visited[neighborIndex]) {
                    traversalQueue.enqueue(neighborIndex);
                    visited[neighborIndex] = true;
                }
            }
        }
        return resultList.iterator();
    }

    // --- Travessia DFS (Adaptado para Lista) ---

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

        // CORREÇÃO: Autoboxing
        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            Iterator<Integer> it = adjList[x.intValue()].iterator();
            while (it.hasNext() && !found) {
                Integer neighborIndex = it.next();
                if (!visited[neighborIndex]) {
                    traversalStack.push(neighborIndex);
                    resultList.addToRear(vertices[neighborIndex]);
                    visited[neighborIndex] = true;
                    found = true;
                }
            }

            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }

    // --- Métodos Auxiliares ---

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
        ArrayUnorderedList<Integer>[] largerAdjList = new ArrayUnorderedList[vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            largerVertices[i] = vertices[i];
            largerAdjList[i] = adjList[i];
        }
        for (int i = numVertices; i < largerVertices.length; i++) {
            largerAdjList[i] = new ArrayUnorderedList<Integer>();
        }

        vertices = largerVertices;
        adjList = largerAdjList;
    }

    // --- Stubs ---
    public boolean isEmpty() { return numVertices == 0; }
    public boolean isConnected() { return false; }
    public int size() { return numVertices; }

    public String toString() {
        String result = "";
        for (int i = 0; i < numVertices; i++) {
            result += vertices[i].toString() + " -> " + adjList[i].toString() + "\n";
        }
        return result;
    }

    // --- Caminho Mais Curto ---

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
        // CORREÇÃO: Autoboxing
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty() && !found) {
            Integer x = traversalQueue.dequeue();

            if (x.intValue() == targetIndex) {
                found = true;
                break;
            }

            Iterator<Integer> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                Integer neighborIndex = it.next();

                if (!visited[neighborIndex]) {
                    visited[neighborIndex] = true;
                    predecessor[neighborIndex] = x.intValue();
                    // CORREÇÃO: Autoboxing
                    traversalQueue.enqueue(neighborIndex);

                    if (neighborIndex.intValue() == targetIndex) {
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
            // CORREÇÃO: Autoboxing
            pathStack.push(current);
            current = predecessor[current];
        }
        // CORREÇÃO: Autoboxing
        pathStack.push(startIndex);

        while (!pathStack.isEmpty()) {
            resultList.addToRear(vertices[pathStack.pop().intValue()]);
        }

        return resultList.iterator();
    }
}