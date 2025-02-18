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
    private Graph nodeData;

    public AStarSearch(Graph g) {
        this.graph = g.getAdjList();
        this.nodeData = g;
    }

    // Haversine Formula for real-world distance estimation
    private double heuristic(int node, int goal) {
        double[] coord1 = nodeData.getCoordinates(node);
        double[] coord2 = nodeData.getCoordinates(goal);
        return haversineDistance(coord1[0], coord1[1], coord2[0], coord2[1]);
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
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
        // Adding locations with lat/lon (Example: Mumbai, Delhi, Bangalore)
        g.addNode(1, 19.0760, 72.8777); // Mumbai
        g.addNode(2, 28.7041, 77.1025); // Delhi
        g.addNode(3, 12.9716, 77.5946); // Bangalore
        g.addNode(4, 22.5726, 88.3639); // Kolkata

        // Adding weighted edges (road distance)
        g.addEdge(1, 2, 1400); // Mumbai-Delhi
        g.addEdge(1, 3, 980);  // Mumbai-Bangalore
        g.addEdge(2, 4, 1500); // Delhi-Kolkata
        g.addEdge(3, 4, 1800); // Bangalore-Kolkata

        AStarSearch astar = new AStarSearch(g);
        System.out.println("Shortest path: " + astar.findShortestPath(1, 4));
    }
}
