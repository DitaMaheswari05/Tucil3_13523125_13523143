public class Piece {
    private int width; // Lebar piece
    private boolean isHorizontal; // Orientasi (horizontal atau vertikal)
    private String id; // ID piece

    public Piece(int width, boolean isHorizontal, String id) {
        // Lebar piece harus 2 atau 3
        if (width < 2 || width > 3) {
            throw new IllegalArgumentException("Lebar (width) harus 2 atau 3.");
        }

        // ID harus huruf A-Z kecuali P dan K
        if (!id.matches("[A-Z]")) {
            throw new IllegalArgumentException("ID harus A-Z");
        }

        this.width = width;
        this.isHorizontal = isHorizontal;
        this.id = id;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        if (isHorizontal) {
            return 1;
        }
        return width;
    }

    public boolean isHorizontal() {
        return this.isHorizontal;
    }

    public String getId() {
        return this.id;
    }
}
