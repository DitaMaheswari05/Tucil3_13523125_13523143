public class PrimaryPiece extends Piece {

    public PrimaryPiece() {
        super("P"); // Primary piece selalu diberi ID 'P'
    }

    @Override
    public void determineOrientation() {
        super.determineOrientation();
        // Memastikan primary piece memiliki dimensi yang valid
        // if (getSize() != 2) {
        //     throw new IllegalArgumentException("Primary piece harus memiliki ukuran 2.");
        // }
    }

    public boolean canExitAt(PintuKeluar exit) {
        // memeriksa apakah orientasi primary piece cocok dengan pintu keluar
        if (getOrientation() == Orientation.HORIZONTAL && !exit.isHorizontal()) {
            return false;
        }
        if (getOrientation() == Orientation.VERTICAL && exit.isHorizontal()) {
            return false;
        }

        // Memeriksa apakah primary piece berada di posisi pintu keluar
        for (Position pos : getPositions()) {
            if (pos.getRow() == exit.getRow() && pos.getCol() == exit.getCol()) {
                return true;
            }
        }
        return false;
    }
}