
public class PrimaryPiece extends Piece {

    public PrimaryPiece(int width, boolean isHorizontal, String id) {
        super(width, isHorizontal, id);
    }
    public boolean isPrimaryPiece() {
        return true;
    }

    public boolean isAtExit(Papan papan, PintuKeluar exit) {
        for (int x = 0; x < papan.getWidth(); x++) {
            for (int y = 0; y < papan.getHeight(); y++) {
                Piece piece = papan.getPiece(x, y);
                if (piece != null && piece == this) {
                    if (x == exit.getX() && y == exit.getY()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}