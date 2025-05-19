import java.util.*;

public class AStar {
    public enum HeuristicType {
        DISTANCE_TO_EXIT,
        BLOCKING_PIECES
    }

    private HeuristicType heuristicType;
    private Heuristic heuristic;

    public AStar(HeuristicType heuristicType, Heuristic heuristic) {
        this.heuristic = heuristic;
        this.heuristicType = heuristicType;
    }

    /**
     * Runs the A* algorithm to find a solution
     * @return List of moves that lead to the solution, or empty list if no solution found
     */

    public List<Gerakan> solve() {
        PriorityQueue<Heuristic> openSet = new PriorityQueue<>(
            (a, b) -> Integer.compare(f(a), f(b)) // compare heuristic values
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
     * g(n) - Path cost function
     * Represents the cost of the path from the initial state to the current state
     * For Rush Hour, this is simply the number of moves made so far
     */
    private int g(Heuristic state) {
        return state.getMoveCount();
    }

    /**
     * f(n) function for the heuristic
     * @param state the state to evaluate
     * @return the heuristic value
     * g(n) + h(n)
     * g(n) is the cost to reach the current state
     * h(n) is the estimated cost to reach the goal state
     */
    private int f(Heuristic state) {
        return g(state) + h(state);
    }
}
