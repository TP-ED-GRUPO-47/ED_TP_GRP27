package structures.graph;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

import java.util.Iterator;

public class Network<T> extends Graph<T> implements NetworkADT<T> {
    protected double[][] adjMatrix;

    public Network() {
        super();
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < DEFAULT_CAPACITY; j++) {
                this.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;

        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }

        numVertices++;
    }

    /**
     * Remove um vértice e ajusta a matriz de pesos (doubles).
     * É necessário fazer Override porque a matriz do pai (Graph) é boolean e esta é double.
     */
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
        addEdge(vertex1, vertex2, 1.0);
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = Double.POSITIVE_INFINITY;
            adjMatrix[index2][index1] = Double.POSITIVE_INFINITY;
        }
    }



    @Override
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

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY && !visited[i]) {
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

        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY && !visited[i]) {
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


    /**
     * Retorna o peso do caminho mais curto entre dois vértices usando o Algoritmo de Dijkstra.
     */
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

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjMatrix[u][v] < Double.POSITIVE_INFINITY) {
                    if (pathWeight[u] + adjMatrix[u][v] < pathWeight[v]) {
                        pathWeight[v] = pathWeight[u] + adjMatrix[u][v];
                    }
                }
            }
        }

        return pathWeight[targetIndex];
    }

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
}