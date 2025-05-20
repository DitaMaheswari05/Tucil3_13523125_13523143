import java.io.*;
import java.util.*;

public class InputHandler {
    private int width;
    private int height;
    private int numPieces;
    private Map<String, Piece> pieces;
    private PrimaryPiece primaryPiece;
    private PintuKeluar pintuKeluar;
    private Papan papan;

    public InputHandler() {
        pieces = new HashMap<>();
    }

    /**
     * Reads the puzzle configuration from a file
     * 
     * @param fileName the name of the file to read
     * @throws IOException if there's an error reading the file
     */
    public void readConfigFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        // Read dimensions
        String[] dimensions = reader.readLine().split(" ");
        height = Integer.parseInt(dimensions[0]);
        width = Integer.parseInt(dimensions[1]);

        // Read number of non-primary pieces (unused in this example, but read anyway)
        numPieces = Integer.parseInt(reader.readLine());

        // Read all remaining lines (including possible extra border lines)
        ArrayList<String> allLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                allLines.add(line);
            }
        }
        reader.close();

        // Cari posisi K (pintu keluar)
        // Bisa di luar papan (di baris sebelum papan, sesudah papan, atau di luar
        // kolom)
        boolean foundK = false;
        int exitRow = -1, exitCol = -1;
        boolean isHorizontal = false;

        // Misal papan dimulai di baris tertentu dalam allLines:
        // Karena kita baca height baris papan, coba cari window baris mana yang jadi
        // papan:
        // Asumsi: papan ada di tengah allLines, bisa coba cari window height baris yang
        // cocok (atau asumsi langsung di tengah)

        // Sederhana: cari semua K di allLines, cari posisi dengan memperhitungkan papan
        // di tengah:
        for (int i = 0; i < allLines.size(); i++) {
            String currLine = allLines.get(i);
            for (int j = 0; j < currLine.length(); j++) {
                if (currLine.charAt(j) == 'K') {
                    foundK = true;
                    exitRow = i;
                    exitCol = j;
                    break;
                }
            }
            if (foundK)
                break;
        }

        if (!foundK) {
            throw new IllegalArgumentException("Pintu keluar (K) tidak ditemukan di papan.");
        }

        // Sekarang kita asumsikan papan berada di allLines dari baris tertentu
        // Kita cari window baris di allLines yang ukurannya height dengan asumsi papan
        // ada di tengah (atau coba cocokkan)
        // Supaya mudah, coba papan dimulai di baris: startIndex = exitRow - height/2
        // (asumsi pintu keluar di luar board)
        int startIndex = -1;

        // Coba cari startIndex agar allLines.subList(startIndex, startIndex + height)
        // panjangnya height dan tiap baris >= width
        for (int possibleStart = 0; possibleStart <= allLines.size() - height; possibleStart++) {
            boolean valid = true;
            for (int k = 0; k < height; k++) {
                if (allLines.get(possibleStart + k).length() < width) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                startIndex = possibleStart;
                break;
            }
        }

        if (startIndex == -1) {
            throw new IllegalArgumentException("Tidak ditemukan baris papan yang valid di file input.");
        }

        // Inisialisasi papan 2D
        char[][] board = new char[height][width];

        boolean pintuKiri = false;
        int pintuKiriRow = -1;

        if (exitRow >= startIndex && exitRow < startIndex + height && exitCol == 0) {
            pintuKiri = true;
            pintuKiriRow = exitRow - startIndex;
            for (int i = 0; i < height; i++) {
                String rowLine = allLines.get(startIndex + i);
                if (i == pintuKiriRow) {
                    // Baris yang ada K di kolom 0 tidak boleh diawali spasi
                    if (rowLine.charAt(0) == ' ') {
                        throw new IllegalArgumentException("Baris pintu kiri (K) tidak boleh diawali spasi.");
                    }
                } else {
                    // Baris lain harus diawali spasi
                    if (rowLine.length() <= width || rowLine.charAt(0) != ' ') {
                        throw new IllegalArgumentException(
                                "Baris ke-" + (i + 1) + " harus diawali spasi jika pintu keluar di kiri.");
                    }
                }
            }
        }

        for (int i = 0; i < height; i++) {
            String rowLine = allLines.get(startIndex + i);
            if (pintuKiri) {
                if (i == pintuKiriRow && rowLine.charAt(0) == 'K') {
                    // Baris pintu: abaikan K, ambil substring setelah K
                    rowLine = rowLine.substring(1);
                } else if (i != pintuKiriRow && rowLine.length() > width) {
                    // Baris lain: abaikan spasi pertama
                    rowLine = rowLine.substring(1);
                }
            }
            for (int j = 0; j < width; j++) {
                board[i][j] = rowLine.charAt(j);
            }
        }

        // Tentukan apakah pintu keluar horizontal atau vertical
        // Jika exitRow < startIndex → pintu keluar di atas papan → horizontal
        // Jika exitRow >= startIndex + height → pintu keluar di bawah papan →
        // horizontal
        // Jika exitRow di dalam range papan → pintu keluar di samping → vertical
        if (exitRow < startIndex || exitRow >= startIndex + height) {
            isHorizontal = false;
            // Karena pintu di luar baris papan, exitRow relatif ke papan = -1 (atas) atau
            // height (bawah)
            if (exitRow < startIndex) {
                exitRow = -1; // atas
            } else {
                exitRow = height; // bawah
            }
        } else {
            isHorizontal = true;
            // pintu keluar di samping kiri (-1) atau kanan (width)
            if (exitCol == 0) {
                exitCol = -1;
            } else if (exitCol >= width) {
                exitCol = width;
            }
            // exitRow sudah disesuaikan karena di dalam papan
            exitRow = exitRow - startIndex;
        }

        pintuKeluar = new PintuKeluar(exitRow, exitCol, isHorizontal);

        // Simpan dan proses papan
        papan = new Papan(width, height);
        processBoardConfiguration(board);

        // Validasi jumlah piece (N) sesuai input, N tidak menghitung primary piece
        int nonPrimaryCount = 0;
        for (String id : pieces.keySet()) {
            if (!id.equals("P")) {
                nonPrimaryCount++;
            }
        }
        if (nonPrimaryCount != numPieces) {
            throw new IllegalArgumentException("Jumlah piece pada papan tidak sesuai dengan input.");
        }

        // Validasi: primary piece dan pintu keluar harus sesuai aturan orientasi dan
        // baris/kolom
        if (primaryPiece != null && pintuKeluar != null) {
            if (primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
                if (!pintuKeluar.isHorizontal()) {
                    throw new IllegalArgumentException("Primary piece horizontal, pintu keluar harus horizontal.");
                }
                boolean sameRow = false;
                for (Position pos : primaryPiece.getPositions()) {
                    if (pos.getRow() == pintuKeluar.getRow()) {
                        sameRow = true;
                        break;
                    }
                }
                if (!sameRow) {
                    throw new IllegalArgumentException(
                            "Primary piece horizontal, pintu keluar harus di baris yang sama.");
                }
            } else if (primaryPiece.getOrientation() == Orientation.VERTICAL) {
                if (pintuKeluar.isHorizontal()) {
                    throw new IllegalArgumentException("Primary piece vertikal, pintu keluar harus vertikal.");
                }
                boolean sameCol = false;
                for (Position pos : primaryPiece.getPositions()) {
                    if (pos.getCol() == pintuKeluar.getCol()) {
                        sameCol = true;
                        break;
                    }
                }
                if (!sameCol) {
                    throw new IllegalArgumentException(
                            "Primary piece vertikal, pintu keluar harus di kolom yang sama.");
                }
            }
        }

        // Validasi primary piece dan pintu keluar
        if (primaryPiece != null && pintuKeluar != null) {
            if (primaryPiece.getOrientation() == Orientation.HORIZONTAL) {
                if (!pintuKeluar.isHorizontal()) {
                    throw new IllegalArgumentException("Posisi pintu keluar tidak sesuai.");
                }
                boolean sameRow = false;
                for (Position pos : primaryPiece.getPositions()) {
                    if (pos.getRow() == pintuKeluar.getRow()) {
                        sameRow = true;
                        break;
                    }
                }
                if (!sameRow) {
                    throw new IllegalArgumentException(
                            "Posisi pintu keluar tidak sesuai.");
                }
            } else if (primaryPiece.getOrientation() == Orientation.VERTICAL) {
                if (pintuKeluar.isHorizontal()) {
                    throw new IllegalArgumentException("Posisi pintu keluar tidak sesuai.");
                }
                boolean sameCol = false;
                for (Position pos : primaryPiece.getPositions()) {
                    if (pos.getCol() == pintuKeluar.getCol()) {
                        sameCol = true;
                        break;
                    }
                }
                if (!sameCol) {
                    throw new IllegalArgumentException(
                            "Posisi pintu keluar tidak sesuai.");
                }
            }
        }

        checkMultipleK(allLines);
        checkBarisKolomValid(allLines, startIndex, height, width, pintuKiri, pintuKiriRow);

    }

    /**
     * Processes the board configuration to identify all pieces
     * 
     * @param board the board configuration as a 2D array of characters
     */
    private void processBoardConfiguration(char[][] board) {
        processPieces(board);

        for (Piece piece : pieces.values()) {
            papan.addPiece(piece);
        }
    }

    /**
     * Identifies all pieces in the board configuration
     * 
     * @param board the board configuration
     */
    private void processPieces(char[][] board) {
        boolean[][] processed = new boolean[height][width];
        HashSet<String> idSet = new HashSet<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (processed[i][j] || board[i][j] == '.' || board[i][j] == 'K') {
                    continue;
                }

                String pieceId = String.valueOf(board[i][j]);
                if (idSet.contains(pieceId)) {
                    throw new IllegalArgumentException("Terdapat id piece yang sama: " + pieceId);
                }
                idSet.add(pieceId);

                Piece piece;
                if (pieceId.equals("P")) {
                    primaryPiece = new PrimaryPiece();
                    piece = primaryPiece;
                } else {
                    piece = new Piece(pieceId);
                }

                identifyPieceCells(board, processed, piece, i, j, pieceId);
                piece.determineOrientation();
                pieces.put(pieceId, piece);
            }
        }
    }

    private void identifyPieceCells(char[][] board, boolean[][] processed, Piece piece, int startRow, int startCol,
            String pieceId) {
        piece.addPosition(startRow, startCol);
        processed[startRow][startCol] = true;

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        for (int[] dir : directions) {
            int newRow = startRow + dir[0];
            int newCol = startCol + dir[1];

            if (isValidCell(newRow, newCol) && !processed[newRow][newCol] &&
                    board[newRow][newCol] == pieceId.charAt(0)) {
                identifyPieceCells(board, processed, piece, newRow, newCol, pieceId);
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    // Fungsi untuk mengecek jika ada lebih dari satu K di input
    private void checkMultipleK(ArrayList<String> allLines) {
        int kCount = 0;
        for (String line : allLines) {
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == 'K') {
                    kCount++;
                }
            }
        }
        if (kCount > 1) {
            throw new IllegalArgumentException("Terdapat lebih dari satu pintu keluar (K) pada input.");
        }
    }

    // Fungsi untuk validasi kelebihan baris dan kolom
    private void checkBarisKolomValid(ArrayList<String> allLines, int startIndex, int height, int width,
            boolean pintuKiri, int pintuKiriRow) {
        if (startIndex < 0 || startIndex + height > allLines.size()) {
            throw new IllegalArgumentException("Window papan di file input tidak valid.");
        }
        int barisPapan = 0;
        for (int i = 0; i < allLines.size(); i++) {
            if (i >= startIndex && i < startIndex + height) {
                String rowLine = allLines.get(i);
                int effectiveLength = rowLine.length();

                // Handle pintu kiri
                if (pintuKiri) {
                    if (i - startIndex == pintuKiriRow) {
                        if (rowLine.length() == 0 || rowLine.charAt(0) != 'K') {
                            throw new IllegalArgumentException("Baris pintu kiri harus diawali 'K'.");
                        }
                        effectiveLength = rowLine.length() - 1;
                    } else {
                        if (rowLine.length() == 0 || rowLine.charAt(0) != ' ') {
                            throw new IllegalArgumentException("Baris non-pintu kiri harus diawali spasi.");
                        }
                        effectiveLength = rowLine.length() - 1;
                    }
                } else {
                    if (rowLine.length() > width && rowLine.charAt(rowLine.length() - 1) == 'K') {
                        effectiveLength = rowLine.length() - 1;
                    } else if (rowLine.length() > 0 && rowLine.charAt(0) == 'K') {
                        effectiveLength = rowLine.length() - 1;
                    }
                }

                if (effectiveLength < width) {
                    throw new IllegalArgumentException(
                            "Baris ke-" + (i - startIndex + 1) + " kurang dari lebar papan.");
                }
                if (effectiveLength > width) {
                    throw new IllegalArgumentException("Baris ke-" + (i - startIndex + 1) + " melebihi lebar papan.");
                }

            }
            barisPapan++;
        }

        if (barisPapan != height) {
            throw new IllegalArgumentException("Jumlah baris papan tidak sesuai dengan height pada input.");
        }
    }

    public Map<String, Piece> getPieces() {
        return pieces;
    }

    public PrimaryPiece getPrimaryPiece() {
        return primaryPiece;
    }

    public PintuKeluar getPintuKeluar() {
        return pintuKeluar;
    }

    public Papan getPapan() {
        return papan;
    }

}
