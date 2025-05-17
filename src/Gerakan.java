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
