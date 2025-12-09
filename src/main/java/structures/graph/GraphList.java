package structures.graph;

import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

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

    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();

        vertices[numVertices] = vertex;
        adjList[numVertices] = new ArrayUnorderedList<Integer>();
        numVertices++;
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjList[index1].addToRear(index2);
            adjList[index2].addToRear(index1);
        }
    }

    @Override
    public void removeVertex(T vertex) {
        int indexToRemove = getIndex(vertex);

        if (indexIsValid(indexToRemove)) {
            numVertices--;

            for (int i = indexToRemove; i < numVertices; i++) {
                vertices[i] = vertices[i + 1];
                adjList[i] = adjList[i + 1];
            }

            vertices[numVertices] = null;
            adjList[numVertices] = null;

            for (int i = 0; i < numVertices; i++) {
                ArrayUnorderedList<Integer> currentList = adjList[i];
                ArrayUnorderedList<Integer> newList = new ArrayUnorderedList<>();

                Iterator<Integer> it = currentList.iterator();

                while (it.hasNext()) {
                    Integer neighbor = it.next();

                    if (neighbor == indexToRemove) {
                        continue;
                    }
                    else if (neighbor > indexToRemove) {
                        newList.addToRear(neighbor - 1);
                    }
                    else {
                        newList.addToRear(neighbor);
                    }
                }
                adjList[i] = newList;
            }
        }
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }


    private void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjList[index1].remove(Integer.valueOf(index2));
            adjList[index2].remove(Integer.valueOf(index1));
        }
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

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < numVertices; i++) {
            result += vertices[i].toString() + " -> " + adjList[i].toString() + "\n";
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

            Iterator<Integer> it = adjList[x.intValue()].iterator();
            while (it.hasNext()) {
                Integer neighborIndex = it.next();

                if (!visited[neighborIndex]) {
                    visited[neighborIndex] = true;
                    predecessor[neighborIndex] = x.intValue();
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