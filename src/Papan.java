public class Papan {
    private int width;
    private int height;
    private Piece[][] pieces;

    public Papan(int width, int height) {
        this.width = width;
        this.height = height;
        this.pieces = new Piece[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Piece getPiece(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Koordinat di luar batas papan.");
        }
        return pieces[x][y];
    }

    public void addPiece(Piece piece, int x, int y) {
        int pieceLength = piece.getWidth();
        boolean isHorizontal = piece.isHorizontal();

        // Validasi: Pastikan piece tidak keluar dari batas papan
        if (isHorizontal) {
            if (x < 0 || x + pieceLength > width || y < 0 || y >= height) {
                throw new IllegalArgumentException("Piece horizontal tidak muat di papan.");
            }
        } else {
            if (x < 0 || x >= width || y < 0 || y + pieceLength > height) {
                throw new IllegalArgumentException("Piece vertikal tidak muat di papan.");
            }
        }

        // Validasi: Pastikan cell yang akan ditempati kosong
        for (int i = 0; i < pieceLength; i++) {
            int checkX = isHorizontal ? x + i : x;
            int checkY = isHorizontal ? y : y + i;
            if (pieces[checkX][checkY] != null) {
                throw new IllegalArgumentException("Cell pada papan sudah terisi.");
            }
        }

        // Tempatkan piece di papan
        for (int i = 0; i < pieceLength; i++) {
            int placeX = isHorizontal ? x + i : x;
            int placeY = isHorizontal ? y : y + i;
            pieces[placeX][placeY] = piece;
        }
    }

    // Fungsi untuk mencetak papan
    public void printPapan() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Piece piece = pieces[j][i];
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
