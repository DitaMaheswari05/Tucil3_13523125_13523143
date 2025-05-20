public class PrimaryPiece extends Piece {

    public PrimaryPiece() {
        super("P"); // Primary piece selalu diberi ID 'P'
    }

    @Override
    public void determineOrientation() {
        super.determineOrientation();
        // Memastikan primary piece memiliki dimensi yang valid
        // if (getSize() != 2) {
        // throw new IllegalArgumentException("Primary piece harus memiliki ukuran 2.");
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

            // Horizontal: keluar kiri/kanan
            if (getOrientation() == Orientation.HORIZONTAL) {
                // Kiri
                if (exit.getCol() == -1 && pos.getCol() == 0 && pos.getRow() == exit.getRow()) {
                    return true;
                }
                // Kanan
                if (exit.getCol() == exit.getPosition().getCol() && pos.getCol() == exit.getCol() - 1
                        && pos.getRow() == exit.getRow()) {
                    return true;
                }
            } else {
                // Vertical: keluar atas/bawah
                // Atas
                if (exit.getRow() == -1 && pos.getRow() == 0 && pos.getCol() == exit.getCol()) {
                    return true;
                }
                // Bawah
                if (exit.getRow() == exit.getPosition().getRow() && pos.getRow() == exit.getRow() - 1
                        && pos.getCol() == exit.getCol()) {
                    return true;
                }
            }
        }
        return false;
    }
}