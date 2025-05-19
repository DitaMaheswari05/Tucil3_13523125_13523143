import java.util.*;

public class UniformCostSearch {

    private Heuristic heuristic;

    public UniformCostSearch(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Runs the UCS algorithm to find a solution
     * @return List of moves that lead to the solution, or empty list if no solution found
     */

    public List<Gerakan> solve() {
        PriorityQueue<Heuristic> openSet = new PriorityQueue<>(
            (a, b) -> Integer.compare(g(a), g(b)) // compare cost only
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
                if (closedSet.contains(successor)) {
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
     * f(n) function for the heuristic
     * @param state the state to evaluate
     * @return the heuristic value
     * f(n) = g(n)
     */
    private int f(Heuristic state) {
        return g(state);
    }

    // g(n) for count cost
    private int g(Heuristic state) {
        return state.getMoveCount();
    }
}
