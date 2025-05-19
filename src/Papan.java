public class Papan {
    private int width;
    private int height;
    private Piece[][] pieces;

    public Papan(int width, int height) {
        this.width = width;
        this.height = height;
        this.pieces = new Piece[height][width]; // row x col
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) {
            throw new IllegalArgumentException("Koordinat di luar batas papan.");
        }
        return pieces[row][col];
    }

    public void addPiece(Piece piece) {
        // Validasi: Pastikan posisi piece valid dan belum ditempati
        for (Position pos : piece.getPositions()) {
            int row = pos.getRow();
            int col = pos.getCol();
            if (row < 0 || row >= height || col < 0 || col >= width) {
                throw new IllegalArgumentException("Piece keluar dari batas papan.");
            }
            if (pieces[row][col] != null) {
                throw new IllegalArgumentException("Cell pada papan sudah terisi.");
            }
        }

        // Tempatkan piece ke papan sesuai posisi-posisinya
        for (Position pos : piece.getPositions()) {
            int row = pos.getRow();
            int col = pos.getCol();
            pieces[row][col] = piece;
        }
    }

    public void removePiece(Piece piece) {
        // Hapus piece dari papan
        for (Position pos : piece.getPositions()) {
            int row = pos.getRow();
            int col = pos.getCol();
            pieces[row][col] = null;
        }
    }

    public void clear() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pieces[row][col] = null;
            }
        }
    }
    

    // Fungsi untuk mencetak papan
    public void printPapan() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Piece piece = pieces[row][col];
                if (piece != null) {
                    System.out.print(piece.getId() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}