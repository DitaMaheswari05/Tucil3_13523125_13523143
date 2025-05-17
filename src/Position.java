// ngetrack posisi piece di board dengan koordinat baris dan kolom.

import java.util.Objects;

public class Position {
    private int row;
    private int col;


    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public Position(Position other) {
        this.row = other.row;
        this.col = other.col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public void gerakan(Direction direction) {
        switch (direction) {
            case UP:
                row--;
                break;
            case DOWN:
                row++;
                break;
            case LEFT:
                col--;
                break;
            case RIGHT:
                col++;
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
}
