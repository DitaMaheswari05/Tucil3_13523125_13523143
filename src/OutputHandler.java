import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputHandler {
    private List<Gerakan> moveHistory;
    private Map<String, Piece> initialPieces;
    private PintuKeluar pintuKeluar;
    private Papan initialPapan;

    public OutputHandler(List<Gerakan> moveHistory, Map<String, Piece> initialPieces,
            PintuKeluar pintuKeluar, Papan initialPapan) {
        this.moveHistory = moveHistory;
        this.initialPieces = initialPieces;
        this.pintuKeluar = pintuKeluar;
        this.initialPapan = initialPapan;
    }

    /**
     * Prints the solution sequence to the console
     */
    public void printSolution() {
        if (moveHistory.isEmpty()) {
            System.out.println("No solution found.");
            return;
        }

        System.out.println("Papan Awal");
        printBoard(initialPapan, pintuKeluar, null);
        System.out.println();

        // Create a deep copy of the initial state
        Map<String, Piece> currentPieces = deepCopyPieces(initialPieces);
        Papan currentPapan = new Papan(initialPapan.getWidth(), initialPapan.getHeight());

        // Place pieces on the board
        for (Piece piece : currentPieces.values()) {
            currentPapan.addPiece(piece);
        }

        boolean primaryPieceExited = false;

        // Execute each move and print the board state
        for (int i = 0; i < moveHistory.size(); i++) {
            Gerakan move = moveHistory.get(i);
            System.out.println("Gerakan " + (i + 1) + ": " + move);

            // Apply the move
            String pieceId = move.getPieceID();
            Piece piece = currentPieces.get(pieceId);

            // Create a new board for this state
            currentPapan = new Papan(initialPapan.getWidth(), initialPapan.getHeight());

            // Apply the move to the piece
            piece.move(move.getDirection());

            // Check if primary piece has exited
            if (pieceId.equals("P") && ((PrimaryPiece)piece).canExitAt(pintuKeluar)) {
                primaryPieceExited = true;
                // If primary piece exited, remove it from the current pieces
                currentPapan.removePiece(piece);
                currentPieces.remove("P");
                System.out.println("Primary piece successfully exited!");
            }

            // Place all pieces on the new board
            for (Piece p : currentPieces.values()) {
                if (!(p instanceof PrimaryPiece) || !primaryPieceExited) {
                    currentPapan.addPiece(p);
                }
            }

            // Print the board state
            printBoard(currentPapan, pintuKeluar, pieceId);
            System.out.println();
        }

        System.out.println("Solution found with " + moveHistory.size() + " moves.");
    }

    /**
     * Creates a deep copy of the pieces map
     * 
     * @param pieces the original pieces map
     * @return a deep copy of the pieces map
     */
    private Map<String, Piece> deepCopyPieces(Map<String, Piece> pieces) {
        Map<String, Piece> copy = new HashMap<>();
        for (String pieceId : pieces.keySet()) {
            Piece originalPiece = pieces.get(pieceId);

            if (originalPiece instanceof PrimaryPiece) {
                PrimaryPiece primaryCopy = new PrimaryPiece();
                for (Position pos : originalPiece.getPositions()) {
                    primaryCopy.addPosition(pos.getRow(), pos.getCol());
                }
                primaryCopy.determineOrientation();
                copy.put(pieceId, primaryCopy);
            } else {
                Piece pieceCopy = new Piece(originalPiece);
                copy.put(pieceId, pieceCopy);
            }
        }
        return copy;
    }

    /**
     * Saves the solution sequence to a text file
     * 
     * @param filename the name of the file to save to
     * @throws IOException if there's an error writing to the file
     */
    public void saveSolutionToFile(String filename) throws IOException {
        if (moveHistory.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("No solution found.");
            }
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Papan Awal");
            saveBoardToFile(writer, initialPapan);
            writer.println();

            // Create a deep copy of the initial state
            Map<String, Piece> currentPieces = deepCopyPieces(initialPieces);
            Papan currentPapan = new Papan(initialPapan.getWidth(), initialPapan.getHeight());

            // Place pieces on the board
            for (Piece piece : currentPieces.values()) {
                currentPapan.addPiece(piece);
            }

            // Execute each move and save the board state
            for (int i = 0; i < moveHistory.size(); i++) {
                Gerakan move = moveHistory.get(i);
                writer.println("Gerakan " + (i + 1) + ": " + move);

                // Apply the move
                String pieceId = move.getPieceID();
                Piece piece = currentPieces.get(pieceId);

                // Create a new board for this state
                currentPapan = new Papan(initialPapan.getWidth(), initialPapan.getHeight());

                // Apply the move to the piece
                piece.move(move.getDirection());

                // Place all pieces on the new board
                for (Piece p : currentPieces.values()) {
                    currentPapan.addPiece(p);
                }

                // Save the board state
                saveBoardToFile(writer, currentPapan);
                writer.println();
            }

            writer.println("Solution found with " + moveHistory.size() + " moves.");
            System.out.println("Solution saved to file: " + filename);
        }
    }

    /**
     * Saves the board state to a file
     * 
     * @param writer the PrintWriter to write to
     * @param papan  the board to save
     */
    private void saveBoardToFile(PrintWriter writer, Papan papan) {
        int height = papan.getHeight();
        int width = papan.getWidth();

        for (int row = 0; row < height; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < width; col++) {
                Piece piece = papan.getPiece(row, col);

                if (piece != null) {
                    line.append(piece.getId());
                } else {
                    // Check if this is the exit door position
                    if (pintuKeluar.getRow() == row && pintuKeluar.getCol() == col) {
                        line.append("K");
                    } else {
                        line.append(".");
                    }
                }
            }
            writer.println(line.toString());
        }
    }

    /**
     * Prints the board state with colored output for the primary piece, exit door,
     * and moved piece
     * s
     * 
     * @param papan the board to print
     */
    private void printBoard(Papan papan, PintuKeluar pintuKeluar, String movedPieceId) {
        // ANSI color codes
        final String RESET = "\u001B[0m";
        final String PRIMARY_COLOR = "\u001B[32m"; // Green for primary piece
        final String EXIT_COLOR = "\u001B[35m"; // Purple for exit door
        final String MOVED_COLOR = "\u001B[31m"; // Red for moved piece

        int height = papan.getHeight();
        int width = papan.getWidth();

        boolean isHorizontalExit = pintuKeluar.isHorizontal();
        int exitRow = pintuKeluar.getRow();
        int exitCol = pintuKeluar.getCol();

        // Print top exit if it exists (exitRow == -1)
        if (exitRow == -1) {
            // Print top border with exit
            for (int col = 0; col < width; col++) {
                if (col == exitCol) {
                    System.out.print(EXIT_COLOR + "K" + RESET);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        // Print the board rows
        for (int row = 0; row < height; row++) {
            // Print left exit if it exists (exitCol == -1 and current row matches exitRow)
            if (exitCol == -1 && row == exitRow) {
                System.out.print(EXIT_COLOR + "K" + RESET);
            } else if (exitCol == -1) {
                System.out.print(" ");
            }

            // Print board content
            for (int col = 0; col < width; col++) {
                Piece piece = papan.getPiece(row, col);

                if (piece != null) {
                    String pieceId = piece.getId();

                    if (pieceId.equals("P")) {
                        System.out.print(PRIMARY_COLOR + pieceId + RESET);
                    } else if (movedPieceId != null && pieceId.equals(movedPieceId)) {
                        System.out.print(MOVED_COLOR + pieceId + RESET);
                    } else {
                        System.out.print(pieceId);
                    }
                } else {
                    System.out.print(".");
                }
            }

            // Print right exit if it exists (exitCol == width and current row matches
            // exitRow)
            if (exitCol == width && row == exitRow) {
                System.out.print(EXIT_COLOR + "K" + RESET);
            }

            System.out.println();
        }

        // Print bottom exit if it exists (exitRow == height)
        if (exitRow == height) {
            // Print bottom border with exit
            for (int col = 0; col < width; col++) {
                if (col == exitCol) {
                    System.out.print(EXIT_COLOR + "K" + RESET);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}