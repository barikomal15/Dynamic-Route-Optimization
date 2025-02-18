import java.util.*;

class Graph {
    private Map<Integer, List<Edge>> adjList = new HashMap<>();

    static class Edge {
        int target;
        double weight;

        Edge(int target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    public void addEdge(int u, int v, double weight){
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, weight));
        adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(u, weight));
    }

    public void printGraph(){
        for (var entry : adjList.entrySet()) {
            System.out.print("Node " + entry.getKey() + " -> ");
            for (Edge edge : entry.getValue()) {
                System.out.print("(" + edge.target + ", " + edge.weight + ")");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addEdge(1, 2, 4.5);
        graph.addEdge(1, 3,2.0);
        graph.addEdge(2, 3, 1.5);
        graph.printGraph();
    }
}