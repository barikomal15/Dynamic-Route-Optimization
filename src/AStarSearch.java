import java.util.*;

class AStarSearch {
    static class Node implements Comparable<Node> {
        int id;
        double g, h, f;

        Node(int id, double g, double h) {
            this.id = id;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.f, other.f);
        }
    }

    private Map<Integer, List<Graph.Edge>> graph;
    private Map<Integer, Double> heuristics;

    public AStarSearch(Graph g) {
        this.graph = g.getAdjList();
        this.heuristics = new HashMap<>();
    }

    // Dummy heuristic function (replace with real distance calculation)
    private double heuristic(int node, int goal) {
        return Math.abs(node - goal);
    }

    public List<Integer> findShortestPath(int start, int goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Integer, Double> gScore = new HashMap<>();
        Map<Integer, Integer> cameFrom = new HashMap<>();

        openSet.add(new Node(start, 0, heuristic(start, goal)));
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.id == goal) {
                return reconstructPath(cameFrom, current.id);
            }

            for (Graph.Edge edge : graph.getOrDefault(current.id, new ArrayList<>())) {
                double tentativeG = gScore.get(current.id) + edge.weight;

                if (tentativeG < gScore.getOrDefault(edge.target, Double.MAX_VALUE)) {
                    cameFrom.put(edge.target, current.id);
                    gScore.put(edge.target, tentativeG);
                    double fScore = tentativeG + heuristic(edge.target, goal);
                    openSet.add(new Node(edge.target, tentativeG, fScore));
                }
            }
        }
        return new ArrayList<>(); // No path found
    }

    private List<Integer> reconstructPath(Map<Integer, Integer> cameFrom, int current) {
        List<Integer> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        path.add(current);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge(1, 2, 4);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 1);
        g.addEdge(2, 4, 5);
        g.addEdge(3, 4, 8);
        
        AStarSearch astar = new AStarSearch(g);
        System.out.println("Shortest path: " + astar.findShortestPath(1, 4));
    }
}
