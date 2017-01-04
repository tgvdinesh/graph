package com.ds.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<V> {

    private boolean isDirected = false;
    private TreeMap<V, ArrayList<Edge<V>>> adjacencyList;

    public Graph(boolean isDirected) {
        this.isDirected = isDirected;
        this.adjacencyList = new TreeMap<>();
    }

    public TreeMap<V, ArrayList<Edge<V>>> getAdjacencyList() {
        return adjacencyList;
    }

    public ArrayList<Edge<V>> getAdjacentVertices(V vertex) {
        return adjacencyList.get(vertex);
    }

    boolean addEdge(V sourceVertex, V destinationVertex) {
        persistVertex(sourceVertex, destinationVertex, 1);
        if (!isDirected) {
            persistVertex(destinationVertex, sourceVertex, 1);
        }
        return true;
    }

    boolean addEdge(V sourceVertex, V destinationVertex, int weight) {
        persistVertex(sourceVertex, destinationVertex, weight);
        if (!isDirected) {
            persistVertex(destinationVertex, sourceVertex, weight);
        }
        return true;
    }

    private void persistVertex(V sourceVertex, V destinationVertex, int weight) {
        ArrayList<Edge<V>> adjacentVertex;
        if (!doesVertexExist(sourceVertex)) {
            adjacentVertex = new ArrayList<>();
            adjacentVertex.add(new Edge<>(destinationVertex, weight));
            adjacencyList.put(sourceVertex, adjacentVertex);
        } else {
            adjacentVertex = adjacencyList.get(sourceVertex);
            adjacentVertex.add(new Edge<>(destinationVertex, weight));
        }

        adjacencyList.get(sourceVertex).sort((edge1, edge2) -> {
            if (edge1.getVertex() instanceof Integer) {
                return ((Integer) edge1.getVertex()) - ((Integer) edge2.getVertex());
            } else if (edge1.getVertex() instanceof String) {
                return ((String) edge1.getVertex()).compareTo((String) edge2.getVertex());
            }
            return 0;
        });
    }

    private boolean doesVertexExist(V vertex) {
        return adjacencyList.containsKey(vertex);
    }

    /**
     * Depth first traversal
     * <a href="https://www.youtube.com/watch?v=iaBEKo5sM7w&t=27s">Depth First Search Algorithm</a>
     *
     * @param startingVertex Identity of a vertex from the traversal must start
     */
    List<V> depthFirstTraversal(V startingVertex) {

        class DFS {
            private List<V> visited = new LinkedList<>();
            private List<V> outputSequence = new LinkedList<>();
            private Stack<V> stack = new Stack<>();

            private DFS(V vertex) {
                stack.push(vertex);
                outputSequence.add(vertex);
                visited.add(vertex);
            }

            private boolean visitedAdjacentNodes(Edge<V> e) {
                return adjacencyList.get(e.getVertex()).stream().anyMatch(edge -> outputSequence.contains(edge.getVertex()));
            }

            private void searchInDepthFirstOrder(V currentVertex) {
                if (!visited.contains(currentVertex)) visited.add(currentVertex);
                if (!stack.contains(currentVertex)) stack.add(currentVertex);
                if (!outputSequence.contains(currentVertex)) outputSequence.add(currentVertex);
                List<Edge<V>> unVisitedAdjacentNodes = adjacencyList.get(currentVertex).stream()
                        .filter(e -> !visited.contains(e.getVertex()))
                        .collect(Collectors.toList());
                for (Edge<V> e : unVisitedAdjacentNodes) {
                    searchInDepthFirstOrder(e.getVertex());
                    if (visitedAdjacentNodes(e)) {
                        stack.pop();
                    }
                }
            }

            private List<V> getOutputSequence() {
                return outputSequence;
            }
        }

        DFS dfs = new DFS(startingVertex);
        dfs.searchInDepthFirstOrder(startingVertex);
        return dfs.getOutputSequence();
    }


    /**
     * Breadth first traversal
     * <a href="https://www.youtube.com/watch?v=QRq6p9s8NVg">Breadth First Search Algorithm</a>
     *
     * @param startingVertex Identity of a vertex from the traversal must start
     */
    List<V> breadthFirstTraversal(V startingVertex) {
        class BFS {
            private List<V> visited = new LinkedList<>();
            private List<V> outputSequence = new LinkedList<>();
            private Queue<V> queue = new LinkedList<>();

            private BFS(V vertex) {
                outputSequence.add(vertex);
                visited.add(vertex);
            }

            private boolean visitedAdjacentNodes(V vertex) {
                return adjacencyList.get(vertex).stream().anyMatch(edge -> outputSequence.contains(edge.getVertex()));
            }

            private void searchInBreadthFirstOrder(V currentVertex) {
                List<Edge<V>> unVisitedAdjacentNodes = adjacencyList.get(currentVertex).stream()
                        .filter(e -> !visited.contains(e.getVertex()))
                        .collect(Collectors.toList());
                if (unVisitedAdjacentNodes.size() != 0) {
                    for (Edge<V> e : unVisitedAdjacentNodes) {
                        queue.add(e.getVertex());
                        outputSequence.add(e.getVertex());
                        visited.add(e.getVertex());
                    }
                    if (visitedAdjacentNodes(currentVertex)) {
                        searchInBreadthFirstOrder(queue.poll());
                    }
                } else {
                    if (queue.size() > 0) {
                        searchInBreadthFirstOrder(queue.poll());
                    }
                }

            }

            private List<V> getOutputSequence() {
                return outputSequence;
            }
        }
        BFS bfs = new BFS(startingVertex);
        bfs.searchInBreadthFirstOrder(startingVertex);
        return bfs.getOutputSequence();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (V vertex : adjacencyList.keySet()) {
            stringBuilder.append(vertex).append(" : ").append(adjacencyList.get(vertex)).append("\n");
        }
        return stringBuilder.toString();
    }
}

