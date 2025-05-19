import java.util.*;

public class Heuristic {
    private Map<String, Piece> pieces;
    private List<Gerakan> moveHistory;
    private PrimaryPiece primaryPiece;
    private PintuKeluar pintuKeluar;
    private Papan papan;

    public Heuristic(Map<String, Piece> pieces, PintuKeluar pintuKeluar, Papan papan) {
        this.pieces = pieces;
        this.moveHistory = new ArrayList<>();
        this.pintuKeluar = pintuKeluar;
        this.papan = papan;

        // deep copy pieces
        for(String pieceId : pieces.keySet()) {
            Piece originalPiece = pieces.get(pieceId);
            if(originalPiece instanceof PrimaryPiece) {
                this.primaryPiece = new PrimaryPiece();

                for(Position position : originalPiece.getPositions()) {
                    this.primaryPiece.addPosition(position.getRow(), position.getCol());
                }
                this.primaryPiece.determineOrientation();
                this.pieces.put(pieceId, this.primaryPiece);
            }
            else {
                Piece newPiece = new Piece(originalPiece);
                this.pieces.put(pieceId, newPiece);
            }
        }
        this.papan.clear();
        for (Piece piece : this.pieces.values()) {
            this.papan.addPiece(piece);
        }
    }

    private Heuristic(Heuristic parentState, Gerakan move) {
        this.papan = new Papan(parentState.papan.getWidth(), parentState.papan.getHeight());
        this.pieces = new HashMap<>();
        this.pintuKeluar = parentState.pintuKeluar;
    
        // copy all pieces
        for (String pieceId : parentState.pieces.keySet()) {
            Piece originalPiece = parentState.pieces.get(pieceId);
    
            if(originalPiece instanceof PrimaryPiece) {
                this.primaryPiece = new PrimaryPiece();
    
                for(Position position : originalPiece.getPositions()) {
                    this.primaryPiece.addPosition(position.getRow(), position.getCol());
                }
                this.primaryPiece.determineOrientation();
                this.pieces.put(pieceId, this.primaryPiece);
            }
            else {
                Piece newPiece = new Piece(originalPiece);
                this.pieces.put(pieceId, newPiece);
            }
        }
    
        // **Clear papan supaya posisi lama hilang**
        this.papan.clear();
    
        // copy move history and add new move
        this.moveHistory = new ArrayList<>(parentState.moveHistory);
        this.moveHistory.add(move);
    
        // apply the move to this state
        applyMove(move);
    
        // places the pieces on the board
        for (Piece piece : this.pieces.values()) {
            try {
                this.papan.addPiece(piece);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Invalid move: " + e.getMessage());
            }
        }
    }
    

    private void applyMove(Gerakan move) {
        String pieceId = move.getPieceID();
        Direction direction = move.getDirection();
        Piece piece = pieces.get(pieceId);

        if(piece != null) {
            piece.move(direction);
        } else {
            throw new IllegalArgumentException("Piece with ID " + pieceId + " not found.");
        }
    }

    // make successors
    public List<Heuristic> getSuccessors() {
        List<Heuristic> successors = new ArrayList<>();

        for(String pieceId : pieces.keySet()) {
            Piece piece = pieces.get(pieceId);

            for (Direction direction : Direction.values()) {
                Gerakan move = new Gerakan(pieceId, direction);

                // skip invalid moves
                if(!move.isValidForPiece(piece)) {
                    continue;
                }
                // check if the move is valid
                if (isValidMove(piece, direction)) {
                // create a new state with the move applied
                    Heuristic successor = new Heuristic(this, move);
                    successors.add(successor);
                }
            }
        }
        return successors;
    }

    private boolean isValidMove(Piece piece, Direction direction) {
        Piece tempPiece = new Piece(piece);
        tempPiece.move(direction);

        // check if any position is outside the board or collides with another piece
        for (Position position : tempPiece.getPositions()) {
            int row = position.getRow();
            int col = position.getCol();
        
            if (row < 0 || row >= papan.getHeight() || col < 0 || col >= papan.getWidth()) {
                return false; // out of bounds
            }

            // check for collision with other pieces
            Piece otherPiece = papan.getPiece(row, col);
            if (otherPiece != null && !otherPiece.getId().equals(piece.getId())) {
                return false; // collision with another piece
            }
        }
        return true; // valid move
    }

    // check if the primary piece is at the exit
    public boolean isGoal() {
        return primaryPiece.canExitAt(pintuKeluar);
    }

    // heuristic 1 : distance to exit
    public int distanceToExit() {
        Position exitPosition = pintuKeluar.getPosition();

        if(primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
            Position endPosition = primaryPiece.getEndPosition();
            if(exitPosition.getRow() == endPosition.getRow()) {
                return Math.abs(exitPosition.getCol() - endPosition.getCol());
            }
            else {
                return Integer.MAX_VALUE;
            }
        }
        else {
            Position endPosition = primaryPiece.getEndPosition();
            if(exitPosition.getCol() == endPosition.getCol()) {
                return Math.abs(exitPosition.getRow() - endPosition.getRow());
            }
            else {
                return Integer.MAX_VALUE;
            }
        }
    }

    // heuristic 2 :  count the number of blocking pieces between the primary piece and the exit door
    public int countBlockingPieces() {
        int count = 0;
        Position exitPosition = pintuKeluar.getPosition();

        if(primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
            Position endPosition = primaryPiece.getEndPosition();
            int row = endPosition.getRow();

            int startCol = endPosition.getCol() + 1;
            int endCol = exitPosition.getCol();

            for (int col = startCol; col <= endCol; col++) {
                if (col < papan.getWidth() && papan.getPiece(row, col) != null) {
                    count++;
                }
            }
        }
        else {
            Position endPosition = primaryPiece.getEndPosition();
            int col = endPosition.getCol();

            int startRow = endPosition.getRow() + 1;
            int endRow = exitPosition.getRow();

            for (int row = startRow; row <= endRow; row++) {
                if (row < papan.getHeight() && papan.getPiece(row, col) != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public Map<String, Piece> getPieces() {
        return pieces;
    }

    public List<Gerakan> getMoveHistory() {
        return moveHistory;
    }

    public PrimaryPiece getPrimaryPiece() {
        return primaryPiece;
    }

    public Papan getPapan() {
        return papan;
    }

    public PintuKeluar getPintuKeluar() {
        return pintuKeluar;
    }

    // count the number of moves made
    public int getMoveCount() {
        return moveHistory.size();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Heuristic other = (Heuristic) obj;

        // compare pieces positions
        if(this.pieces.size() != other.pieces.size()) return false;

        for(String pieceId : this.pieces.keySet()) {
            Piece thisPiece = this.pieces.get(pieceId);
            Piece otherPiece = other.pieces.get(pieceId);

            if (otherPiece == null || !thisPiece.equals(otherPiece)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7; // initial hash value
        for (String pieceId : pieces.keySet()) {
            Piece piece = pieces.get(pieceId);
            hash = 31 * hash + (piece != null ? piece.hashCode() : 0); // combine hash codes
        }
        return hash;
    }

    // print the current board
    public void currentBoard() {
        papan.printPapan();
    }
}

