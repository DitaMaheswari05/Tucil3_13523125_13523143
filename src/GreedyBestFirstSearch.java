import java.util.*;

public class GreedyBestFirstSearch {
    public enum HeuristicType {
        DISTANCE_TO_EXIT,
        BLOCKING_PIECES
    }

    private HeuristicType heuristicType;
    private Heuristic heuristic;

    public GreedyBestFirstSearch(HeuristicType heuristicType, Heuristic heuristic) {
        this.heuristic = heuristic;
        this.heuristicType = heuristicType;
    }

    /**
     * Runs the GBFS algorithm to find a solution
     * @return List of moves that lead to the solution, or empty list if no solution found
     */

    public List<Gerakan> solve() {
        PriorityQueue<Heuristic> openSet = new PriorityQueue<>(
            (a, b) -> Integer.compare(h(a), h(b)) // compare heuristic values
        );

        // closed set to track of visited states
        Set<Heuristic> closedSet = new HashSet<>();
        // add the initial state to the open set
        openSet.add(heuristic);

        // keep tracj of the number of iterations
        int explored = 0;
        while (!openSet.isEmpty()) {
            // get the state with the lowest heuristic value
            Heuristic current = openSet.poll();
            explored++;

            // check if the current state is the goal state
            if (current.isGoal()) {
                System.out.println("Found solution in " + explored + " iterations.");
                return current.getMoveHistory(); // return the moves that lead to the solution
            }
            // add the current state to the closed set
            closedSet.add(current);

            // generate successors
            List<Heuristic> successors = current.getSuccessors();
            for (Heuristic successor : successors) {
                if (!closedSet.contains(successor)) {
                    continue;
                }
                if (!openSet.contains(successor)) {
                    openSet.add(successor);
                }
            }
        }
        System.out.println("No solution found.");
        return new ArrayList<>(); // return empty list if no solution found
    }

    /**
     * Heuristic function to evaluate the state
     * @param state the state to evaluate
     * @return the heuristic value
     */

    private int h(Heuristic state) {
        switch (heuristicType) {
            case DISTANCE_TO_EXIT:
                return state.distanceToExit();
            case BLOCKING_PIECES:
                return state.countBlockingPieces();
            default:
                throw new IllegalArgumentException("Unknown heuristic type: " + heuristicType);
        }
    }
    /**
     * f(n) function for the heuristic
     * @param state the state to evaluate
     * @return the heuristic value
     */
    private int f(Heuristic state) {
        return h(state);
    }

    // g(n) for counting the number of moves
    private int g(Heuristic state) {
        return state.getMoveCount();
    }
}
