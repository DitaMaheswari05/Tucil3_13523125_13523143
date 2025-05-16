import java.io.*;
import java.util.*;

public class InputHandler {

    public static Papan processInputFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        // baca ukuran papan
        String[] size = br.readLine().split(" ");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);

        // buat papan
        Papan papan = new Papan(width, height);

        // baca jumlah piece
        int pieceCount = Integer.parseInt(br.readLine());

        // Membaca representasi papan
        char[][] board = new char[height][width];
        for (int i = 0; i < height; i++) {
            String line = br.readLine();
            board[i] = line.toCharArray();
        }

        // Map untuk menyimpan pieces
        Map<Character, Piece> pieces = new HashMap<>();

        // Proses mengisi papan dengan piece
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char id = board[i][j];
                if (id != '.') {
                    if (!pieces.containsKey(id)) {
                        // Cari dimensi dan orientasi piece
                        Piece newPiece = findPieceDimensions(board, id, i, j);
                        pieces.put(id, newPiece);
                        papan.addPiece(newPiece, j, i);
                    }
                }
            }
        }

        br.close();
        return papan;
    }

    private static Piece findPieceDimensions(char[][] board, char id, int startRow, int startCol) {
        int height = 0, width = 0;
        boolean isHorizontal = false;

        // Cek horizontal
        for (int col = startCol; col < board[0].length && board[startRow][col] == id; col++) {
            width++;
        }

        // Cek vertikal
        for (int row = startRow; row < board.length && board[row][startCol] == id; row++) {
            height++;
        }

        // Tentukan orientasi
        isHorizontal = width > height;

        // Validasi dimensi
        if (!(width == 1 || height == 1)) {
            throw new IllegalArgumentException("Piece tidak valid (bukan rectangular).");
        }

        if (id == 'P') {
            return new PrimaryPiece(Math.max(width, height), isHorizontal);
        } else {
            return new Piece(Math.max(width, height), isHorizontal, String.valueOf(id));
        }
    }
}