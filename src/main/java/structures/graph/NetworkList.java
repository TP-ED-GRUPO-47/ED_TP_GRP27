package structures.graph;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

import java.util.Iterator;

public class NetworkList<T> implements NetworkADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected T[] vertices;

    // Array de Listas de "WeightedEdge"
    protected ArrayUnorderedList<WeightedEdge>[] adjList;

    // Classe auxiliar para guardar o vizinho e o peso
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

    // --- Métodos de Gestão ---

    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        adjList[numVertices] = new ArrayUnorderedList<WeightedEdge>();
        numVertices++;
    }

    // Adicionar Aresta COM peso
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    private void addEdge(int index1, int index2, double weight) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            // Adiciona (vizinho 2, peso) à lista do 1
            WeightedEdge edge1 = new WeightedEdge(index2, weight);
            adjList[index1].addToRear(edge1);

            // Adiciona (vizinho 1, peso) à lista do 2 (Não direcionado)
            WeightedEdge edge2 = new WeightedEdge(index1, weight);
            adjList[index2].addToRear(edge2);
        }
    }

    // Adicionar Aresta SEM peso (assume peso 1.0)
    public void addEdge(T vertex1, T vertex2) {
        addEdge(vertex1, vertex2, 1.0);
    }

    public void removeVertex(T vertex) {
        // Stub
    }

    public void removeEdge(T vertex1, T vertex2) {
        // Stub (remover de lista ligada é complexo sem iterador específico)
    }

    // --- Travessias ---

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

        // CORREÇÃO: Autoboxing
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);

            Iterator<WeightedEdge> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();
                if (!visited[edge.neighborIndex]) {
                    // CORREÇÃO: Autoboxing
                    traversalQueue.enqueue(edge.neighborIndex);
                    visited[edge.neighborIndex] = true;
                }
            }
        }
        return resultList.iterator();
    }

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

        // CORREÇÃO: Autoboxing
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
                    // CORREÇÃO: Autoboxing
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

            Iterator<WeightedEdge> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                WeightedEdge edge = it.next();

                if (!visited[edge.neighborIndex]) {
                    visited[edge.neighborIndex] = true;
                    predecessor[edge.neighborIndex] = x.intValue();
                    // CORREÇÃO: Autoboxing
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

    // --- Stubs para interface ---
    public double shortestPathWeight(T vertex1, T vertex2) { return 0; }
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
}