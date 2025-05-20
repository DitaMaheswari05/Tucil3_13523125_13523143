import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Get the input file name
            System.out.print("Enter the puzzle configuration file name: ");
            String fileName = scanner.nextLine();

            // Read the puzzle configuration
            InputHandler inputHandler = new InputHandler();
            inputHandler.readConfigFromFile(fileName);

            // Get the map of pieces, primary piece, exit door, and the board
            Map<String, Piece> pieces = inputHandler.getPieces();
            PrimaryPiece primaryPiece = inputHandler.getPrimaryPiece();
            PintuKeluar pintuKeluar = inputHandler.getPintuKeluar();
            Papan papan = inputHandler.getPapan();

            // Choose the pathfinding algorithm
            System.out.println("\nSelect pathfinding algorithm:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. A* Search");
            System.out.println("3. Greedy Best-First Search (GBFS)");
            System.out.print("Enter your choice (1-3): ");
            int algorithmChoice = Integer.parseInt(scanner.nextLine());

            // Choose the heuristic function for A* and GBFS
            AStar.HeuristicType heuristicType = AStar.HeuristicType.DISTANCE_TO_EXIT; // default
            GreedyBestFirstSearch.HeuristicType gbfsHeuristicType = GreedyBestFirstSearch.HeuristicType.DISTANCE_TO_EXIT; // default

            if (algorithmChoice == 2 || algorithmChoice == 3) {
                System.out.println("\nSelect heuristic function:");
                System.out.println("1. Distance to Exit");
                System.out.println("2. Blocking Pieces");
                System.out.print("Enter your choice (1-2): ");
                int heuristicChoice = Integer.parseInt(scanner.nextLine());

                if (heuristicChoice == 1) {
                    heuristicType = AStar.HeuristicType.DISTANCE_TO_EXIT;
                } else if (heuristicChoice == 2) {
                    heuristicType = AStar.HeuristicType.BLOCKING_PIECES;
                } else {
                    System.out.println("Invalid choice. Using Distance to Exit as default.");
                }
            }

            // Create the initial state heuristic
            Heuristic initialState = new Heuristic(pieces, pintuKeluar, papan);

            // Solve the puzzle using the selected algorithm
            List<Gerakan> solution = null;
            long startTime = System.currentTimeMillis();

            switch (algorithmChoice) {
                case 1:
                    System.out.println("\nSolving with Uniform Cost Search (UCS)...");
                    UniformCostSearch ucs = new UniformCostSearch(initialState);
                    solution = ucs.solve();
                    break;
                case 2:
                    System.out.println("\nSolving with A* Search...");
                    AStar aStar = new AStar(heuristicType, initialState);
                    solution = aStar.solve();
                    break;
                case 3:
                    System.out.println("\nSolving with Greedy Best-First Search (GBFS)...");
                    GreedyBestFirstSearch gbfs = new GreedyBestFirstSearch(gbfsHeuristicType, initialState);
                    solution = gbfs.solve();
                    break;
                default:
                    System.out.println("Invalid choice. Using Uniform Cost Search as default.");
                    UniformCostSearch defaultUcs = new UniformCostSearch(initialState);
                    solution = defaultUcs.solve();
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            // Print execution statistics
            System.out.println("\nExecution time: " + executionTime + " ms");

            // Create output handler
            OutputHandler outputHandler = new OutputHandler(solution, pieces, pintuKeluar, papan);

            // Ask user if they want to save to file
            System.out.println("\nDo you want to save the solution to a file? (y/n)");
            String saveChoice = scanner.nextLine().trim().toLowerCase();

            if (saveChoice.equals("y") || saveChoice.equals("yes")) {
                System.out.print("Enter the output file name: ");
                String outputFileName = scanner.nextLine();
                outputHandler.saveSolutionToFile(outputFileName);
            }

            // Print the solution to console
            outputHandler.printSolution();

        } catch (IOException e) {
            System.out.println("Error reading/writing file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}