public class Gerakan {
    private String pieceID;
    private Direction direction;

    public Gerakan(String pieceID, Direction direction) {
        this.pieceID = pieceID;
        this.direction = direction;
    }
    
    public String getPieceID() {
        return pieceID;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    // validasi apakah gerakan sesuai dengan orientasi piece
    public boolean isValidForPiece(Piece piece) {
        if (piece == null) {
            return false;
        }
        
        Orientation orientation = piece.getOrientation();
        
        // piece horizontal hanya bisa bergerak kiri-kanan
        if (orientation == Orientation.HORIZONTAL) {
            return direction == Direction.LEFT || direction == Direction.RIGHT;
        }
        // piece vertical hanya bisa bergerak atas-bawah
        else if (orientation == Orientation.VERTICAL) {
            return direction == Direction.UP || direction == Direction.DOWN;
        }
        
        return false;
    }

    @Override
    public String toString() {
        String dirString = "";
        switch (direction) {
            case UP:
                dirString = "atas";
                break;
            case DOWN:
                dirString = "bawah";
                break;
            case LEFT:
                dirString = "kiri";
                break;
            case RIGHT:
                dirString = "kanan";
                break;
        }
        return pieceID + "-" + dirString;
    }
}