package structures.graphs;

import structures.lists.ArrayUnorderedList;
import structures.queues.LinkedQueue;
import structures.stacks.LinkedStack;

import java.util.Iterator;

public class Network<T> extends Graph<T> implements NetworkADT<T> {
    protected double[][] adjMatrix; // Matriz de pesos

    public Network() {
        super();
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];

        // Inicializar com infinito
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < DEFAULT_CAPACITY; j++) {
                this.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    // --- Métodos de Gestão ---

    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;

        // Inicializar a nova linha e coluna com Infinito na matriz de PESOS
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }

        numVertices++;
    }

    // Adicionar Aresta com Peso
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    private void addEdge(int index1, int index2, double weight) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = weight;
            adjMatrix[index2][index1] = weight;
        }
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(vertex1, vertex2, 1.0); // Peso default 1.0
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        addEdge(vertex1, vertex2, Double.POSITIVE_INFINITY);
    }

    // --- Travessias (Adaptação para pesos) ---

    @Override
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

            for (int i = 0; i < numVertices; i++) {
                // Verifica se peso < infinito
                if (adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                    // CORREÇÃO: Autoboxing
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    @Override
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

            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                    // CORREÇÃO: Autoboxing
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

    // --- Caminho Mais Curto ---

    @Override
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

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                    visited[i] = true;
                    predecessor[i] = x.intValue();
                    // CORREÇÃO: Autoboxing
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

    @Override
    protected void expandCapacity() {
        T[] largerVertices = (T[]) (new Object[vertices.length * 2]);
        double[][] largerAdjMatrix = new double[vertices.length * 2][vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            largerVertices[i] = vertices[i];
            for (int j = 0; j < numVertices; j++) {
                largerAdjMatrix[i][j] = adjMatrix[i][j];
            }
            for (int j = numVertices; j < largerAdjMatrix.length; j++) {
                largerAdjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = numVertices; i < largerAdjMatrix.length; i++) {
            for (int j = 0; j < largerAdjMatrix.length; j++) {
                largerAdjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        vertices = largerVertices;
        adjMatrix = largerAdjMatrix;
    }

    public double shortestPathWeight(T vertex1, T vertex2) {
        return 0; // Por implementar (Dijkstra)
    }
}