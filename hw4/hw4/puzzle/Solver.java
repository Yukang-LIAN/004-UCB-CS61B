package hw4.puzzle;


import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {

    private class Node {
        private WorldState state;
        private int move;
        private Node prev;

        public Node(WorldState state, int move, Node prev) {
            this.state = state;
            this.move = move;
            this.prev = prev;
        }
    }

    private class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            int o1Priority = o1.move + getEdt(o1);
            int o2Priority = o2.move + getEdt(o2);
            return o1Priority - o2Priority;
        }
    }

    private int getEdt(Node n) {
        if (!edtCaches.containsKey(n)) {
            edtCaches.put(n.state, n.state.estimatedDistanceToGoal());
        }
        return edtCaches.get(n.state);
    }

    private Map<WorldState, Integer> edtCaches = new HashMap<>();
    private List<WorldState> solution = new ArrayList<>();

    /**
     * * Constructor which solves the puzzle, computing
     * * everything necessary for moves() and solution() to
     * * not have to solve the problem again. Solves the
     * * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        MinPQ<Node> pq = new MinPQ<>(new NodeComparator());
        Node currentNode = new Node(initial, 0, null);
        pq.insert(currentNode);

        while (!pq.isEmpty()) {
            currentNode = pq.delMin();
            if (currentNode.state.isGoal()) {
                break;
            }
            for (WorldState w : currentNode.state.neighbors()) {
                Node newNode = new Node(w, currentNode.move + 1, currentNode);
                if ((currentNode.prev != null) && (w.equals(currentNode.prev.state))) {
                    continue;
                }
                pq.insert(newNode);
            }
        }

        Stack<WorldState> path = new Stack<>();
        for (Node node = currentNode; node != null; node = node.prev) {
            path.push(node.state);
        }
        while (!path.isEmpty()) {
            solution.add(path.pop());
        }
    }

    /**
     * * Returns the minimum number of moves to solve the puzzle starting
     * * at the initial WorldState.
     */
    public int moves() {
        return solution.size() - 1;
    }

    /**
     * * Returns a sequence of WorldStates from the initial WorldState
     * * to the solution.
     */
    public Iterable<WorldState> solution() {
        return solution;
    }
}
