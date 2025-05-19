import java.util.*;

public class Piece {
    private List<Position> positions;
    private Orientation orientation;
    private String id; // ID piece

    public Piece(String id) {
        if (!id.matches("[A-Z]")) {
            throw new IllegalArgumentException("ID harus huruf A-Z.");
        }
        // Validasi 'P' dan 'K' hanya jika bukan kelas turunan
        if ((id.equals("P") || id.equals("K")) && getClass() == Piece.class) {
            throw new IllegalArgumentException("ID 'P' dan 'K' khusus digunakan untuk primary piece dan pintu keluar.");
        }
        this.id = id;
        this.positions = new ArrayList<>();
    }

    // cctor
    public Piece(Piece piece) {
        this.id = piece.id;
        this.orientation = piece.orientation;
        this.positions = new ArrayList<>();
        for (Position position : piece.positions) {
            this.positions.add(new Position(position));
        }
    }

    public void addPosition(int row, int col) {
        if(positions.size() >= 3) {
            throw new IllegalStateException("Piece tidak boleh memiliki lebih dari 3 posisi.");
        }
        positions.add(new Position(row, col));
    }

    public void determineOrientation() {
        if (positions.size() < 2 || positions.size() > 3) {
            throw new IllegalArgumentException("Lebar piece harus 2 atau 3 posisi.");
        }

        int firstRow = positions.get(0).getRow();
        boolean allSameRow = true;

        for (int i = 1; i < positions.size(); i++) {
            if (positions.get(i).getRow() != firstRow) {
                allSameRow = false;
                break;
            }
        }
        orientation = allSameRow ? Orientation.HORIZONTAL : Orientation.VERTICAL;

        // sort position
        if(orientation == Orientation.HORIZONTAL) {
            positions.sort((p1, p2) -> Integer.compare(p1.getCol(), p2.getCol()));
        }
        else {
            positions.sort((p1, p2) -> Integer.compare(p1.getRow(), p2.getRow()));
        }
    }
    public void move(Direction direction) {
        for (Position position : positions) {
            position.gerakan(direction);
        }
    }
    public String getId() {
        return id;
    }
    public List<Position> getPositions() {
        return positions;
    }
    public void setPositions(List<Position> positions) {
        this.positions = new ArrayList<>(positions);
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public Position getStartPosition() {
        return positions.get(0);
    }
    public Position getEndPosition() {
        return positions.get(positions.size() - 1);
    }
    public int getSize() {
        return positions.size();
    }

    @Override
    public boolean equals(Object obj) {
        // bandingin apakah dua piece itu sama
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        if(!id.equals(piece.id)) return false;
        if(!positions.equals(piece.positions)) return false;
        if(orientation != piece.orientation) return false;

        for (int i = 0; i < positions.size(); i++) {
            if (!positions.get(i).equals(piece.positions.get(i))) {
                return false;
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        // menghasilkan kode hash unik dari piece
        return Objects.hash(id, positions, orientation);
    }
}
