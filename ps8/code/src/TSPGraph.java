import java.util.ArrayList;
import java.util.List;

public class TSPGraph implements IApproximateTSP {

    @Override
    public void MST(TSPMap map) {
        // TODO: implement this method
        // Prim's algorithm
        int numNodes = map.getCount();
        TreeMapPriorityQueue<Double, Integer> pQueue = new TreeMapPriorityQueue<>();
        int startNode = 0;
        Integer[] parents = new Integer[numNodes];
        parents[0] = startNode;
        // adding all nodes to the priority queue
        for (int i = 1; i < numNodes; i++) { // O(n)
            pQueue.add(i, map.pointDistance(startNode, i));
            parents[i] = startNode;
        }
        while (!pQueue.isEmpty()) { // O(n^2)
            Integer curr = pQueue.extractMin();
            map.setLink(curr, parents[curr]);
            for (int i = 1; i < numNodes; i++) { // O(n)
                if (map.getLink(i) >= 0) {
                    continue;
                }
                if (map.pointDistance(curr, i) < pQueue.lookup(i)) {
                    pQueue.decreasePriority(i, map.pointDistance(curr, i));
                    parents[i] = curr;
                }
            }
        }
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // TODO: implement the rest of this method.
        int startNode = -1;
        for (int i = 0; i < map.getCount(); i++) { // O(n)
            if (map.getLink(i) == -1) {
                startNode = i;
                break;
            }
        }
        List<Integer> path = new ArrayList<>();
        path.add(startNode);
        DFS(startNode, path, map);
        // update links using DFS path quietly
        for (int i = 0; i < path.size() - 1; i++) { // O(n)
            map.setLink(path.get(i), path.get(i + 1), false);
        }
        // connect first and last node of DFS path and redraw
        map.setLink(path.get(path.size() - 1), 0, true);
    }

    public void DFS(int startNode, List<Integer> path, TSPMap map) { // O(n^2)
        if (path.size() == map.getCount()) {
            return;
        }
        for (int i = 0; i < map.getCount(); i++) {
            if (!path.contains(i) && map.getLink(i) == startNode) {
                path.add(i);
                DFS(i, path, map);
            }
        }
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        // A valid tour is one where each node is visited exactly once.
        int startNode = 0;
        List<Integer> testPath = new ArrayList<>();
        testPath.add(startNode);
        int curr = map.getLink(startNode);
        while (curr != startNode) {
            int nextNode = map.getLink(curr);
            testPath.add(curr);
            if (nextNode == startNode) { break; }
            if (nextNode < 0 || testPath.contains(nextNode)) { return false; }
            curr = nextNode;
        }
        return testPath.size() == map.getCount();
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        if (!isValidTour(map)) {
            return -1;
        }
        int startNode = 0;
//        List<Integer> testPath = new ArrayList<Integer>();
//        testPath.add(startNode);
        int curr = map.getLink(startNode);
        double distance = map.pointDistance(startNode, curr);
        while (curr != startNode) {
            int nextNode = map.getLink(curr);
            //testPath.add(curr);
            distance += map.pointDistance(curr, nextNode);
            curr = nextNode;
        }
        return distance;
    }


    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "/Users/keagan/Documents/NUS/Y1S2/CS2040S Data Structures & Algorithms/Problem Sets/ps8/code/fiftypoints.txt");
        TSPGraph graph = new TSPGraph();

        //graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
